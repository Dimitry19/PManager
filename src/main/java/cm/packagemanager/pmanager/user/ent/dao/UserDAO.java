package cm.packagemanager.pmanager.user.ent.dao;


import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.springframework.security.core.userdetails.User;



import java.util.List;
import java.util.Optional;


public interface UserDAO {


	Optional<UserVO> findByUsername(String username) throws BusinessResourceException, UserException;

	void registerUser(UserVO user) throws Exception;

	UserVO login(String username) throws UserException;
	UserVO login(String username, String password) throws UserException;

	User findUserWithName(String username);


	public List<UserVO> getAllUsers()  throws BusinessResourceException;

	//public UserVO getUser(int id)  throws BusinessResourceException;

	public UserVO getUser(Long id) throws BusinessResourceException;

	public UserVO register(UserVO user)  throws BusinessResourceException;

	public void updateUser(UserVO user)  throws BusinessResourceException;

	public void deleteUser(Long id)  throws BusinessResourceException;

	public UserVO save(UserVO user)  throws BusinessResourceException;

}
