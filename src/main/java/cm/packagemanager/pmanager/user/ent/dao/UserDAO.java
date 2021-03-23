package cm.packagemanager.pmanager.user.ent.dao;


import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.communication.ent.vo.CommunicationVO;
import cm.packagemanager.pmanager.rating.ent.vo.RatingCountVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.users.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserDAO extends CommonDAO {


	UserVO findByUsername(String username) throws Exception;

	UserVO findByOnlyUsername(String username, boolean isRegistration) throws Exception;

	List<UserVO> find(UserSeachDTO userSeachDTO, PageBy pageBy) throws BusinessResourceException;

	UserVO findByToken(String token) throws Exception;

	UserVO findById(Long userId) throws UserException;


	UserVO login(String username) throws UserException;

	int count(PageBy pageBy) throws BusinessResourceException;

	UserVO login(String username, String password) throws UserException;

	public  void  subscribe(SubscribeDTO subscribe) throws UserException;

	public  void  unsubscribe(SubscribeDTO subscribe) throws UserException;

	public  List<UserVO>  subscriptions(Long  userId) throws UserException;

	public  List<UserVO>  subscribers(Long  userId) throws UserException;


	public List<UserVO> getAllUsers() throws Exception;

	public List<UserVO> getAllUsersToConfirm()  throws BusinessResourceException;

	public List<UserVO> getAllUsers(PageBy pageBy) throws Exception;

	//public UserVO getUser(int id)  throws BusinessResourceException;

	public UserVO getUser(Long id) throws UserException;

	public UserVO register(RegisterDTO register) throws BusinessResourceException, UserException;

	public void updateUser(UserVO user)  throws BusinessResourceException;

	public UserVO updateUser(UpdateUserDTO user) throws Exception;

	public boolean deleteUser(Long id)  throws UserException;

	public UserVO save(UserVO user)  throws BusinessResourceException;

	public void remove(UserVO user)  throws BusinessResourceException;


	public UserVO findByEmail(String email)  throws BusinessResourceException;


	public UserVO findByFacebookId(String facebookId)  throws BusinessResourceException;


	public UserVO findByGoogleId(String googleId)  throws BusinessResourceException;


	public boolean setRole(UserVO user, RoleEnum roleId) throws Exception;
	public boolean setRole(String email, RoleEnum roleId) throws Exception;

	public boolean deleteUser(UserVO user) throws BusinessResourceException, UserException;

	public boolean checkLogin(LoginDTO lr ) throws Exception;

	List<RatingCountVO> findRatingCounts(UserVO user);


	UserVO updateImage(Long userId, MultipartFile multipartFile) throws IOException;

	boolean manageNotification(Long userId, boolean enableNotification) throws UserException;

	boolean editPassword(Long userId, String oldPassword, String newPassword) throws UserException;
	List<CommunicationVO> communications(Long userId) throws Exception;
	List notifications(Long userId) throws Exception;
	List messages(Long userId) throws Exception;
}
