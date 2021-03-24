package cm.packagemanager.pmanager.ws.controller.rest.users;


import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.service.RoleService;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.users.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static cm.packagemanager.pmanager.constant.WSConstants.*;

@RestController
@RequestMapping(ROLE_WS)
public class RoleController extends CommonController {

	@Autowired
	RoleService roleService;

	@PostMapping(value = ROLE_WS_ADD)
	public ResponseEntity<RoleVO> add(@RequestBody RoleDTO role) throws Exception {
		try{
			logger.info("add  role  request in");
			createOpentracingSpan("RoleController -add");
			RoleVO roleCreated = roleService.add(role);
 		    return new ResponseEntity<RoleVO>(roleCreated, HttpStatus.CREATED);
		}catch (Exception e){
			throw e;
		}finally {
			finishOpentracingSpan();
		}
 	}
}
