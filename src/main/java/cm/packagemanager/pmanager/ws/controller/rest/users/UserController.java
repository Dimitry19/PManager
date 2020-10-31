package cm.packagemanager.pmanager.ws.controller.rest.users;

import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.user.service.UserService;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.users.LoginDTO;
import cm.packagemanager.pmanager.ws.requests.users.PasswordDTO;
import cm.packagemanager.pmanager.ws.requests.users.RegisterDTO;
import cm.packagemanager.pmanager.ws.requests.users.RoleToUserDTO;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.jvm.hotspot.debugger.AddressException;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

import static cm.packagemanager.pmanager.ws.controller.rest.CommonController.USER_WS;


@RestController
@RequestMapping(USER_WS)
public class UserController extends CommonController {

	protected final Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	protected  UserService userService;

	@PostMapping(value = USER_WS_REGISTRATION)
	public  Response register(HttpServletRequest request ,HttpServletResponse response,@RequestBody @Valid RegisterDTO register) throws Exception{

		logger.info("register request in");
		response.setHeader("Access-Control-Allow-Origin", "*");

		Response pmResponse = new Response();

		try{

			if(register!=null){

				UserVO usr=userService.register(register);

				if(usr!=null && StringUtils.isNotEmpty(usr.getError())){

					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(usr.getError());

					return pmResponse;
				}

				if(usr==null){

					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USER_REGISTER_LABEL);
					response.setStatus(400);

					return pmResponse;
				}

				boolean sent=userService.buildAndSendMail(request,usr);

				if(sent){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_LABEL);
					response.setStatus(200);

					return pmResponse;
				}else{
					//userService.delete(usr);
				}

			}
		}catch (Exception e){
			logger.error("Errore eseguendo register: ", e);
			response.setStatus(400);
		}
		return null;
	}


	@RequestMapping(value=USER_WS_CONFIRMATION, method = RequestMethod.GET, headers =WSConstants.HEADER_ACCEPT)
	public Response showConfirmationPage(HttpServletResponse response, HttpServletRequest request, @RequestParam("token") String token) throws Exception{


		logger.info("confirm request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

	try{
		UserVO user = userService.findByToken(token);

		if (user == null) {
			pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			pmResponse.setRetDescription(WebServiceResponseCode.ERROR_INVALID_TOKEN_REGISTER_LABEL);
		} else {

			if(user.getActive()==1){
				pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USED_TOKEN_REGISTER_LABEL);
				return pmResponse;
			}

			user.setActive(1);
			if(userService.update(user)!=null){
				pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_ACTIVE_LABEL);
			}

		}

	}catch (Exception e){
		logger.error("Errore eseguendo confirm: ", e);
		response.setStatus(400);
		response.getWriter().write(e.getMessage());
	}

		return pmResponse;
	}

	@RequestMapping(value = USER_WS_LOGIN, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody
	UserVO login(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginDTO login)	throws Exception{
		logger.info("login request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserVO user = null;

		try
		{
			logger.info("login request out");
			if (login!=null){
				 user=userService.login(login);

				if(user!=null){
					user.setRetCode(WebServiceResponseCode.OK_CODE);
					user.setRetDescription(WebServiceResponseCode.LOGIN_OK_LABEL);
				}else{
					user=new UserVO();
					user.setRetCode(WebServiceResponseCode.NOK_CODE);
					user.setRetDescription(WebServiceResponseCode.ERROR_LOGIN_LABEL);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Errore eseguendo login: ", e);
			response.setStatus(400);
			response.getWriter().write(e.getMessage());
		}

		return  user;
	}


	@RequestMapping(value = USER_WS_USER_ID, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT,produces = MediaType.APPLICATION_JSON)
	public UserVO getUser(@PathVariable("id") Long id) {
		return userService.getUser(id);
	}



	@RequestMapping(value = USER_WS_UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	String  update(HttpServletResponse response, HttpServletRequest request, @RequestBody RegisterDTO register){

		if(register!=null){

			UserVO user = new UserVO();
			user.setEmail(register.getEmail());
			user.setUsername(register.getUsername());
			user.setPassword(register.getPassword());


			userService.updateUser(user);
		}


		return redirect;
	}


	@RequestMapping(value = USER_WS_PASSWORD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	Response  password(HttpServletResponse response, HttpServletRequest request, @RequestBody PasswordDTO password){


		logger.info("password request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();
		if(password!=null){

			UserVO user = new UserVO();
			user.setEmail(password.getEmail());

			if(userService.managePassword(user)){
				pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.RETRIVEVE_PASSWORD_LABEL+password.getEmail() +">>");
			}else{
				pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.ERROR_RETRIVEVE_PASSWORD_LABEL);
			}
		}


		return pmResponse;
	}


	//@RequestMapping(value = "/update/{id}", method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	@GetMapping(USER_WS_UPDATE_ID)
	public ResponseEntity<String> update(@PathVariable Long id) {
		
		  return new ResponseEntity<String>("Réponse du serveur: "+HttpStatus.OK.name(), HttpStatus.OK);
	}


	@PostMapping(value = USER_WS_ROLE)
	public ResponseEntity<UserVO> setRole(@RequestBody @Valid RoleToUserDTO roleToUser) {

		  if (userService.setRoleToUser(roleToUser)){
		  	  userService.findByEmail(roleToUser.getEmail(), true);
			  return new ResponseEntity<UserVO>(userService.findByEmail(roleToUser.getEmail(),true), HttpStatus.FOUND);
		  }else{
			  return new ResponseEntity<UserVO>(userService.findByEmail(roleToUser.getEmail(),true), HttpStatus.NOT_FOUND);
		  }

	}


	@GetMapping(USER_WS_DELETE_USER)
	public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long userId) throws Exception{
		logger.info("delete request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{

			logger.info("delete request out");
			if (userId!=null){
				if(userService.deleteUser(userId)){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.CANCELLED_USER_LABEL);
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_DELETE_USER_CODE_LABEL);
				}

			}
		}
		catch (Exception e)
		{
			logger.error("Errore eseguendo login: ", e);
			response.setStatus(400);
			response.getWriter().write(e.getMessage());
		}

		return pmResponse;
	}

	@RequestMapping(value = USER_WS_MAIL)
	public Response sendEmail(HttpServletResponse response, HttpServletRequest request, @RequestBody MailDTO mail) throws AddressException, MessagingException, IOException {


		logger.info("mail request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{
			if (mail!=null){
				if(userService.sendMail(mail,true)){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.MAIL_SENT_LABEL);
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_MAIL_SENT_LABEL);
				}

			}
		}
		catch (Exception e)
		{
			logger.error("Errore eseguendo send mail: ", e);
			response.setStatus(400);
			response.getWriter().write(e.getMessage());
		}

		return pmResponse;
	}



	// Retrieve All Users
	@GetMapping(USER_WS_USERS)
	public ResponseEntity<List<UserVO>> findAll(
			@Valid @Positive(message = "Page number should be a positive number") @RequestParam(required = false, defaultValue = "1") int page,
			@Valid @Positive(message = "Page size should be a positive number") @RequestParam(required = false, defaultValue = "20") int size) {

		HttpHeaders headers = new HttpHeaders();
		List<UserVO> users = userService.getAllUsers(page,size);
		headers.add("X-Users-Total", Long.toString(users.size()));
		return new ResponseEntity<List<UserVO>>(users, headers, HttpStatus.OK);
	}

	@RequestMapping(USER_WS_USERS_PAGE_NO)
	@ResponseBody
	public List<UserVO> users(@PathVariable int pageno,@PageableDefault(value=10, page=0) SpringDataWebProperties.Pageable pageable) throws ServletException {

		return userService.getAllUsers(pageno,size);

	}
}