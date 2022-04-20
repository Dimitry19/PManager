package cm.travelpost.tp.ws.controller.rest.users;


import cm.travelpost.tp.user.ent.vo.RoleVO;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.users.RoleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cm.travelpost.tp.constant.WSConstants.ROLE_WS;

@RestController
@RequestMapping(ROLE_WS)
public class RoleController extends CommonController {


    @PostMapping(value = ROLE_WS_ADD)
    public ResponseEntity<RoleVO> add(@RequestBody RoleDTO role) throws Exception {
        try {
            log.info("add  role  request in");
            createOpentracingSpan("RoleController -add");
            RoleVO roleCreated = roleService.add(role);
            return new ResponseEntity<>(roleCreated, HttpStatus.CREATED);
        } catch (Exception e) {
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }
}
