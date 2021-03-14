package cm.packagemanager.pmanager.user.ent.dao;

import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.rating.ent.vo.RatingCountVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.security.PasswordGenerator;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.users.*;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.opentracing.Span;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;



@Repository
public  class UserDAOImpl extends CommonFilter implements UserDAO {

	private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	private static final MathContext MATH_CONTEXT = new MathContext(2,RoundingMode.HALF_UP);

	@Autowired
	private SessionFactory sessionFactory;


	@Autowired
	RoleDAO roleDAO;


	@Autowired
	Span span;

	public UserDAOImpl() {

	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public int count(PageBy pageBy) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query = session.createQuery("from UserVO ");
		//query.setFirstResult(pageBy.getPage());
		//query.setMaxResults(pageBy.getSize());
		int count = CollectionsUtils.isNotEmpty(query.list())?query.list().size():0;
		return count;
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
			session.flush();
		}else throw new UserException("Une erreur survenue pendant l'abonnement, veuillez reessayer");

	}

	@Override
	public void subscription(SubscribeDTO subscribe) throws UserException {

	}


	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public List<UserVO> getAllUsers() {
		logger.info("User: all users");
		Session session = this.sessionFactory.getCurrentSession();
		List<UserVO>  users = session.createQuery("from UserVO").list();
		return users;
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public List<UserVO> getAllUsersToConfirm() {
		logger.info("User: all users");
		Session session = this.sessionFactory.getCurrentSession();
		List  users =
				 session.createQuery("from UserVO where confirmationToken is not  null  and active =:act")
				.setParameter("act",0).setMaxResults(20)
				.list();
		return users;
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public List<UserVO>  getAllUsers(PageBy pageBy) {

		logger.info("User: all users page by");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from UserVO");
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		query.setFirstResult(pageBy.getPage());
		query.setMaxResults(pageBy.getSize());

		List<UserVO> users = (List) query.list();
		return users;

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
			user.setGender(register.getGender());
			user.setConfirmationToken(UUID.randomUUID().toString());

			Session session = this.sessionFactory.getCurrentSession();
			session.save(user);
			session.flush();
			session.refresh(user);

			user=session.get(UserVO.class,user.getId());
			setRole(user, register.getRole());
			return session.get(UserVO.class,user.getId());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new UserException("Erreur durant l'inscriptions de l'utilisateur");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor =UserException.class)
	public UserVO updateUser(UpdateUserDTO userDTO) throws UserException {
		logger.info("User: update");
		Session session = this.sessionFactory.getCurrentSession();

		UserVO user=findByEmail(userDTO.getEmail());

		if(user==null ){
			user=findById(userDTO.getId());
		}


		if (user!=null){
			//user.setEmail(userDTO.getEmail()); // on ne peut pas ajourner le NaturalId
			user.setPhone(userDTO.getPhone());
			user.setGender(userDTO.getGender());
			setRole(user, userDTO.getRole());
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={BusinessResourceException.class, UserException.class})
	public UserVO update(UserVO user) throws BusinessResourceException {
		logger.info("User: update 2");
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
	public UserVO findByUsername(String username) throws BusinessResourceException, UserException {

		logger.info("User: find by username");
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);

		Query query=session.getNamedQuery(UserVO.USERNAME);
		query.setParameter("username", username);
		UserVO user= (UserVO) query.uniqueResult();
		return user;
	}


	@Override
	public UserVO findByOnlyUsername(String username,boolean disableFilter) throws BusinessResourceException {
		logger.info("User: find by only username");
		Session session = this.sessionFactory.getCurrentSession();
		if(!disableFilter){
			session.enableFilter(FilterConstants.CANCELLED);
			session.enableFilter(FilterConstants.ACTIVE_MBR);
		}
		Query query=session.getNamedQuery(UserVO.USERNAME);
		query.setParameter("username", username);
		UserVO user= (UserVO) CollectionsUtils.getFirstOrNull(query.list());
		return user;
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
		List users = query.list();

		return null;
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
	public UserVO findByToken(String token) throws BusinessResourceException {

		logger.info("User: find by toker");
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		//session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query=session.getNamedQuery(UserVO.CONF_TOKEN);
		query.setParameter("ctoken", token);

		List<UserVO> users=query.list();
		if(users!=null && users.size()>0) {
			return users.get(0);
		}
		return null;
	}

	@Override
	public UserVO findById(Long id) throws UserException {

		try{
			logger.info("User: find by id");
			Session session = sessionFactory.getCurrentSession();
			session.enableFilter(FilterConstants.ACTIVE_MBR);
			session.enableFilter(FilterConstants.CANCELLED);

			UserVO user = (UserVO) manualFilter(session.find(UserVO.class,id));
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
	public UserVO findByEmail(String email) throws BusinessResourceException{
		logger.info("User: find by email");
		Session session = this.sessionFactory.getCurrentSession();

		Query query=session.getNamedQuery(UserVO.EMAIL);
		query.setParameter("email", email);

		UserVO user= (UserVO) query.uniqueResult();
		return user;
	}

	@Override
	public UserVO findByFacebookId(String facebookId) throws BusinessResourceException {
		logger.info("User: find by facebook");
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query=session.getNamedQuery(UserVO.FACEBOOK);
		query.setParameter("facebookId", facebookId);

		UserVO user= (UserVO) query.uniqueResult();
		return user;
	}

	@Override
	public UserVO findByGoogleId(String googleId) throws BusinessResourceException {
		logger.info("User: find by google");
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query=session.getNamedQuery(UserVO.GOOGLE);
		query.setParameter("googleId", googleId);

		UserVO user= (UserVO) query.uniqueResult();
		return user ;
	}

	@Override
	public boolean setRole(UserVO user, RoleEnum roleId) throws BusinessResourceException {
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
	public boolean setRole(String email , RoleEnum roleId) throws BusinessResourceException {

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
	public boolean checkLogin(LoginDTO lr) throws BusinessResourceException, UserException {
		logger.info("User: check login");
		UserVO user=findByOnlyUsername(lr.getUsername(), true);
		return  (UserVO)manualFilter(user)!=null;
	}

	@Override
	public List<RatingCountVO> findRatingCounts(UserVO user) {
		Session session= sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createNamedQuery(ReviewVO.RATING,RatingCountVO.class);
		query.setParameter("userid", user);
		return query.getResultList();
	}

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

	private double calcolateAverage(UserVO user){
		if(user==null)
			return 0.0;
		int totalRating=0;
		int counterRating=0;
		List<RatingCountVO> ratingCounts =findRatingCounts(user);
		if(CollectionsUtils.isEmpty(ratingCounts)){
			return 0.0;
		}
		for(int i=0;i<ratingCounts.size();i++){
			RatingCountVO ratingCount=ratingCounts.get(i);
			counterRating+=ratingCount.getCount();
			totalRating+=ratingCount.getRating().toValue()*ratingCount.getCount();
		}
		double averageRating = new BigDecimal(((double) totalRating/(double) counterRating),MATH_CONTEXT).doubleValue();
		user.setRating(averageRating);
		return averageRating;
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
