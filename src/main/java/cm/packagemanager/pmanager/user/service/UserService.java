package cm.packagemanager.pmanager.user.service;

import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.users.LoginDTO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.users.RegisterDTO;
import cm.packagemanager.pmanager.ws.requests.users.RoleToUserDTO;
import cm.packagemanager.pmanager.ws.requests.users.UpdateUserDTO;
import com.sendgrid.Response;


import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService{

	public UserVO login(LoginDTO lr ) throws UserException;

	public UserVO update(UserVO user) throws UserException ;

	public boolean delete(UserVO user) throws UserException ;

	public void remove(UserVO user) throws UserException ;

	public List<UserVO> getAllUsers(int page, int size) throws Exception ;

	public List<UserVO> getAllUsers() throws Exception;

	public UserVO getUser(Long id) throws Exception;

	public UserVO register(RegisterDTO register) throws UserException;

	public UserVO updateUser(UpdateUserDTO userDTO) throws UserException;

	public Response managePassword(String email) throws UserException;

	public Response sendMail(MailDTO mr, boolean active) throws Exception;

	public boolean deleteUser(Long id)throws UserException;

	public UserVO findByEmail(String email)throws UserException;

	public boolean setRoleToUser(RoleToUserDTO roleToUser)  throws UserException;

	public UserVO findByUsername(String username, boolean isReg) throws UserException;

	public UserVO findByToken(String token) throws UserException;

	public Response buildAndSendMail(HttpServletRequest request , UserVO user) throws UserException;

	public boolean checkLogin(LoginDTO lr ) throws BusinessResourceException, UserException;

}
