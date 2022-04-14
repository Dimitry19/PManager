package cm.travelpost.tp.ws.controller.rest.authentication;

import cm.travelpost.tp.security.jwt.service.JwtService;
import cm.travelpost.tp.security.jwt.utils.JwtTokenUtils;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.users.LoginDTO;
import cm.travelpost.tp.ws.responses.AuthenticationResponse;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static cm.travelpost.tp.constant.WSConstants.AUTHENTICATE_WS;


@RestController
@RequestMapping(AUTHENTICATE_WS)
public class AuthenticationController extends CommonController {



    @Autowired
    private JwtTokenUtils jwtTokenUtil;

    @Autowired
    private JwtService jwtUserDetailsService;


    @RequestMapping(value = AUTH_TOKEN, method = RequestMethod.POST)
    public ResponseEntity<?> generateAuthenticationToken(@RequestBody LoginDTO  authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest);

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    private void authenticate(LoginDTO authenticationRequest) throws Exception {
        Objects.requireNonNull(authenticationRequest.getUsername());
        Objects.requireNonNull(authenticationRequest.getPassword());
        try {
                UserVO user = userService.login(authenticationRequest);
                if(user == null){
                    throw new Exception(WebServiceResponseCode.ERROR_CREDENTIALS_LABEL);
                }

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception(WebServiceResponseCode.ERROR_CREDENTIALS_LABEL, e);
        }
    }

}
