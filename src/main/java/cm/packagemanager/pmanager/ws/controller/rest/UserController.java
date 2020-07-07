package cm.packagemanager.pmanager.ws.controller.rest;

import cm.packagemanager.pmanager.common.exception.ResponseException;
import cm.packagemanager.pmanager.common.mail.MailSender;
import cm.packagemanager.pmanager.common.mail.MailType;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.security.PasswordGenerator;
import cm.packagemanager.pmanager.user.service.UserService;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.LoginRequest;
import cm.packagemanager.pmanager.ws.requests.MailRequest;
import cm.packagemanager.pmanager.ws.requests.PasswordRequest;
import cm.packagemanager.pmanager.ws.requests.RegisterRequest;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.jvm.hotspot.debugger.AddressException;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
/*L'annotation @CrossOrigin(origins = "http://localhost:8080", maxAge = 3600) permet de favoriser une communication distante entre le client et le serveur,
		c'est-à-dire lorsque le client et le serveur sont déployés dans deux serveurs distincts, ce qui permet d'éviter des problèmes réseaux.*/
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/ws/user/*")
public class UserController {

	protected final Log logger = LogFactory.getLog(UserController.class);


	@Autowired
	private ServletContext servletContext;

	@ExceptionHandler(
			{ ResponseException.class, Exception.class })

	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(Exception e)
	{
		if (e instanceof ResponseException)
		{
			//logger.warn(e.getMessage());
			return e.getMessage();
		}
		else
		{
			//logger.error("Error", e);
		}
		return "internal server error";
	}


	@Autowired
	protected  UserService userService;

	@Value("${ws.redirect.user}")
	private String redirect;

	/*@PostMapping(value = "/register")
	@Transactional
	public ResponseEntity<UserVO> register(@RequestBody RegisterRequest registerRequest) {
		
		UserVO userSaved = userService.saveOrUpdateUser(user);		
 		return new ResponseEntity<UserVO>(userSaved, HttpStatus.CREATED);
 	}*/

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	Response  register(HttpServletResponse response, HttpServletRequest request,@RequestBody RegisterDTO register) throws Exception{

		logger.info("register request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try{
			if(register!=null){

				UserVO user = userService.findByEmail(register.getEmail());
				if(user!=null){
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_EMAIL_REGISTER_LABEL);
					return pmResponse;
				}

				user=userService.findByUsername(register.getUserName());

				if(user!=null){
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USERNAME_REGISTER_LABEL);

					return pmResponse;
				}

				user=new UserVO();
				user.setEmail(register.getEmail());
				user.setUsername(register.getUserName());
				user.setPassword(PasswordGenerator.encrypt(register.getPassword()));
				user.setFirstName(register.getFirstName());
				user.setLastName(register.getLastName());
				user.setPhone(register.getPhone());
				user.setActive(0);
				user.setConfirmationToken(UUID.randomUUID().toString());
				UserVO usr=userService.register(user);


				if(usr==null){

					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USER_REGISTER_LABEL);

					return pmResponse;
				}

				boolean sent=userService.buildAndSendMail(request,usr);

				if(sent){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_LABEL);

					return pmResponse;
				}

			}
		}catch (Exception e){
			logger.error("Errore eseguendo register: ", e);
			response.setStatus(400);
			response.getWriter().write(e.getMessage());
		}


		return null;
	}


	@RequestMapping(value="/confirm", method = RequestMethod.GET, headers =WSConstants.HEADER_ACCEPT)
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

	@RequestMapping(value = "/ulogin", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody
	Response login(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginDTO login)
			throws Exception
	{
		logger.info("login request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{
			logger.info("login request out");
			if (login!=null){
				UserVO user=userService.login(login);
				if(user!=null){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.LOGIN_OK_LABEL);
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_LOGIN_LABEL);
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


	/*  TODO cette methode est encore à tester
	@GetMapping(value = "/users")
	public ResponseEntity<Collection<User>> getAllUsers() {
		Collection<UserVO> users = userService.getAllUsers();
		return new ResponseEntity<Collection<UserVO>>(users, HttpStatus.FOUND);
	}
	
	*/
	
	@RequestMapping(value = "/users", method = RequestMethod.GET,headers = WSConstants.HEADER_ACCEPT)
	@ResponseBody
	String   allUsers(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginDTO login)throws Exception
	{


		logger.info("all request in");
		response.setHeader("Access-Control-Allow-Origin", "*");

		try
		{
			logger.info("login request out");
			List<UserVO>  users=userService.getAllUsers();

		}
		catch (Exception e)
		{
			logger.error("Errore eseguendo login: ", e);
			response.setStatus(400);
			response.getWriter().write(e.getMessage());
		}

		return "" ;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public String goToHomePage() {
		return redirect;
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public UserVO getUser(@PathVariable("id") Long id) {
		return userService.getUser(id);
	}





	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	String  update(HttpServletResponse response, HttpServletRequest request, @RequestBody RegisterDTO register){

		if(register!=null){

			UserVO user = new UserVO();
			user.setEmail(register.getEmail());
			user.setUsername(register.getUserName());
			user.setPassword(register.getPassword());


			userService.updateUser(user);
		}


		return redirect;
	}


	@RequestMapping(value = "/password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	Response  password(HttpServletResponse response, HttpServletRequest request, @RequestBody PasswordDTO password){


		logger.info("password request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();
		if(passwordRequest!=null){

			UserVO user = new UserVO();
			user.setEmail(password.getEmail());

			if(userService.managePassword(user)){
				pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.RETRIVEVE_PASSWORD_LABEL+passwordRequest.getEmail() +">>");
			}else{
				pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.ERROR_RETRIVEVE_PASSWORD_LABEL);
			}
		}


		return pmResponse;
	}


	//@RequestMapping(value = "/update/{id}", method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	@GetMapping("/update/{id}")
	public ResponseEntity<String> update(@PathVariable Long id) {
		
		  return new ResponseEntity<String>("Réponse du serveur: "+HttpStatus.OK.name(), HttpStatus.OK);
	}

	//@RequestMapping(value = "/delete/user", method = RequestMethod.GET, headers =WSConstants.HEADER_ACCEPT)
	@GetMapping("/delete/user/{userId}")
	public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam Long userId) throws Exception{
		logger.info("delete request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{

			logger.info("delete request out");
			if (id!=null){
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

	@RequestMapping(value = "/mail")
	public Response sendEmail(HttpServletResponse response, HttpServletRequest request, @RequestBody MailDTO mail) throws AddressException, MessagingException, IOException {


		logger.info("mail request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{
			if (mail!=null){
				if(userService.sendMail(mail)){
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
}
