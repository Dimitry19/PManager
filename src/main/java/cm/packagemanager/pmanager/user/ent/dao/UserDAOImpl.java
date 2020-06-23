package cm.packagemanager.pmanager.user.ent.dao;

import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.mail.MailSender;
import cm.packagemanager.pmanager.common.mail.MailType;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.security.PasswordGenerator;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.MailRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public  class UserDAOImpl implements UserDAO{

	private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	HibernateTransactionManager tx;

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
	public User findUserWithName(String username) {
		return null;
	}

	@Override
	public List<UserVO> getAllUsers() {
		Session session = this.sessionFactory.getCurrentSession();
		List<UserVO>  users = session.createQuery("from UserVO").list();
		return users;
	}

	@Override
	public UserVO getUser(Long id) {

		Session session = this.sessionFactory.getCurrentSession();
		UserVO user = (UserVO) session.get(UserVO.class, id);
		return user;

	}

	@Override
	public UserVO register(UserVO user) {
		Session session = this.sessionFactory.getCurrentSession();

		user.setPassword(PasswordGenerator.encrypt(user.getPassword()));
		session.save(user);
		return user;
	}

	@Override
	public void updateUser(UserVO user) {

		Session session = this.sessionFactory.getCurrentSession();
		session.update(user);

	}

	@Override
	public void managePassword(UserVO user) throws BusinessResourceException {


		UserVO usr=findByEmail(user.getEmail());

		if(usr!=null){

			String decrypt=PasswordGenerator.decrypt(usr.getPassword());
			usr.setPassword(decrypt);

			List<String> labels=new ArrayList<String>();
			labels.add(MailType.PASSWORD_KEY);
			labels.add(MailType.USERNAME_KEY);

			MailSender mailSender = new MailSender();
			mailSender.sendMailMessage(MailType.PASSWORD_TEMPLATE,
					MailType.PASSWORD_TEMPLATE_TITLE,
					MailSender.replace(usr,labels,null,true),
					user.getEmail(), user.getUsername());
		}
	}

	@Override
	public boolean deleteUser(Long id) {

		UserVO user=updateDelete(id);

		return  (user!=null) ? user.isCancelled() : false;

	}


	@Override
	public boolean sendMail(MailRequest mr) {

		UserVO user=findByEmail(mr.getFrom());

		if(user!=null){
			MailSender mailSender = new MailSender();
			List<String> labels=new ArrayList<String>();

			labels.add(MailType.BODY_KEY);

			mailSender.sendMailMessage(MailType.SEND_MAIL_TEMPLATE,
					mr.getSubject(),MailSender.replace(user,labels,mr.getBody(),false),mr.getTo(), user.getUsername());


		}


		return  (user!=null) ? user.isCancelled() : false;

	}

	@Override
	public UserVO save(UserVO user) throws BusinessResourceException {
		Session session = this.sessionFactory.getCurrentSession();
		if(user!=null){
			session.save(user);
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

	private UserVO findByEmail(String email) throws BusinessResourceException{

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query=session.getNamedQuery(UserVO.EMAIL);
		query.setParameter("email", email);


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
			session.save(user);
			//return (UserVO) session.get(UserVO.class, id);
		}
		return null;
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
