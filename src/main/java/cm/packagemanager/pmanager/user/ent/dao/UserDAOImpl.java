package cm.packagemanager.pmanager.user.ent.dao;

import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.Gender;
import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.security.PasswordGenerator;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.users.LoginDTO;
import cm.packagemanager.pmanager.ws.requests.users.RegisterDTO;
import cm.packagemanager.pmanager.ws.requests.users.UpdateUserDTO;
import cm.packagemanager.pmanager.ws.requests.users.UserSeachDTO;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;



@Repository
public  class UserDAOImpl extends CommonFilter implements UserDAO {

	private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;


	@Autowired
	RoleDAO roleDAO;


	public UserDAOImpl() {

	}

	@Override
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
	public UserVO login(String username, String password) {
		/*Optional optional=internalLogin(username,password);
		if (optional==null) return null;
		return  (UserVO) optional.get();*/
		logger.info("User: login");
		return internalLogin(username,password);
	}


	@Override
	public List<UserVO> getAllUsers() {
		logger.info("User: all users");
		Session session = this.sessionFactory.getCurrentSession();
		List<UserVO>  users = session.createQuery("from UserVO").list();
		return users;
	}

	@Override
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
	public UserVO getUser(Long id) {
		logger.info("User: get user");
		try {
			return  findById(id);

		} catch (UserException e) {
			logger.error("User:" +e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
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
	public void updateUser(UserVO user) {

		Session session = this.sessionFactory.getCurrentSession();
		session.update(user);
	}

	@Override
	public boolean deleteUser(Long id) throws UserException{
		logger.info("User: delete");
		//deleteObject(id);
		return updateDelete(id);
	}



	@Override
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
	public void remove(UserVO user) throws BusinessResourceException {
		Session session = this.sessionFactory.getCurrentSession();
		if(user!=null){
			session.remove(user);
		}
	}


	@Override
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
	public List<UserVO> find(UserSeachDTO userSeachDTO, PageBy pageBy) throws BusinessResourceException {


		Session session = sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);

		String where=composeQuery(userSeachDTO, "u");
		Query query=session.createQuery("from UserVO  as a "+ where);
		composeQueryParameters(userSeachDTO,query);
		query.setFirstResult(pageBy.getPage());
		query.setMaxResults(pageBy.getSize());
		List announces = query.list();

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
			return  (UserVO) manualFilter(session.find(UserVO.class,id));
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
	public boolean setRole(String email , RoleEnum roleId) throws BusinessResourceException {

		UserVO user= findByEmail(email);
		return  setRole( user, roleId);

	}

	@Override
	public boolean deleteUser(UserVO user) throws BusinessResourceException, UserException {
		if(user!=null){
			logger.info("User: update delete");
			return updateDelete(user.getId());
		}
		return false;
	}

	@Override
	public boolean checkLogin(LoginDTO lr) throws BusinessResourceException, UserException {
		logger.info("User: check login");
		UserVO user=findByOnlyUsername(lr.getUsername(), true);
		return  (UserVO)manualFilter(user)!=null;
	}

	@Override
	public void deleteObject(Object object) throws RecordNotFoundException {
		Long id = (Long)object;
		try{
			Session session=sessionFactory.getCurrentSession();

			UserVO user=findById(id);
			if(user!=null){
				session.remove(user);
				//session.flush(); // Etant donné que la suppression de l'entité UserVO entraine la suppression des autres en
				// entités pas besoin de faire le flush car le flush fait la synchronisation entre l'entité et la session hibernate
				// du coup cree une transaction et enverra en erreur la remove
			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}
	}

	@Override
	public boolean updateDelete(Long id) throws BusinessResourceException ,UserException{
		boolean result=false;

		try{

			Session session=sessionFactory.getCurrentSession();

			UserVO user=findById(id);
			if(user!=null){
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
