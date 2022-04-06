package cm.packagemanager.pmanager.user.ent.dao;


import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.communication.ent.vo.CommunicationVO;
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

    int count(Object o,Long id, PageBy pageBy) throws BusinessResourceException;

    UserVO login(String username, String password) throws Exception;

    void subscribe(SubscribeDTO subscribe) throws UserException;

    void unsubscribe(SubscribeDTO subscribe) throws UserException;

    List<UserVO> subscriptions(Long userId) throws UserException;

    List<UserVO> subscribers(Long userId) throws UserException;


    List<UserVO> getAllUsers() throws Exception;

    List<UserVO> getAllUsersToConfirm() throws Exception;

    List<UserVO> getAllUsers(PageBy pageBy) throws Exception;

    //public UserVO getUser(int id)  throws BusinessResourceException;

    UserVO getUser(Long id) throws UserException;

    UserVO register(RegisterDTO register) throws BusinessResourceException, UserException;

    void updateUser(UserVO user) throws BusinessResourceException;

    UserVO updateUser(UpdateUserDTO user) throws Exception;

    boolean deleteUser(Long id) throws UserException;


    void removeUser(UserVO user) throws BusinessResourceException;


    UserVO findByEmail(String email) throws Exception;


    UserVO findByFacebookId(String facebookId) throws Exception;


    UserVO findByGoogleId(String googleId) throws Exception;


    boolean setRole(UserVO user, RoleEnum roleId) throws Exception;

    boolean setRole(String email, RoleEnum roleId) throws Exception;

    boolean deleteUser(UserVO user) throws BusinessResourceException, UserException;

    boolean checkLogin(LoginDTO lr) throws Exception;

    UserVO updateImage(Long userId, MultipartFile multipartFile) throws IOException;

    UserVO manageNotification(Long userId, boolean enableNotification) throws UserException;

    boolean editPassword(Long userId, String oldPassword, String newPassword) throws UserException;

    List<CommunicationVO> communications(Long userId) throws Exception;


}
