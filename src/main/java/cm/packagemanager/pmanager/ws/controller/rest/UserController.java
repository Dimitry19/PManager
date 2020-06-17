package cm.packagemanager.pmanager.ws.controller.rest;

import cm.packagemanager.pmanager.user.ent.bo.UserBO;
import cm.packagemanager.pmanager.user.ent.rep.UserRP;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/* https://bnguimgo.developpez.com/tutoriels/spring/services-rest-avec-springboot-et-spring-resttemplate/?page=premiere-partie-le-serveur*/

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/serv/*")
//@Validated
public class UserController {

	protected final Log logger = LogFactory.getLog(UserController.class);



	@Autowired
	UserBO userBO;



	/*@RequestMapping(value = "/loginHome", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody Response login(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginRequest loginRequest)
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

		}
		catch (Exception e)
		{
			logger.error("Errore eseguendo login: ", e);
		}

		return pmResponse;
	}

	@RequestMapping(method=RequestMethod.GET, value = "/user/{userId}", produces = MediaType.APPLICATION_JSON)
	//@RequestMapping(value = "/loginHome", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public UserVO findUserById(@PathVariable Long userId){

		return userBO.findUserById(userId);
	}
*/


	@RequestMapping(value = "/useris",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public List<UserVO> getAllUserws() {
		return null ;//userRepositoty.findAll();
	}


	@RequestMapping(method=RequestMethod.GET,value = "/users", produces = { MediaType.APPLICATION_JSON_VALUE })
	//@GetMapping(value = "/users")
	public ResponseEntity<Collection<UserVO>> getAllUsers() {
		logger.info("into get all user request");
		Collection<UserVO> users = userBO.getAllUsers();
		return new ResponseEntity<Collection<UserVO>>(users, HttpStatus.FOUND);
	}






}
