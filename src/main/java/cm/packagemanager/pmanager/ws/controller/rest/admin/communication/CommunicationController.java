package cm.packagemanager.pmanager.ws.controller.rest.admin.communication;

import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cm.packagemanager.pmanager.constant.WSConstants.*;

@RestController
@RequestMapping(COMMUNICATION_WS)
@Api(value="Communication-service", description="Communication Operations")
public class CommunicationController extends CommonController {
	//Implementer le controle tel que seul l'admin peut operer ici
}
