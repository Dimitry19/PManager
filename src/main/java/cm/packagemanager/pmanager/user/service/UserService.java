package cm.packagemanager.pmanager.user.service;

import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.LoginRequest;
import cm.packagemanager.pmanager.ws.requests.MailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
public class UserService{

	@Autowired
	UserDAO userDAO;


	@Transactional
	public UserVO login(LoginRequest lr ) throws UserException {
		return userDAO.login(lr.getUsername(), lr.getPassword());
	}


	@Transactional
	public List<UserVO> getAllUsers() {
		return userDAO.getAllUsers();
	}

	@Transactional
	public UserVO getUser(Long id) {
		return userDAO.getUser(id);
	}

	@Transactional
	public void register(UserVO user) {
		userDAO.register(user);
	}

	@Transactional
	public void updateUser(UserVO user) {
		userDAO.updateUser(user);
	}

	@Transactional
	public void managePassword(UserVO user) {
		userDAO.managePassword(user);
	}

	@Transactional
	public boolean sendMail(MailRequest mr) {
		return userDAO.sendMail(mr);
	}

	@Transactional
	public boolean deleteUser(Long id) {
		return userDAO.deleteUser(id);
	}
}
