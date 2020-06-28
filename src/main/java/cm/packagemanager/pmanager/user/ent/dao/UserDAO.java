package cm.packagemanager.pmanager.user.ent.dao;


import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.MailRequest;

import java.util.List;
import java.util.Optional;


public interface UserDAO {


	Optional<UserVO> findByUsername(String username) throws BusinessResourceException, UserException;

	UserVO findByOnlyUsername(String username) throws BusinessResourceException;


	UserVO findByToken(String token) throws BusinessResourceException;


	UserVO login(String username) throws UserException;


	UserVO login(String username, String password) throws UserException;


	public List<UserVO> getAllUsers()  throws BusinessResourceException;

	//public UserVO getUser(int id)  throws BusinessResourceException;

	public UserVO getUser(Long id) throws BusinessResourceException;

	public UserVO register(UserVO user)  throws BusinessResourceException;

	public void updateUser(UserVO user)  throws BusinessResourceException;
	
	
	public boolean managePassword(UserVO user)  throws BusinessResourceException;

	public boolean deleteUser(Long id)  throws BusinessResourceException;

	public UserVO save(UserVO user)  throws BusinessResourceException;
	public UserVO update(UserVO user)  throws BusinessResourceException;


	public UserVO findByEmail(String email)  throws BusinessResourceException;


	public UserVO findByFacebookId(String facebookId)  throws BusinessResourceException;


	public UserVO findByGoogleId(String googleId)  throws BusinessResourceException;


	public boolean sendMail(MailRequest mr)  throws BusinessResourceException;




}
