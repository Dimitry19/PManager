package cm.travelpost.tp.ws.controller.rest.users.totp;


import cm.travelpost.tp.common.sms.ent.service.TotpService;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.users.otp.SignupDTO;
import cm.travelpost.tp.ws.requests.users.otp.VerificationDTO;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.otp.SignupResponse;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.net.URI;


@RestController
@RequestMapping(WSConstants.TOTP_WS)
@Api(value = "totp-service", description = "Totp Operations",tags ="Totp" )
public class TotpController extends CommonController {

     private static Logger logger = LoggerFactory.getLogger(TotpController.class);


    @Autowired
    TotpService totpService;


    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody VerificationDTO codeVerification) throws Exception {
        String token = userService.verify(codeVerification.getUsername(), codeVerification.getCode());
        Response response = new Response();
        response.setRetDescription(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/userCreated", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignupDTO payload) throws Exception{
        logger.info("creating user {}", payload.getUsername());

        UserVO user =  userService.login(payload);
        user.setMultipleFactorAuthentication(payload.isMfa());


        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity
                .created(location)
                .body(new SignupResponse(user.isMultipleFactorAuthentication(),
                        totpService.getUriForImage(user.getSecret())));
    }

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> findUser(@PathVariable("username") String username) throws Exception {
        logger.info("retrieving user {}", username);

        return  ResponseEntity.ok(userService.findByUsername(username, false));
    }
}
