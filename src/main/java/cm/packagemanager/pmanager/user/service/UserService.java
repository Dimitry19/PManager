package cm.packagemanager.pmanager.user.service;

import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.mail.MailSender;
import cm.packagemanager.pmanager.common.mail.MailType;
import cm.packagemanager.pmanager.security.PasswordGenerator;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.users.LoginDTO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.users.RegisterDTO;
import cm.packagemanager.pmanager.ws.requests.users.RoleToUserDTO;
import cm.packagemanager.pmanager.ws.requests.users.UpdateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static cm.packagemanager.pmanager.constant.FieldConstants.FACEBOOK_PROVIDER;
import static cm.packagemanager.pmanager.constant.FieldConstants.GOOGLE_PROVIDER;

/*
Le fait d’avoir des singletons a un impact en environnement multi-threadé
○ Les variables de classe sont partagées entre les threads
○ Les beans doivent donc être thread-safe (d'où le transactional)
*
* */
@Service("userService")
public class UserService{

	@Autowired
	UserDAO userDAO;

	@PostConstruct
	public void init() {
		System.out.println("User service starts...." );
	}

	@Transactional(readOnly = true,propagation = Propagation. REQUIRED)
	public UserVO login(LoginDTO lr ) throws UserException {


		UserVO user=null;
		if(lr.getUsername()!=null){
			user= userDAO.login(lr.getUsername(), lr.getPassword());
		}

		if(user== null){
			if(lr.getEmail()!=null){
				user=userDAO.findByEmail(lr.getEmail(),true);
			}

			if(lr.getProvider()!=null){

				if(lr.getProvider().equals(GOOGLE_PROVIDER)){
					user=userDAO.findByGoogleId(lr.getSocialId());
				}

				if(lr.getProvider().equals(FACEBOOK_PROVIDER)){
					user=userDAO.findByFacebookId(lr.getSocialId());
				}

			}
		}

		return user;

	}


	@Transactional
	public UserVO update(UserVO user) throws UserException {
		return userDAO.update(user);
	}


	@Transactional
	public boolean delete(UserVO user) throws UserException {
		return userDAO.deleteUser(user);
	}

	@Transactional(readOnly = true)
	public List<UserVO> getAllUsers(int page, int size) {
		return userDAO.getAllUsers(page, size);
	}


	@Transactional(readOnly = true)
	public List<UserVO> getAllUsers() {
		return userDAO.getAllUsers();
	}

	@Transactional(readOnly = true)
	public UserVO getUser(Long id) {
		return userDAO.getUser(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UserVO register(RegisterDTO register) {
		return userDAO.register(register);
	}


	public UserVO updateUser(UpdateUserDTO userDTO) {
		return userDAO.updateUser(userDTO);
	}


	public boolean managePassword(UserVO user) {
		UserVO usr=findByEmail(user.getEmail(),true);

		if(usr!=null){

			UserVO transientUser=usr;

			String decrypt=PasswordGenerator.decrypt(usr.getPassword());
			//transientUser.setPassword(decrypt);

			List<String> labels=new ArrayList<String>();
			List<String> emails=new ArrayList<String>();

			labels.add(MailType.PASSWORD_KEY);
			labels.add(MailType.USERNAME_KEY);

			emails.add(transientUser.getEmail());

			MailSender mailSender = new MailSender();
			return mailSender.sendMailMessage(MailType.PASSWORD_TEMPLATE,MailType.PASSWORD_TEMPLATE_TITLE,	MailSender.replace(transientUser,labels,null,decrypt),
					emails,null,null, null,transientUser.getUsername());
		}
		return false;
	}


	public boolean sendMail(MailDTO mr, boolean active) {

		UserVO user=findByEmail(mr.getFrom(),active);

		if(user!=null){
			MailSender mailSender = new MailSender();
			List<String> labels=new ArrayList<String>();

			labels.add(MailType.BODY_KEY);

			return mailSender.sendMailMessage(MailType.SEND_MAIL_TEMPLATE,
					mr.getSubject(),MailSender.replace(user,labels,mr.getBody(),null),mr.getTo(),mr.getCc(),mr.getBcc(), mr.getFrom(),user.getUsername());

		}


		return  false;

	}

	@Transactional
	public boolean deleteUser(Long id) {
		return userDAO.deleteUser(id);
	}

	@Transactional(readOnly = true)
	public UserVO findByEmail(String email, boolean active) {
		return userDAO.findByEmail(email,active);
	}


	@Transactional
	public boolean setRoleToUser(RoleToUserDTO roleToUser) {

		return userDAO.setRole(roleToUser.getEmail(),roleToUser.getRole());
	}

	@Transactional(readOnly = true)
	public UserVO findByUsername(String username) {
		return userDAO.findByOnlyUsername(username);
	}

	@Transactional(readOnly = true)
	public UserVO findByToken(String token) {
		return userDAO.findByToken(token);
	}

	/*
	public Page<UserVO> findAll(Pageable pageable) {
		return userDAO.findAll(pageable);
	}*/

	public boolean buildAndSendMail(HttpServletRequest request , UserVO user){

		String appUrl = request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort() +request.getContextPath()+"/ws/user";

		StringBuilder sblink= new StringBuilder("<a href=");
		sblink.append(appUrl);
		sblink.append("/confirm?token=");
		sblink.append(user.getConfirmationToken());
		sblink.append(">Confirmation</a>");;

		String body=MailType.CONFIRM_TEMPLATE_BODY +sblink.toString();
		List<String> labels=new ArrayList<String>();
		List<String> emails=new ArrayList<String>();

		labels.add(MailType.BODY_KEY);

		emails.add(user.getEmail());

		MailSender mailSender = new MailSender();
		return mailSender.sendMailMessage(MailType.CONFIRM_TEMPLATE,MailType.CONFIRM_TEMPLATE_TITLE,	MailSender.replace(user,labels,body,null),
				emails,null,null, null,user.getUsername());
	}
}
