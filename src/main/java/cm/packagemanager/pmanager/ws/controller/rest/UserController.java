package cm.packagemanager.pmanager.ws.controller.rest;

import cm.packagemanager.pmanager.user.ent.bo.UserBO;
import cm.packagemanager.pmanager.user.ent.service.UserService;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/* https://bnguimgo.developpez.com/tutoriels/spring/services-rest-avec-springboot-et-spring-resttemplate/?page=premiere-partie-le-serveur*/

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@Controller
@RequestMapping("/ws/*")
//@Validated
public class UserController {

	protected final Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	private UserService userService;






	@RequestMapping(value = "/useris",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public List<UserVO> getAllUserws() {
		return null ;//userRepositoty.findAll();
	}



	@RequestMapping(method=RequestMethod.GET,value = "/users", produces = { MediaType.APPLICATION_JSON_VALUE })
	//@GetMapping(value = "/users")
	public ResponseEntity<Collection<UserVO>> getAllUsers() {
		logger.info("into get all user request");
		Collection<UserVO> users = userService.getAllUsers();
		return new ResponseEntity<Collection<UserVO>>(users, HttpStatus.FOUND);
	}






}
