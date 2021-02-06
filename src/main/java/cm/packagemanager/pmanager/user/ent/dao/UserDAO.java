package cm.packagemanager.pmanager.user.ent.dao;


import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.users.RegisterDTO;
import cm.packagemanager.pmanager.ws.requests.users.UpdateUserDTO;

import java.util.List;
import java.util.Optional;

public interface UserDAO {


	Optional<UserVO> findByUsername(String username) throws BusinessResourceException, UserException;

	UserVO findByOnlyUsername(String username, boolean isRegistration) throws BusinessResourceException;


	UserVO findByToken(String token) throws BusinessResourceException;

	UserVO findById(Long userId) throws UserException;


	UserVO login(String username) throws UserException;


	UserVO login(String username, String password) throws UserException;


	public List<UserVO> getAllUsers()  throws BusinessResourceException;

	public List<UserVO> getAllUsers(int page, int size)  throws BusinessResourceException;

	//public UserVO getUser(int id)  throws BusinessResourceException;

	public UserVO getUser(Long id) throws BusinessResourceException;

	public UserVO register(RegisterDTO register) throws BusinessResourceException, UserException;

	public void updateUser(UserVO user)  throws BusinessResourceException;

	public UserVO updateUser(UpdateUserDTO user) throws BusinessResourceException, UserException;

	public boolean deleteUser(Long id)  throws UserException;

	public UserVO save(UserVO user)  throws BusinessResourceException;

	public UserVO update(UserVO user)  throws BusinessResourceException;


	public UserVO findByEmail(String email)  throws BusinessResourceException;


	public UserVO findByFacebookId(String facebookId)  throws BusinessResourceException;


	public UserVO findByGoogleId(String googleId)  throws BusinessResourceException;


	public boolean setRole(UserVO user, RoleEnum roleId)  throws BusinessResourceException;
	public boolean setRole(String email, RoleEnum roleId)  throws BusinessResourceException;

	public boolean deleteUser(UserVO user) throws BusinessResourceException, UserException;




}
