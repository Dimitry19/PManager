package cm.packagemanager.pmanager.ws.controller.rest;

import cm.packagemanager.pmanager.common.exception.ResponseException;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.user.service.UserService;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.controller.CommonControler;
import cm.packagemanager.pmanager.ws.requests.LoginRequest;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/ws/user/*")
public class UserController extends CommonControler {

	protected final Log logger = LogFactory.getLog(UserController.class);

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


	@RequestMapping(value = "/userLogin", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody
	Response login(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginRequest loginRequest)
			throws Exception
	{
		logger.info("login request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{
			pmResponse.setRetCode(0);
			pmResponse.setRetDescription("OK");
			logger.info("login request out");
			if (loginRequest!=null){
				userService.login(loginRequest);
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


	@RequestMapping(value = "/all", method = RequestMethod.GET,headers = WSConstants.HEADER_ACCEPT)
	@ResponseBody
	String   all(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginRequest loginRequest)throws Exception
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

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	String  add(HttpServletResponse response, HttpServletRequest request,
	            @RequestBody RegisterRequest registerRequest) throws Exception{

		try{
			if(registerRequest!=null){

				UserVO user = new UserVO();
				user.setEmail(registerRequest.getEmail());
				user.setUsername(registerRequest.getUserName());
				user.setPassword(registerRequest.getPassword());
				user.setFirstName(registerRequest.getFirstName());
				user.setLastName(registerRequest.getLastName());
				user.setPhone(registerRequest.getPhone());
				user.setActive(1);


				userService.register(user);
			}
		}catch (Exception e){
			logger.error("Errore eseguendo login: ", e);
			response.setStatus(400);
			response.getWriter().write(e.getMessage());
		}


		return redirect;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	String  update(HttpServletResponse response, HttpServletRequest request, @RequestBody RegisterRequest registerRequest){

		if(registerRequest!=null){

			UserVO user = new UserVO();
			user.setEmail(registerRequest.getEmail());
			user.setUsername(registerRequest.getUserName());
			user.setPassword(registerRequest.getPassword());


			userService.updateUser(user);
		}


		return redirect;
	}


	@RequestMapping(value = "/password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	String  password(HttpServletResponse response, HttpServletRequest request, @RequestBody RegisterRequest registerRequest){

		if(registerRequest!=null){

			UserVO user = new UserVO();
			user.setEmail(registerRequest.getEmail());
			userService.updateUser(user);
		}


		return redirect;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public String update(@PathVariable("id") Long id,Model model) {
		model.addAttribute("customer", this.userService.getUser(id));
		model.addAttribute("listOfCustomers", this.userService.getAllUsers());
		return "customerDetails";
	}

	@RequestMapping(value = "/delete/user", method = RequestMethod.GET, headers =WSConstants.HEADER_ACCEPT)
	public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam("userId")  Long id) throws Exception{
		/*http://localhost:8080/pmanager/ws/user/delete/user?userId=2*/
		logger.info("delete request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{

			logger.info("delete request out");
			if (id!=null){
				if(userService.deleteUser(id)){
					pmResponse.setRetCode(WebServiceResponseCode.DELETE_USER_CODE_OK);
					pmResponse.setRetDescription("OK");
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.ERROR_DELETE_USER_CODE);
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
}
