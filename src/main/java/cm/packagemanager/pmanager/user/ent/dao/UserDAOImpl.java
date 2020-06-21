package cm.packagemanager.pmanager.user.ent.dao;

import cm.packagemanager.pmanager.PackageApplication;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.Filter;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public  class UserDAOImpl implements UserDAO{

	private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	HibernateTransactionManager tx;

	BCryptPasswordEncoder bCryptPasswordEncoder;

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
		session.save(user);
		return user;
	}

	@Override
	public void updateUser(UserVO user) {

		Session session = this.sessionFactory.getCurrentSession();
		session.update(user);

	}

	@Override
	public boolean deleteUser(Long id) {

		UserVO user=updateDelete(id);

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

	private UserVO updateDelete(Long id) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);//.setParameter("canc",false);

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

			Optional<UserVO> userFound = findByUsername(username);
			if(bCryptPasswordEncoder.matches(password, userFound.get().getPassword())) {
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
	public Optional<UserVO> findByUsername(String username) throws BusinessResourceException, UserException {


		Session session = this.sessionFactory.getCurrentSession();
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
		UserVO usr = this.login(user.getUsername());
		if (usr != null) {
			throw new UserException(" Username deja existant " + user.getUsername());
		}
		session.save(user);
	}

	@Override
	public UserVO login(String username) throws UserException {
		Session session = this.sessionFactory.getCurrentSession();
		return session.get(UserVO.class, username);
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
