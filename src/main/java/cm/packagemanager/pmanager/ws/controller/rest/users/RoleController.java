package cm.packagemanager.pmanager.ws.controller.rest.users;


import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.service.RoleService;
import cm.packagemanager.pmanager.ws.requests.users.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/ws/role/*")
public class RoleController {

	@Autowired
	RoleService roleService;



	@PostMapping(value = "/add")
	@Transactional
	public ResponseEntity<RoleVO> add(@RequestBody RoleDTO role) {

		RoleVO roleCreated = roleService.add(role);
 		return new ResponseEntity<RoleVO>(roleCreated, HttpStatus.CREATED);
 	}
}
