package cm.packagemanager.pmanager.user.ent.dao;

import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.mail.MailSender;
import cm.packagemanager.pmanager.common.mail.MailType;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.security.PasswordGenerator;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.users.RegisterDTO;
import cm.packagemanager.pmanager.ws.requests.users.UpdateUserDTO;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public  class UserDAOImpl implements UserDAO {

	private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	//@Autowired
	Transaction tx;

	@Autowired
	RoleDAO roleDAO;



	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}



	public UserDAOImpl() {

	}

	@Override
	public UserVO login(String username, String password) {
		return  internalLogin( username,  password).get();
	}


	@Override
	public List<UserVO> getAllUsers() {

		Session session = this.sessionFactory.getCurrentSession();
		List<UserVO>  users = session.createQuery("from UserVO").list();
		return users;
	}

	@Override
	public List<UserVO>  getAllUsers(int firstResult, int maxResults) {

		int paginatedCount = 0;
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query = session.createQuery("from UserVO");
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);

		List<UserVO> users = (List) query.list();

		if (users != null) {
			paginatedCount = users.size();
			System.out.println("Total Results: " + paginatedCount);
			for (UserVO user : users) {
				System.out.println("Retrieved Product using Query. Name: " + user.getUsername());
			}
		}

		return users;

	}

	@Override
	public UserVO getUser(Long id) {
		UserVO user = findById(id);
		return user;

	}

	@Override
	public UserVO register(RegisterDTO register) {

		try {
			UserVO userError=new UserVO();
			UserVO user = findByEmail(register.getEmail(),true);
			if(user!=null){

				userError.setError(WebServiceResponseCode.ERROR_EMAIL_REGISTER_LABEL);
				return userError;
			}

			if(findByUsername(register.getUsername())!=null){
				userError.setError(WebServiceResponseCode.ERROR_USERNAME_REGISTER_LABEL);
				return userError;
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
			Session session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();

			if(StringUtils.equals(register.getRole().name(), RoleEnum.ADMIN.name())){
				setRole(user.getEmail(), RoleEnum.ADMIN);
			}else{
				setRole(user.getEmail(), RoleEnum.USER);
			}
			return findByEmail(user.getEmail(),false);

		} catch (UserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UserVO updateUser(UpdateUserDTO userDTO) {

		Session session = this.sessionFactory.getCurrentSession();

		UserVO user=findById(userDTO.getId());

		if (user!=null){
			user.setEmail(userDTO.getEmail());
			user.setPhone(userDTO.getPhone());
			session.update(user);
			return user;

		}else throw new BusinessResourceException("Aucun utilisateur trouvé");

	}


	@Override
	public void updateUser(UserVO user) {

		Session session = this.sessionFactory.getCurrentSession();
		session.update(user);

	}

	@Override
	public boolean managePassword(UserVO user) throws BusinessResourceException {


		UserVO usr=findByEmail(user.getEmail(),true);

		if(usr!=null){

			UserVO transientUser=usr;

			String decrypt=PasswordGenerator.decrypt(usr.getPassword());
			//transientUser.setPassword(decrypt);

			List<String> labels=new ArrayList<String>();
			List<String> emails=new ArrayList<String>();

			labels.add(MailType.PASSWORD_KEY);
			labels.add(MailType.USERNAME_KEY);

			emails.add(transientUser.getEmail());

			MailSender mailSender = new MailSender();
			return mailSender.sendMailMessage(MailType.PASSWORD_TEMPLATE,MailType.PASSWORD_TEMPLATE_TITLE,	MailSender.replace(transientUser,labels,null,decrypt),
					emails,null,null, null,transientUser.getUsername());
		}
		return false;
	}

	@Override
	public boolean deleteUser(Long id) {

		UserVO user=updateDelete(id);

		return  (user!=null) ? user.isCancelled() : false;

	}


	@Override
	public boolean sendMail(MailDTO mr, boolean active) {

		UserVO user=findByEmail(mr.getFrom(),active);

		if(user!=null){
			MailSender mailSender = new MailSender();
			List<String> labels=new ArrayList<String>();

			labels.add(MailType.BODY_KEY);

			return mailSender.sendMailMessage(MailType.SEND_MAIL_TEMPLATE,
					mr.getSubject(),MailSender.replace(user,labels,mr.getBody(),null),mr.getTo(),mr.getCc(),mr.getBcc(), mr.getFrom(),user.getUsername());

		}


		return  false;

	}

	@Override
	public UserVO save(UserVO user) throws BusinessResourceException {
		Session session = this.sessionFactory.getCurrentSession();
		if(user!=null){
			session.update(user);
			return user;
		}
		return null;
	}


	@Override
	public UserVO update(UserVO user) throws BusinessResourceException {
		Session session = this.sessionFactory.getCurrentSession();
		if(user!=null){
			session.update(user);
			return user;
		}
		return null;
	}



	@Override
	public Optional<UserVO> findByUsername(String username) throws BusinessResourceException, UserException {


		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query=session.getNamedQuery(UserVO.USERNAME);
		query.setParameter("username", username);

		List<UserVO> users=query.list();
		if(users!=null && users.size()>0) {
			return Optional.of(users.get(0));
		}
		return null;
	}


	@Override
	public UserVO findByOnlyUsername(String username) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query=session.getNamedQuery(UserVO.USERNAME);
		query.setParameter("username", username);

		List<UserVO> users=query.list();
		if(users!=null && users.size()>0) {
			return users.get(0);
		}
		return null;
	}


	@Override
	public UserVO findByToken(String token) throws BusinessResourceException {

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
	public UserVO findById(Long userId) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query=session.getNamedQuery(UserVO.FINDBYID);
		query.setParameter("id", userId);

		List<UserVO> users=query.list();
		if(users!=null && users.size()>0) {
			return users.get(0);
		}
		return null;
	}

	// MANDATORY: Transaction must be created before.
	@Transactional(propagation = Propagation.MANDATORY)
	public void registerUser(UserVO user) throws UserException {
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		UserVO usr = this.login(user.getUsername());
		if (usr != null) {
			throw new UserException(" Username deja existant " + user.getUsername());
		}
		session.save(user);
	}

	@Override
	public UserVO login(String username) throws UserException {
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		return session.get(UserVO.class, username);
	}

	@Override
	public UserVO findByEmail(String email, boolean active) throws BusinessResourceException{

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		if(active){
			session.enableFilter(FilterConstants.ACTIVE_MBR);
		}

		Query query=session.getNamedQuery(UserVO.EMAIL);
		query.setParameter("email", email);


		List<UserVO> users=query.list();
		if(users!=null && users.size()>0) {
			return users.get(0);

		}
		return null;
	}

	@Override
	public UserVO findByFacebookId(String facebookId) throws BusinessResourceException {
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query=session.getNamedQuery(UserVO.FACEBOOK);
		query.setParameter("facebookId", facebookId);


		List<UserVO> users=query.list();
		if(users!=null && users.size()>0) {
			return users.get(0);

		}
		return null;
	}

	@Override
	public UserVO findByGoogleId(String googleId) throws BusinessResourceException {
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query=session.getNamedQuery(UserVO.GOOGLE);
		query.setParameter("googleId", googleId);


		List<UserVO> users=query.list();
		if(users!=null && users.size()>0) {
			return users.get(0);

		}
		return null;
	}

	private UserVO updateDelete(Long id) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);

		Query query=session.getNamedQuery(UserVO.FINDBYID);
		query.setParameter("id", id);
		List<UserVO> users=query.getResultList();

		UserVO user=(!users.isEmpty())?users.get(0):null;
		if(user!=null){
			user.setCancelled(true);
			session.merge(user);
			return session.get(UserVO.class,user);
		}else{
			return null;
		}

	}

	private Optional<UserVO> internalLogin(String username, String password) throws BusinessResourceException{
		try {

			String encryptedPassword=PasswordGenerator.encrypt(password);

			if(encryptedPassword==null)
				throw new BusinessResourceException("UserNotFound", "Mot de passe incorrect", HttpStatus.NOT_FOUND);

			Optional<UserVO> userFound = findByUsername(username);
			if(encryptedPassword.equals(userFound.get().getPassword())) {
				return userFound;
			} else {
				throw new BusinessResourceException("UserNotFound", "Mot de passe incorrect", HttpStatus.NOT_FOUND);
			}
		} catch (BusinessResourceException ex) {
			logger.error("Login ou mot de passe incorrect", ex);
			throw new BusinessResourceException("UserNotFound", "Login ou mot de passe incorrect", HttpStatus.NOT_FOUND);
		}catch (Exception ex) {
			logger.error("Une erreur technique est survenue", ex);
			throw new BusinessResourceException("TechnicalError", "Une erreur technique est survenue", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public boolean setRole(String email, RoleEnum roleId) throws BusinessResourceException {

		UserVO user= findByEmail(email, false);
		RoleVO role = roleDAO.findByDescription(roleId.name());


		if (user!=null && role!=null){
			user.getRoles().add(role);
			return (save(user)!=null);
		}else{
			return false;
		}
	}

	@Override
	public boolean deleteUser(UserVO user) throws BusinessResourceException {
		if(user!=null){

			return updateDelete(user.getId())!=null;
		}
		return false;
	}


/*@Override
	public boolean deleteUser(Long id) {

		Session session = this.sessionFactory.getCurrentSession();

		UserVO user=updateDelete(id);

		if(user!=null){
			user = (UserVO) session.load(UserVO.class, id);
			if (user!= null) {
				session.delete(user);
				return true;
			}
		}
		else {
			return false;
		}
		return false;
	}*/

}
