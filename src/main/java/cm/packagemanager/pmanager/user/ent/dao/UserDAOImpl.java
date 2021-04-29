package cm.packagemanager.pmanager.user.ent.dao;

import cm.packagemanager.pmanager.common.ent.dao.Generic;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.communication.ent.vo.CommunicationVO;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.security.PasswordGenerator;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.users.*;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.opentracing.Span;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Repository
public  class UserDAOImpl extends Generic implements UserDAO {

	private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);


	@Autowired
	RoleDAO roleDAO;
	@Autowired
	Span span;
	public UserDAOImpl() {

	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public int count(PageBy pageBy) throws BusinessResourceException {
		logger.info("User - count ");

		return  count(UserVO.class,pageBy);
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public UserVO login(String username, String password) {
		/*Optional optional=internalLogin(username,password);
		if (optional==null) return null;
		return  (UserVO) optional.get();*/
		logger.info("User: login");
		return internalLogin(username,password);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = UserException.class)
	public void subscribe(SubscribeDTO subscribe) throws UserException {
		UserVO subscriber=findById(subscribe.getSubscriberId());
		UserVO subscription=findById(subscribe.getSubscriptionId());

		if (subscriber!=null && subscription!=null){
			subscriber.addSubscription(subscription);
			subscription.addSubscriber(subscriber);

			Session session=sessionFactory.getCurrentSession();
			session.update(subscriber);
			session.update(subscription);
		}else throw new UserException("Une erreur survenue pendant l'abonnement, veuillez reessayer");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = UserException.class)
	public void unsubscribe(SubscribeDTO subscribe) throws UserException {

		UserVO subscriber=findById(subscribe.getSubscriberId());
		UserVO subscription=findById(subscribe.getSubscriptionId());

		if (subscriber!=null && subscription!=null){
			subscriber.removeSubscription(subscription);
			subscription.removeSubscriber(subscriber);

			Session session=sessionFactory.getCurrentSession();
			session.update(subscriber);
			session.update(subscription);
		}else throw new UserException("Une erreur survenue pendant la desinscription, veuillez reessayer");
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation.REQUIRED,rollbackFor = UserException.class)
	public List<UserVO> subscriptions(Long  userId) throws UserException {
		UserVO user=findById(userId);
		if (user ==null)
			throw new UserNotFoundException("Utilisateur non trouvé");

		return  new ArrayList<UserVO>(user.getSubscriptions());
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation.REQUIRED,rollbackFor = UserException.class)
	public List<UserVO> subscribers(Long  userId) throws UserException {
		UserVO user=findById(userId);
		if (user ==null)
			throw new UserNotFoundException("Utilisateur non trouvé");

		return new ArrayList<UserVO>(user.getSubscribers());
	}


	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public List<UserVO> getAllUsers() throws Exception {
		logger.info("User: all users");
		return all(UserVO.class, null);
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public List<UserVO> getAllUsersToConfirm() throws Exception {
		logger.info("User:  users to confirm");
		return findBy(UserVO.JOB_CONFIRM, UserVO.class,0,"act",null);
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public List<UserVO>  getAllUsers(PageBy pageBy) throws Exception {

		String[] filters = new String[2];
		filters[0]=FilterConstants.CANCELLED;
		filters[1]=FilterConstants.ACTIVE_MBR;

		logger.info("User: all users page by");
		return all(UserVO.class, null,filters);
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public UserVO getUser(Long id)  throws UserException{
		logger.info("User: get user");
		try {
			UserVO user= findById(id);
			calcolateAverage(user);
			return user;
		} catch (UserException e) {
			logger.error("User:" +e.getMessage());
			throw e;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor =UserException.class)
	public UserVO register(RegisterDTO register) throws UserException {
		logger.info("User: register");
		try {
			UserVO user=findByNaturalIds(register.getUsername(), register.getEmail(),true);
			if(user!=null){
				logger.error("User: username deja exisitant");
				user.setError(WebServiceResponseCode.ERROR_USERNAME_REGISTER_LABEL + " et "+WebServiceResponseCode.ERROR_EMAIL_REGISTER_LABEL);
				return user;
			}

			user = findByEmail(register.getEmail());
			if(user!=null){
				logger.error("User: email existe deja");
				user.setError(WebServiceResponseCode.ERROR_EMAIL_REGISTER_LABEL);
				return user;
			}

			user=findByOnlyUsername(register.getUsername(),true);
			if(user!=null){
				user.setError(WebServiceResponseCode.ERROR_USERNAME_REGISTER_LABEL);
				return user;
			}
			user=new UserVO();
			user.setEmail(register.getEmail());
			user.setUsername(register.getUsername());
			user.setPassword(PasswordGenerator.encrypt(register.getPassword()));
			user.setFirstName(register.getFirstName());
			user.setLastName(register.getLastName());
			user.setPhone(register.getPhone());
			user.setActive(0);
			user.setEnableNotification(true);
			user.setGender(register.getGender());
			user.setConfirmationToken(UUID.randomUUID().toString());

			Session session = this.sessionFactory.getCurrentSession();
			session.save(user);
			setRole(user, register.getRole());
			return user;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new UserException("Erreur durant l'inscriptions de l'utilisateur");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor =UserException.class)
	public UserVO updateUser(UpdateUserDTO userDTO) throws Exception {
		logger.info("User: update");
		Session session = this.sessionFactory.getCurrentSession();

		UserVO user=findById(userDTO.getId());

		if(user==null ){
			user=findByEmail(userDTO.getEmail());
		}

		if (user!=null){
			//user.setEmail(userDTO.getEmail()); // on ne peut pas ajourner le NaturalId
			user.setPhone(userDTO.getPhone());
			user.setGender(userDTO.getGender());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			//setRole(user, userDTO.getRole());
			calcolateAverage(user);
			session.update(user);
			return user;

		}else throw  new UserException("Aucun utilisateur trouvé");

	}


	@Override
	@Transactional(propagation = Propagation. REQUIRED,rollbackFor = Exception.class)
	public void updateUser(UserVO user) {

		Session session = this.sessionFactory.getCurrentSession();
		session.update(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor =UserException.class)
	public boolean deleteUser(Long id) throws UserException{
		logger.info("User: delete");
		//delete(UserVO.class,id,false);
		return  updateDelete(id);
	}



	@Override
	@Transactional(propagation = Propagation. REQUIRED)
	public UserVO save(UserVO user) throws BusinessResourceException {
		logger.info("User: save");
		Session session = this.sessionFactory.getCurrentSession();
		if(user!=null){
			session.update(user);
			return user;
		}
		return null;
	}


	@Override
	@Transactional(propagation = Propagation. REQUIRED,rollbackFor = BusinessResourceException.class)
	public void remove(UserVO user) throws BusinessResourceException {
		Session session = this.sessionFactory.getCurrentSession();
		if(user!=null){
			session.remove(user);
		}
	}


	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public UserVO findByUsername(String username) throws Exception {

		 logger.info("User: find by username");
		 String[] filters = new String[2];
		 filters[0]=FilterConstants.CANCELLED;
		 filters[1]=FilterConstants.ACTIVE_MBR;
		 return (UserVO) findByUniqueResult(UserVO.USERNAME,UserVO.class,username,"username",null,filters);

	}


	@Override
	public UserVO findByOnlyUsername(String username,boolean disableFilter) throws Exception {
		logger.info("User: find by only username");
		String[] filters = null;

		if(!disableFilter){
			filters = new String[2];
			filters[0]=FilterConstants.CANCELLED;
			filters[1]=FilterConstants.ACTIVE_MBR;
		}

		return (UserVO) findByUniqueResult(UserVO.USERNAME,UserVO.class,username,"username",null,filters);

	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation.REQUIRED)
	public List<UserVO> find(UserSeachDTO userSeachDTO, PageBy pageBy) throws BusinessResourceException {


		Session session = sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);

		String where=composeQuery(userSeachDTO, "u");
		Query query=session.createQuery("from UserVO  as a "+ where);
		composeQueryParameters(userSeachDTO,query);
		query.setFirstResult(pageBy.getPage());
		query.setMaxResults(pageBy.getSize());
		return query.list();
	}

	public UserVO findByNaturalIds(String username,String email, boolean disableFilter) throws BusinessResourceException {

		logger.info("User: find by NaturalIds");
		Session session = this.sessionFactory.getCurrentSession();
		if(!disableFilter){
			session.enableFilter(FilterConstants.CANCELLED);
			session.enableFilter(FilterConstants.ACTIVE_MBR);
		}
		UserVO user = session.byNaturalId(UserVO.class)
				.using("username",username)
				.using("email",email)
				.getReference();

		/*Query query=session.getNamedQuery(UserVO.USERNAME);
		query.setParameter("username", username);
		UserVO user= (UserVO) CollectionsUtils.getFirstOrNull(query.list());*/
		return user;
	}


	@Override
	public UserVO findByToken(String token) throws Exception {
		logger.info("User: find by token");
		String[] filters = new String[1];
		filters[0]=FilterConstants.CANCELLED;

		return (UserVO) findByUniqueResult(UserVO.CONF_TOKEN,UserVO.class,token,"ctoken",null,filters);
	}

	@Override
	@Transactional
	public UserVO findById(Long id) throws UserException {

		try{
			logger.info("User: find by id");
			Session session = sessionFactory.getCurrentSession();
			session.enableFilter(FilterConstants.ACTIVE_MBR);
			session.enableFilter(FilterConstants.CANCELLED);

			UserVO user = (UserVO) session.find(UserVO.class,id);
			//Hibernate.initialize(user.getMessages()); // on l'utilise quand la fetch avec message est lazy
			return user;
		}catch (Exception e){
			throw  new UserException(e.getMessage());
		}
	}



	@Override
	public UserVO login(String username) throws UserException {
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		return session.get(UserVO.class, username);
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation.REQUIRED)
	public UserVO findByEmail(String email) throws Exception {
		logger.info("User: find by email");
        return (UserVO) findByUniqueResult(UserVO.EMAIL,UserVO.class,email,"email");
	}

	@Override
	public UserVO findByFacebookId(String facebookId) throws Exception {
		logger.info("User: find by facebook");
		String[] filters= new String[2];
		filters[0]=FilterConstants.CANCELLED;
		filters[1]=FilterConstants.ACTIVE_MBR;
		return (UserVO) findByUniqueResult(UserVO.FACEBOOK,UserVO.class,facebookId,"facebookId",null,filters);
	}

	@Override
	public UserVO findByGoogleId(String googleId) throws Exception {
		logger.info("User: find by google");
		String[] filters= new String[2];
		filters[0]=FilterConstants.CANCELLED;
		filters[1]=FilterConstants.ACTIVE_MBR;
		return (UserVO) findByUniqueResult(UserVO.FACEBOOK,UserVO.class,googleId,"googleId",null,filters);

	}

	@Override
	public boolean setRole(UserVO user, RoleEnum roleId) throws Exception {
		logger.info("User:  set role");

		RoleVO role = roleDAO.findByDescription(roleId.name());

		if (user!=null && role!=null){
			user.addRole(role);
			return (save(user)!=null);
		}else{
			return false;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor =UserException.class)
	public boolean setRole(String email , RoleEnum roleId) throws Exception {

		UserVO user= findByEmail(email);
		return  setRole( user, roleId);

	}

	@Override
	@Transactional(propagation = Propagation. REQUIRED, rollbackFor = {BusinessResourceException.class,UserException.class})
	public boolean deleteUser(UserVO user) throws BusinessResourceException, UserException {
		if(user!=null){
			logger.info("User: update delete");
			return updateDelete(user.getId());
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public boolean checkLogin(LoginDTO lr) throws Exception {
		logger.info("User: check login");
		UserVO user=findByOnlyUsername(lr.getUsername(), true);
		return  user!=null;
	}

/*	@Override
	public List<RatingCountVO> findRatingCounts(UserVO user) {
		Session session= sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createNamedQuery(ReviewVO.RATING,RatingCountVO.class);
		query.setParameter("userId", user);
		return query.getResultList();
	}*/

	@Override
	public boolean updateDelete(Long id) throws BusinessResourceException ,UserException{
		boolean result=false;

		try{

			Session session=sessionFactory.getCurrentSession();

			UserVO user=findById(id);
			if(user!=null){
				user.updateDeleteChildrens();
				user.setCancelled(true);
				session.merge(user);
				user = session.get(UserVO.class, id);

				result= (user!=null) && (user.isCancelled());
			}
		}catch (Exception e){
			throw new UserException(e.getMessage());
		}
		return result;
	}

	@Override
	public UserVO updateImage(Long userId, MultipartFile multipartFile) throws IOException {

		String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename());
		UserVO user = findById(userId);
		//user.setPicture(fileName);
		Session session=sessionFactory.getCurrentSession();
		user.setCancelled(true);
		session.merge(user);

		String uploadDir = imagesFolder + user.getId();

		fileUtils.saveFileToFileSystem(uploadDir, fileName, multipartFile);
		return null;
	}

	@Override
	@Transactional(rollbackFor = {UserException.class,Exception.class})
	public UserVO manageNotification(Long userId, boolean enableNotification) throws UserException{

				UserVO user=findById(userId);
		try {
			Session session=sessionFactory.getCurrentSession();
			boolean precedent=user.isEnableNotification();
			if(user!=null && precedent!=enableNotification){
				user.setEnableNotification(enableNotification);
				session.update(user);
				return  session.get(UserVO.class, userId);
			}
		}catch (UserException e){
			logger.error("Erreur durant la modification de la gestion des notifications");
			throw e;
		}
		return user;
	}

	@Override
	@Transactional
	public boolean editPassword(Long userId, String oldPassword, String newPassword) throws UserException {
		logger.info("Modification du mot de passe");
		UserVO user=findById(userId);
		if(user==null){
			logger.error("Erreur : aucun utilisateur correspondant a l'id:"+userId);
			throw new UserException( "Aucun utilisateur trouvé avec cet identifiant"+userId);
		}
		String decryptedPassword=PasswordGenerator.decrypt(user.getPassword());
		if(!StringUtils.equals(decryptedPassword,oldPassword)){
			logger.error("Erreur : mots de passe inexacts {}-{}:",decryptedPassword,oldPassword);
			throw new UserException( "Le mot de passe ne correspond pas: veuillez contrôler l'ancien mot de passe");
		}
		Session session=sessionFactory.getCurrentSession();
		user.setPassword(PasswordGenerator.encrypt(newPassword));
		session.merge(user);
		return true;
	}

	@Override
	@Transactional
	public List<CommunicationVO> communications(Long userId) throws Exception {
		UserVO user=findById(userId);
		if(user==null){
			logger.error("Erreur : aucun utilisateur correspondant a l'id:"+userId);
			throw new UserException( "Aucun utilisateur trouvé avec cet identifiant"+userId);
		}

		/*List<UserVO> communications=comms.stream()
										.map(com ->com.getUsers())
										.flatMap(c ->c.stream())
										.filter(usr ->usr.getId()==userId)
										.collect(Collectors.toList());*/

		return user.getCommunications().stream().collect(Collectors.toList());
	}

	@Override
	public List notifications(Long userId) throws Exception {
		return null;
	}

	@Override
	public List messages(Long userId) throws Exception {
		return null;
	}


	private UserVO internalLogin(String username, String password) throws BusinessResourceException{
		try {
			logger.info("User: internal login");
			String encryptedPassword= PasswordGenerator.encrypt(password);

			if(encryptedPassword==null)
				throw new BusinessResourceException("UserNotFound", "Mot de passe incorrect", HttpStatus.NOT_FOUND);

			//Optional<UserVO> userFound = findByUsername(username);
			UserVO userFound = findByUsername(username);
			if (userFound==null){
				return null;
			}
			if(encryptedPassword.equals(userFound.getPassword())) {
				return userFound;
			} else {
				throw new BusinessResourceException("UserNotFound", "Mot de passe incorrect", HttpStatus.NOT_FOUND);
			}
		} catch (BusinessResourceException ex) {
			logger.error("Login ou mot de passe incorrect", ex);
			throw new BusinessResourceException("UserNotFound", " Nom utilisateur ou mot de passe incorrect", HttpStatus.NOT_FOUND);
		}catch (Exception ex) {
			logger.error("Une erreur technique est survenue", ex);
			throw new BusinessResourceException("TechnicalError", "Une erreur technique est survenue", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public  String composeQuery(Object o, String alias) {

		UserSeachDTO userSeach=(UserSeachDTO)o;

		StringBuilder hql = new StringBuilder(" where ");
		try {
			boolean and = false;

			if (StringUtils.isNotEmpty(userSeach.getUsername())) {
				hql.append(alias+".username=:username ");
			}
			and = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(userSeach.getUsername())) {
				if (and) {
					hql.append(" and ");
				}
				hql.append(alias+".announceType=:announceType ");
			}
		}catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return hql.toString();
	}

	@Override
	public  void composeQueryParameters(Object o,Query query) {

	}
}
