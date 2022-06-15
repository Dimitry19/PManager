package cm.travelpost.tp.ws.controller.rest.authentication;


import cm.travelpost.tp.authentication.ent.vo.AuthenticationVO;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.security.PasswordGenerator;
import cm.travelpost.tp.user.ent.vo.UserInfo;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.controller.RedirectType;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.authentication.VerificationDTO;
import cm.travelpost.tp.ws.requests.users.LoginDTO;
import cm.travelpost.tp.ws.requests.users.RegisterDTO;
import cm.travelpost.tp.ws.responses.QrCodeResponse;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import cm.travelpost.tp.ws.responses.authentication.AuthenticationResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;

import static cm.travelpost.tp.common.Constants.TP_ACTIVATE_ACCOUNT;


@RestController
@RequestMapping(WSConstants.AUTHENTICATION_WS)
@Api(value = "Authentication-service", description = "Authentication Operations",tags ="Authenticate" )
public class AuthenticationController extends CommonController {

    protected final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Value("${tp.travelpost.active.registration.enable}")
    protected boolean enableAutoActivateRegistration;

    @Value("${tp.travelpost.active.registration.qr.code.enable}")
    protected boolean enableQrCodeRegistration;

    @Value("${tp.travelpost.qr.code.issuer}")
    protected String issuer;

    @Value("${tp.travelpost.authentication.attempt}")
    private int attemptLimit;

    @Value("${mail.admin.username}")
    protected String defaultContactUs;



    @ApiOperation(value = "Register an user ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful registration",
                    response = Response.class, responseContainer = "Object")})
    @PostMapping(path = WSConstants.REGISTRATION, consumes = {MediaType.APPLICATION_JSON}, produces = {MediaType.APPLICATION_JSON}, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Response> register(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid RegisterDTO register) throws ValidationException {

        logger.info("register request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

        QrCodeResponse tpResponse = new QrCodeResponse();

        try {
            createOpentracingSpan("AuthenticationController - register");


            UserVO user = userService.register(register);

            if (user != null && StringUtils.isNotEmpty(user.getError())) {
                tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                tpResponse.setMessage(user.getError());
                response.setStatus(400);
                return new ResponseEntity<>(tpResponse, HttpStatus.CONFLICT);
            }

            if (user == null) {
                tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                tpResponse.setMessage(WebServiceResponseCode.ERROR_USER_REGISTER_LABEL);
                response.setStatus(400);
                return new ResponseEntity<>(tpResponse, HttpStatus.CONFLICT);
            }

            if(BooleanUtils.isTrue(enableQrCodeRegistration)){
                String qrCodeImage = authenticationService.qrCodeGenerator(user.getEmail(),user.getSecret(),issuer);
                tpResponse.setMfa(true);
                tpResponse.setSecretImageUri(qrCodeImage);
            }


            if(enableAutoActivateRegistration){
                tpResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_LABEL);
            }


         /*   if(BooleanUtils.isFalse(enableQrCodeRegistration) && BooleanUtils.isTrue(mailService.sendConfirmationMail(request, user))){
                tpResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_MAIL_LABEL);
            }*/

            tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
            response.setStatus(200);
            return new ResponseEntity<>(tpResponse, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Erreur durant l'execution de register: ", e);
            return getResponseMailResponseEntity(tpResponse, e,"Votre compte sera activé d'ici 24h");
        } finally {
            finishOpentracingSpan();
        }

    }

    @ApiOperation(value = "Confirmation user registration ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful registration",
                    response = Response.class, responseContainer = "Object")})
    @GetMapping(path = WSConstants.USER_WS_CONFIRMATION, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody void confirmation(HttpServletResponse response, HttpServletRequest request, @RequestParam("token") String token) throws Exception {

        Response tpResponse = new Response();


        try {
            logger.info("confirm request in");
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

            createOpentracingSpan("AuthenticationController - confirmation");

            UserVO user = userService.findByToken(token);

            if (user == null) {
                tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                tpResponse.setMessage(WebServiceResponseCode.ERROR_INVALID_TOKEN_REGISTER_LABEL);
                response.sendRedirect(getRedirectPage(RedirectType.CONFIRMATION_ERROR));
            } else {

                if (user.getActive() == TP_ACTIVATE_ACCOUNT) {
                    tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    tpResponse.setMessage(WebServiceResponseCode.ERROR_USED_TOKEN_REGISTER_LABEL);
                    response.sendRedirect(getRedirectPage(RedirectType.CONFIRMATION_ERROR));
                }

                user.setActive(TP_ACTIVATE_ACCOUNT);
                if (userService.update(user) != null) {
                    tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    tpResponse.setRetDescription(WebServiceResponseCode.QRCODE_LABEL);
                    response.sendRedirect(getRedirectPage(RedirectType.CONFIRMATION));
                }
            }

        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = " Login user ", response = UserVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful registration",
                    response = UserVO.class, responseContainer = "Object")})
    @PostMapping(path = WSConstants.USER_WS_LOGIN, consumes = {MediaType.APPLICATION_JSON}, produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Object> authenticate(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid LoginDTO login) throws Exception {
        logger.info("Authenticate request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        UserVO user = null;

        try {
            createOpentracingSpan("AuthenticationController - login");

            UserInfo ui=userService.checkMFA(login);

            boolean partialMfaEnabled=ui!=null && BooleanUtils.isTrue(ui.isMultipleFactorAuthentication());
            boolean mfaEnabled= BooleanUtils.isTrue(partialMfaEnabled) &&  StringUtils.isNotEmpty(ui.getSecret());
            boolean generateQrCode= BooleanUtils.isTrue(partialMfaEnabled) &&  StringUtils.isEmpty(ui.getSecret());

            if(BooleanUtils.isTrue(mfaEnabled)){

                AuthenticationVO authentication = userService.checkAttempt(login.getUsername());
                if(authentication !=null && BooleanUtils.isTrue(authentication.isDesactivate())) {
                        return getResponseLoginErrorResponseEntity(MessageFormat.format(WebServiceResponseCode.ERROR_LOGIN_ATTEMPT_KO_LABEL,attemptLimit, defaultContactUs));
                }
                user = userService.findByUsername(login.getUsername());
                if (user != null) {
                    user.setRetCode(WebServiceResponseCode.LOGIN_MFA_ENABLED);
                    userService.resetUserAuthentication(login.getUsername());
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
            }

            if(BooleanUtils.isTrue(generateQrCode)) {

                ui = userService.enableMFA(login);
                if (ui == null) {
                    return getResponseLoginErrorResponseEntity(WebServiceResponseCode.ERROR_LOGIN_LABEL);
                }

                QrCodeResponse tpResponse = new QrCodeResponse();
                String qrCodeImage = authenticationService.qrCodeGenerator(ui.getEmail(), ui.getSecret(), issuer);
                tpResponse.setMfa(true);
                tpResponse.setSecretImageUri(qrCodeImage);
                tpResponse.setRetCode(WebServiceResponseCode.QR_CODE_MFA_ENABLED);
                tpResponse.setRetDescription(WebServiceResponseCode.QRCODE_LABEL);
                userService.resetUserAuthentication(login.getUsername());
                return new ResponseEntity<>(tpResponse, HttpStatus.OK);
            }else{
                user = userService.login(login);

                if(user != null) {
                    user.setRetCode(WebServiceResponseCode.OK_CODE);
                    userService.resetUserAuthentication(login.getUsername());
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
            }
            return getResponseLoginErrorResponseEntity(WebServiceResponseCode.ERROR_LOGIN_LABEL);

        } catch (Exception e) {
            logger.error("Erreur durant l'execution de login: ", e);
            return attempt(login.getUsername(),  e);
        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = "Verification  user token to login ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful login",
                    response = Response.class, responseContainer = "Object")})
    @PostMapping(AUTHENTICATION_WS_VERIFICATION)
    @PreAuthorize("hasRole('PRE_VERIFICATION_USER')")
    public ResponseEntity<Object> verifyCode(HttpServletResponse response, HttpServletRequest request,@Valid @RequestBody VerificationDTO verification) throws Exception {


        createOpentracingSpan("AuthenticationController - verifyCode");
        try {

            UserVO user = userService.findByUsername(verification.getUsername());
            StringBuilder retDescription = new StringBuilder();

            if (user != null) {

                   if (BooleanUtils.isFalse(authenticationService.verifyCode(verification.getCode(), user.getSecret()))) {

                    AuthenticationVO authentication = userService.checkAuthenticationAttempt(verification.getUsername());
                    retDescription.append(WebServiceResponseCode.ERROR_INVALID_CODE_LABEL);

                    if (authentication != null) {

                            if (BooleanUtils.isTrue(authentication.isDesactivate())) {
                                retDescription.append(MessageFormat.format(WebServiceResponseCode.ERROR_LOGIN_ATTEMPT_KO_LABEL, attemptLimit, defaultContactUs));
                            }

                            int attempt = authentication.getAttempt();
                            if (BooleanUtils.isFalse(authentication.isDesactivate()) && BooleanUtils.isTrue(attempt >= 0 && attempt < attemptLimit)) {
                                int diff = attemptLimit - attempt;
                                String attemptMessage = (diff <= 0) ? MessageFormat.format(WebServiceResponseCode.ERROR_LOGIN_ATTEMPT_KO_LABEL, attemptLimit, defaultContactUs)
                                        : MessageFormat.format(WebServiceResponseCode.ERROR_LOGIN_ATTEMPT_LABEL, diff, attemptLimit);
                                retDescription.append(attemptMessage);
                            }
                        }
                    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
                    authenticationResponse.setAccessToken(null);
                    authenticationResponse.setAuthenticated(false);
                    authenticationResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    authenticationResponse.setMessage(retDescription.toString());
                    return new ResponseEntity<>(authenticationResponse, HttpStatus.BAD_REQUEST);
                }

                String generatedToken = tokenProvider.createToken(user.getUsername(), true);

                user.setAuthenticated(true);
                user.setAccessToken(generatedToken);
                user.setRetCode(WebServiceResponseCode.OK_CODE);
                user.setRetDescription(WebServiceResponseCode.LOGIN_OK_LABEL);

                userService.resetUserAuthentication(user.getUsername());
                cookie(response, user.getUsername(), user.getPassword());
                return ResponseEntity.ok(user);
        }
        } finally {
            finishOpentracingSpan();
        }
        return new ResponseEntity<>(new UserVO(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = AUTHENTICATION_WS_QRCODE,headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody ResponseEntity<QrCodeResponse> generateQrCode(HttpServletResponse response, HttpServletRequest request,  @RequestBody LoginDTO login) throws Exception {

        UserVO user =userService.login(login);
        QrCodeResponse tpResponse = new QrCodeResponse();

        if(user == null){
            tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            tpResponse.setMessage(WebServiceResponseCode.ERROR_USER_QRCODE_LABEL);
            return new ResponseEntity<>(tpResponse, HttpStatus.OK);
        }
        tpResponse.setMfa(true);
        String qrCodeImage = authenticationService.qrCodeGenerator(user.getEmail(),user.getSecret(),issuer);
        tpResponse.setSecretImageUri(qrCodeImage);
        tpResponse.setRetCode(WebServiceResponseCode.QR_CODE_MFA_ENABLED);
        tpResponse.setRetDescription(WebServiceResponseCode.QRCODE_LABEL);
        return new ResponseEntity<>(tpResponse, HttpStatus.OK);

    }



    @GetMapping(value = WSConstants.LOGOUT, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response, @RequestParam  @Valid String username) throws Exception {

        //TODO Gerer les cookies pour la deconnexion

        Response tpResponse = new Response();

        UserVO user = userService.findByUsername(username,false);

       if (user!=null){

           if(request.getCookies() == null){
               tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
               tpResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);
               return new ResponseEntity<>(tpResponse, HttpStatus.OK);
           }

           for (Cookie cookie : request.getCookies()) {


               if (StringUtils.equals(cookie.getValue(),buildCookiePart(user.getUsername(),user.getPassword()))) {

                   cookie.setValue(null);
                   cookie.setMaxAge(0);
                   cookie.setPath(request.getContextPath());
                   response.addCookie(cookie);
                   tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                   tpResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);
                   return new ResponseEntity<>(tpResponse, HttpStatus.OK);
               }
               tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
               tpResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);
           }
       }else{
           tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
           tpResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);

       }
        return new ResponseEntity<>(tpResponse, HttpStatus.OK);
    }

    private void  cookie(HttpServletResponse response,String username, String password){
        Cookie cookie =new Cookie("_tps_2P_", buildCookiePart(username,password));
        cookie.setSecure(true);
        cookie.setDomain(travelPostDomain);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }
    private String buildCookiePart(String username, String password){

        return PasswordGenerator.encrypt(username).concat(password.substring(0, password.length()-4).concat(password.substring(2,password.length()-1)));
    }


    private ResponseEntity<Object>  attempt(String username, Exception e) throws Exception {
        AuthenticationVO authentication = userService.checkAuthenticationAttempt(username);

        if(authentication !=null){
            int attempt = authentication.getAttempt();
            if(BooleanUtils.isTrue(attempt >=0)){
                int diff=attemptLimit-attempt;
                String attemptMessage= (diff<=0)? MessageFormat.format(WebServiceResponseCode.ERROR_LOGIN_ATTEMPT_KO_LABEL, attemptLimit,defaultContactUs)
                        :MessageFormat.format(WebServiceResponseCode.ERROR_LOGIN_ATTEMPT_LABEL,diff, attemptLimit);
                return getResponseLoginErrorResponseEntity(attemptMessage);
            }
        }
        throw e;
    }
}
