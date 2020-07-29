package cm.packagemanager.pmanager.user.ent.dao;


import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.MailDTO;
import cm.packagemanager.pmanager.ws.requests.RegisterDTO;

import java.util.List;
import java.util.Optional;

public interface UserDAO{


	Optional<UserVO> findByUsername(String username) throws BusinessResourceException, UserException;

	UserVO findByOnlyUsername(String username) throws BusinessResourceException;


	UserVO findByToken(String token) throws BusinessResourceException;


	UserVO login(String username) throws UserException;


	UserVO login(String username, String password) throws UserException;


	public List<UserVO> getAllUsers()  throws BusinessResourceException;

	public List<UserVO> getAllUsers(int page, int size)  throws BusinessResourceException;

	//public UserVO getUser(int id)  throws BusinessResourceException;

	public UserVO getUser(Long id) throws BusinessResourceException;

	public UserVO register(RegisterDTO register)  throws BusinessResourceException;

	public void updateUser(UserVO user)  throws BusinessResourceException;
	
	
	public boolean managePassword(UserVO user)  throws BusinessResourceException;

	public boolean deleteUser(Long id)  throws BusinessResourceException;

	public UserVO save(UserVO user)  throws BusinessResourceException;
	public UserVO update(UserVO user)  throws BusinessResourceException;


	public UserVO findByEmail(String email, boolean active)  throws BusinessResourceException;


	public UserVO findByFacebookId(String facebookId)  throws BusinessResourceException;


	public UserVO findByGoogleId(String googleId)  throws BusinessResourceException;


	public boolean sendMail(MailDTO mr, boolean active)  throws BusinessResourceException;


	public boolean setRole(String username, RoleEnum roleId)  throws BusinessResourceException;




}
