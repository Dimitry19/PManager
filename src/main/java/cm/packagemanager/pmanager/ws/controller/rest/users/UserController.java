package cm.packagemanager.pmanager.ws.controller.rest.users;

import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.UserException;

import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
import cm.packagemanager.pmanager.common.mail.MailSender;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.user.service.UserService;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.users.*;
import cm.packagemanager.pmanager.ws.responses.PaginateResponse;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

import static cm.packagemanager.pmanager.ws.controller.rest.CommonController.USER_WS;


@RestController
@RequestMapping(USER_WS)
@Api(value="user-service", description="User Operations")
public class UserController extends CommonController {

	protected final Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	protected UserService userService;

	@Autowired
	MailSender mailSender;

	@ApiOperation(value = "Register an user ",response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful registration",
					response = Response.class, responseContainer = "Object") })
	@PostMapping(value = USER_WS_REGISTRATION)
	public  Response register(HttpServletRequest request ,HttpServletResponse response,@RequestBody @Valid RegisterDTO register) throws ValidationException, IOException {

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

					return pmResponse;
				}

				com.sendgrid.Response sent = userService.buildAndSendMail(request,usr);

				if(mailSender.manageResponse(sent)){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_LABEL);
					response.setStatus(200);
					return pmResponse;
				}else{
					pmResponse.setRetCode(sent.getStatusCode());
					pmResponse.setRetDescription(sent.getBody());
					response.setStatus(sent.getStatusCode());
					userService.remove(usr);
				}

			}
		}catch (Exception e){
			logger.error("Errore eseguendo register: ", e);
			pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
			pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_LABEL);
			response.getWriter().write(e.getMessage());
		}
		return null;
	}


	@ApiOperation(value = "Confirmation user registration ",response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful registration",
					response = Response.class, responseContainer = "Object") })
	@RequestMapping(value=USER_WS_CONFIRMATION, method = RequestMethod.GET, headers =WSConstants.HEADER_ACCEPT)
	public Response showConfirmationPage(HttpServletResponse response, HttpServletRequest request, @RequestParam("token") String token) throws Exception, UserNotFoundException, ValidationException {


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

	@ApiOperation(value = " Login user ",response = UserVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful registration",
					response = UserVO.class, responseContainer = "Object") })
	@RequestMapping(value = USER_WS_LOGIN, method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody
	UserVO login(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginDTO login)	throws Exception{
		logger.info("login request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserVO user = null;

		try{
			createOpentracingSpan("UserController -login");

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
		catch (Exception e){
			logger.error("Errore eseguendo login: ", e);
			//response.setStatus(400);
			response.getWriter().write(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}
		return  user;
	}


	@ApiOperation(value = " Update user ",response = UserVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful update",
					response = UserVO.class, responseContainer = "Object") })
	@RequestMapping(value = USER_WS_UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	UserVO update(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid UpdateUserDTO userDTO) throws UserException {

		logger.info("update user request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
			try {
				createOpentracingSpan("UserController -update");

				if(userDTO!=null){
					UserVO user=userService.updateUser(userDTO);
					if (user==null){
						user= new UserVO();
						user.setRetCode(-1);
						user.setRetDescription(WebServiceResponseCode.ERROR_UPD_EMAIL_LABEL);
					}
					return  user;
				}
			} catch (UserException e) {
				logger.error("Erreur durant l'ajournement de l'utilisateur  " + userDTO.toString() +"{}",e);
				e.printStackTrace();
				throw  e;
			}finally {
				finishOpentracingSpan();
			}

		return null;
	}

	@ApiOperation(value = " Retrieve password",response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Password sent successfully",
					response = Response.class, responseContainer = "Object") })
	@RequestMapping(value = USER_WS_PASSWORD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	Response  password(HttpServletResponse response, HttpServletRequest request, @RequestBody PasswordDTO password) throws UserException {


		logger.info("password request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try{
			createOpentracingSpan("UserController -password");
			if(password!=null){

				if(mailSender.manageResponse(userService.managePassword(password.getEmail()))){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.RETRIVEVE_PASSWORD_LABEL+" "+password.getEmail());
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_RETRIVEVE_PASSWORD_LABEL);
				}
			}
			return pmResponse;
		}catch (UserException e){
			logger.error("Erreur durant la recuperation du mot de passe de l'utilisateur  " + password.toString() +"{}",e);
			e.printStackTrace();
			throw  e;
		}finally {
			finishOpentracingSpan();
		}
	}

	@ApiOperation(value = " Update user role ",response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Update role successfully",
					response = ResponseEntity.class, responseContainer = "Object") })
	@PostMapping(value = USER_WS_ROLE)
	public ResponseEntity<UserVO> setRole(@RequestBody @Valid RoleToUserDTO roleToUser) throws UserException {

		logger.info("set role request in");

		try {
			createOpentracingSpan("UserController -setRole");
			if (userService.setRoleToUser(roleToUser)){
				userService.findByEmail(roleToUser.getEmail());
				return new ResponseEntity<UserVO>(userService.findByEmail(roleToUser.getEmail()), HttpStatus.FOUND);
			}else{
				return new ResponseEntity<UserVO>(userService.findByEmail(roleToUser.getEmail()), HttpStatus.NOT_FOUND);
			}
		}catch (UserException e){
			logger.error("Erreur durant l'ajournement  du role de l'utilisateur  " + roleToUser.toString() +"{}",e);
			e.printStackTrace();
			throw  e;
		}finally {
			finishOpentracingSpan();
		}
	}


	@ApiOperation(value = "Delete user ",response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful deleted",
					response = Response.class, responseContainer = "Object") })
	@RequestMapping(value =USER_WS_DELETE_USER,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") Long id) throws UserException{
		logger.info("delete request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try{
			createOpentracingSpan("UserController -delete");
			if (id!=null){
				if(userService.deleteUser(id)){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.CANCELLED_USER_LABEL);
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_DELETE_USER_CODE_LABEL);
				}
			}
		}
		catch (Exception e){
			//response.getWriter().write(e.getMessage());
			pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			pmResponse.setRetDescription(e.getMessage());
			logger.error("Erreur durant la delete de l'utilisateur ayant id:" + id +"{}",e);
			e.printStackTrace();
			throw  e;
		}finally {
			finishOpentracingSpan();
		}

		return pmResponse;
	}

	@ApiOperation(value = " Retrieve user with an ID ",response = UserVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful user retrieving",
					response = UserVO.class, responseContainer = "Object") })
	@RequestMapping(value = USER_WS_USER_ID, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT,produces = MediaType.APPLICATION_JSON)
	public UserVO infosUser(HttpServletResponse response, HttpServletRequest request,@PathVariable("id") Long id) throws UserException,IOException {
		try{
			createOpentracingSpan("UserController -getUser");
			logger.info("retrieve user request in");
			response.setHeader("Access-Control-Allow-Origin", "*");
			UserVO user =userService.getUser(id);
			if (user!=null){
				return user;
			}
		}catch (UserException e){
			logger.error("Erreur durant l'execution de recuperation des infos de l'utilisateur: ", e);
			response.getWriter().write(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}
		return null;
	}

	@ApiOperation(value = " Send mail ",response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Mail sent",
					response = Response.class, responseContainer = "Object") })
	@RequestMapping(value = USER_WS_MAIL)
	public Response sendEmail(HttpServletResponse response, HttpServletRequest request, @RequestBody MailDTO mail) throws AddressException, MessagingException, IOException {

		logger.info("send mail request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{
			createOpentracingSpan("UserController -sendMail");
			if (mail!=null){
				if(mailSender.manageResponse(userService.sendMail(mail,true))){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.MAIL_SENT_LABEL);
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_MAIL_SENT_LABEL);
				}
			}
		}
		catch (Exception e) {
			logger.error("Errore eseguendo send mail: ", e);
			response.getWriter().write(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}

		return pmResponse;
	}



	@ApiOperation(value = " Retrieve users ",response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieving",
					response = ResponseEntity.class, responseContainer = "Object") })	@GetMapping(USER_WS_USERS)
	public ResponseEntity<PaginateResponse> users(HttpServletResponse response, HttpServletRequest request,
			@Valid @Positive(message = "la page doit etre nombre positif") @RequestParam(required = false, defaultValue = DEFAULT_PAGE) int page,
			@Valid @Positive(message = "Page size should be a positive number") @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {


		logger.info("get all users request in");
		HttpHeaders headers = new HttpHeaders();
		PageBy pageBy = new PageBy(page,size);
		PaginateResponse paginateResponse = new PaginateResponse();

		try{
			createOpentracingSpan("UserController -users");
			int count = userService.count(pageBy);
			if(count == 0){
				headers.add(HEADER_TOTAL, Long.toString(count));
			}else{
				List<UserVO> users = userService.getAllUsers(pageBy);
				paginateResponse.setCount(count);
				paginateResponse.setResults(users);
				headers.add(HEADER_TOTAL, Long.toString(users.size()));
			}
		}catch (Exception e){
			response.getWriter().write(e.getMessage());
			logger.info(" UserController -users:Exception occurred while fetching the response from the database.", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			finishOpentracingSpan();
		}
		return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
	}

	//@RequestMapping(USER_WS_USERS_PAGE_NO)
	@ResponseBody	public List<UserVO> users(@PathVariable int pageno,@PageableDefault(value=10, page=0) SpringDataWebProperties.Pageable pageable) throws Exception {

		PageBy pageBy = new PageBy(pageno,size);
		return userService.getAllUsers(pageBy);
	}
}
