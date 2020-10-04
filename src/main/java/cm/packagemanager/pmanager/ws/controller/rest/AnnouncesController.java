package cm.packagemanager.pmanager.ws.controller.rest;


import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.service.AnnounceService;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.*;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static cm.packagemanager.pmanager.ws.controller.rest.CommonController.ANNOUNCE_WS;

@RestController
@RequestMapping(ANNOUNCE_WS)
public class AnnouncesController extends CommonController {

	protected final Log logger = LogFactory.getLog(AnnouncesController.class);


	@Autowired
	protected AnnounceService announceService;


	@RequestMapping(value = ANNOUNCE_WS_CREATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	AnnounceVO  create(HttpServletResponse response, HttpServletRequest request, @RequestBody AnnounceDTO ar) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		AnnounceVO announce = null;

		try
		{
			logger.info("create request out");
			if (ar!=null){
				announce=announceService.create(ar);

				if(announce!=null){
					//announce.setRetCode(WebServiceResponseCode.OK_CODE);
					//announce.setRetDescription(WebServiceResponseCode.ANNOUNCE_CREATE_LABEL);
				}else{
					announce=new AnnounceVO();
					//announce.setRetCode(WebServiceResponseCode.NOK_CODE);
					//announce.setRetDescription(WebServiceResponseCode.ERROR_ANNOUNCE_CREATE_LABEL);
				}
			}
		}
		catch (Exception e)	{
			logger.error("Errore eseguendo login: ", e);
			response.setStatus(400);
			response.getWriter().write(e.getMessage());
		}

		return  announce;
	}

	@GetMapping(USER_WS_UPDATE_ID)
	public ResponseEntity<String> update(@PathVariable Long id) {

		return new ResponseEntity<String>("Réponse du serveur: "+HttpStatus.OK.name(), HttpStatus.OK);
	}


	@PostMapping(value = USER_WS_ROLE)
	public ResponseEntity<AnnounceVO> addMessage(@RequestBody @Valid RoleToUserDTO roleToUser) {

			return null;
			//return new ResponseEntity<AnnounceVO>(announceService.update(roleToUser.getEmail(),true), HttpStatus.NOT_FOUND);


	}


	@GetMapping(USER_WS_DELETE_USER)
	public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long userId) throws Exception{
		logger.info("delete request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{

			logger.info("delete request out");

		}
		catch (Exception e)
		{
			logger.error("Errore eseguendo login: ", e);
			response.setStatus(400);
			response.getWriter().write(e.getMessage());
		}

		return pmResponse;
	}




	// Retrieve All Users
	@GetMapping(USER_WS_USERS)
	public ResponseEntity<List<AnnounceVO>> findAll(
			@Valid @Positive(message = "Page number should be a positive number") @RequestParam(required = false, defaultValue = "1") int page,
			@Valid @Positive(message = "Page size should be a positive number") @RequestParam(required = false, defaultValue = "10") int size) {

		HttpHeaders headers = new HttpHeaders();
		List<AnnounceVO> announces = null;//announceService.getAllUsers(page,size);
		headers.add("X-Users-Total", Long.toString(announces.size()));
		return new ResponseEntity<List<AnnounceVO>>(announces, headers, HttpStatus.OK);
	}

	@RequestMapping(USER_WS_USERS_PAGE_NO)
	@ResponseBody
	public List<UserVO> getAllPosts(@PathVariable int pageno,@PageableDefault(value=10, page=0) SpringDataWebProperties.Pageable pageable) throws ServletException {

		return null;//userService.getAllUsers(pageno,size);

	}


}
