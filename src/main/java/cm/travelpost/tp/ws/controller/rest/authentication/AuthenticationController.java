package cm.travelpost.tp.ws.controller.rest.authentication;


import cm.framework.ds.common.ent.vo.WSCommonResponseVO;
import cm.travelpost.tp.common.exception.UserNotFoundException;
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


@RestController
@RequestMapping(WSConstants.AUTHENTICATION_WS)
@Api(value = "Authentication-service", description = "Authentication Operations",tags ="Authenticate" )
public class AuthenticationController extends CommonController {

    protected final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Value("${tp.travelpost.active.registration.enable}")
    protected boolean enableAutoActivateRegistration;

    @Value("${tp.travelpost.qr.code.issuer}")
    protected String issuer;



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

        QrCodeResponse pmResponse = new QrCodeResponse();

        try {
            createOpentracingSpan("AuthenticationController -register");

            if (register != null) {

                UserVO user = userService.register(register);

                if (user != null && StringUtils.isNotEmpty(user.getError())) {

                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(user.getError());

                    return new ResponseEntity<>(pmResponse, HttpStatus.CONFLICT);
                }

                if (user == null) {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USER_REGISTER_LABEL);
                    return new ResponseEntity<>(pmResponse, HttpStatus.CONFLICT);
                }

                if (enableAutoActivateRegistration){
                    String qrCodeImage = authenticationService.qrCodeGenerator(user.getEmail(),user.getSecret(),issuer);
                    pmResponse.setMfa(true);
                    pmResponse.setSecretImageUri(qrCodeImage);
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.USER_QRCODE_LABEL);
                    response.setStatus(200);
                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            logger.error("Erreur durant l'execution de register: ", e);
            return getResponseMailResponseEntity(pmResponse, e,"Votre compte sera activé d'ici 24h");
        } finally {
            finishOpentracingSpan();
        }
        return null;
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
    ResponseEntity<Object> authenticate(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginDTO login) throws Exception {
        logger.info("Authenticate request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        UserVO user = null;

        try {
            createOpentracingSpan("AuthenticationController - login");

            if (login != null) {

                 UserInfo ui= userService.enableMFA(login);
                if(BooleanUtils.isFalse(ui.isEnableMFA())){

                    QrCodeResponse pmResponse = new QrCodeResponse();

                    String qrCodeImage = authenticationService.qrCodeGenerator(ui.getEmail(),ui.getSecret(),issuer);
                    pmResponse.setMfa(true);
                    pmResponse.setSecretImageUri(qrCodeImage);
                    pmResponse.setRetCode(WebServiceResponseCode.MFA_NOT_ENABLED);
                    pmResponse.setRetDescription(WebServiceResponseCode.USER_QRCODE_LABEL);
                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);
                }else{
                    user = userService.login(login);
                    if (user != null) {
                        user.setRetCode(WebServiceResponseCode.OK_CODE);
                       return new ResponseEntity<>(user, HttpStatus.OK);
                    }
                }

                WSCommonResponseVO commonResponse = new WSCommonResponseVO();
                commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                commonResponse.setRetDescription(WebServiceResponseCode.ERROR_LOGIN_LABEL);
                return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            logger.error("Erreur durant l'execution de login: ", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return null;

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
    public ResponseEntity<UserVO> verifyCode(HttpServletResponse response, HttpServletRequest request,@Valid @RequestBody VerificationDTO verification) throws Exception {

        UserVO user = userService.findByUsername(verification.getUsername());

        if(user == null){
            return new ResponseEntity<>(new UserVO(), HttpStatus.BAD_REQUEST);
        }

        if (BooleanUtils.isFalse(authenticationService.verifyCode(verification.getCode(), user.getSecret()))) {
            //TODO Controler le nombre de tentative de login
            user.setAccessToken(null);
            user.setAuthenticated(false);
            user.setRetCode(WebServiceResponseCode.NOK_CODE);
            user.setRetDescription(WebServiceResponseCode.ERROR_INVALID_CODE_LABEL);
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        String generatedToken = tokenProvider.createToken(user.getUsername(),true);

        user.setAuthenticated(true);
        user.setAccessToken(generatedToken);
        user.setRetCode(WebServiceResponseCode.OK_CODE);
        user.setRetDescription(WebServiceResponseCode.LOGIN_OK_LABEL);
        cookie(response,user.getUsername(),user.getPassword());
        return ResponseEntity.ok(user);
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

        Response pmResponse = new Response();


        try {
            logger.info("confirm request in");
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

            createOpentracingSpan("AuthenticationController -confirmation");

            UserVO user = userService.findByToken(token);

            if (user == null) {
                pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                pmResponse.setRetDescription(WebServiceResponseCode.ERROR_INVALID_TOKEN_REGISTER_LABEL);
                response.sendRedirect(getRedirectPage(RedirectType.CONFIRMATION_ERROR));
            } else {

                if (user.getActive() == 1) {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USED_TOKEN_REGISTER_LABEL);
                    response.sendRedirect(getRedirectPage(RedirectType.CONFIRMATION_ERROR));
                }

                user.setActive(1);
                if (userService.update(user) != null) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.USER_QRCODE_LABEL);
                    response.sendRedirect(getRedirectPage(RedirectType.CONFIRMATION));
                }
            }

        } catch (Exception e) {
            logger.error("Errore eseguendo confirm: ", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    @PostMapping(path = AUTHENTICATION_WS_QRCODE,headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody ResponseEntity<QrCodeResponse> generateQrCode(HttpServletResponse response, HttpServletRequest request,  @RequestBody LoginDTO login) throws Exception {

        UserVO user =userService.login(login);
        QrCodeResponse pmResponse = new QrCodeResponse();

        if(user == null){
            pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USER_QRCODE_LABEL);
            return new ResponseEntity<>(pmResponse, HttpStatus.OK);
        }
        //TODO Controler le nombre de tentative de login
        pmResponse.setMfa(true);
        String qrCodeImage = authenticationService.qrCodeGenerator(user.getEmail(),user.getSecret(),issuer);
        pmResponse.setSecretImageUri(qrCodeImage);
        pmResponse.setRetCode(WebServiceResponseCode.MFA_NOT_ENABLED);
        pmResponse.setRetDescription(WebServiceResponseCode.USER_QRCODE_LABEL);
        return new ResponseEntity<>(pmResponse, HttpStatus.OK);

    }



    @GetMapping(value = WSConstants.LOGOUT, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response, @RequestParam  @Valid String username) throws Exception {

        //TODO Gerer les cookies pour la deconnexion

        Response pmResponse = new Response();

        UserVO user = userService.findByUsername(username,false);

       if (user!=null){

           if(request.getCookies() == null){
               pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
               pmResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);
               return new ResponseEntity<>(pmResponse, HttpStatus.OK);
           }

           for (Cookie cookie : request.getCookies()) {


               if (StringUtils.equals(cookie.getValue(),builCookiePart(user.getUsername(),user.getPassword()))) {

                   cookie.setValue(null);
                   cookie.setMaxAge(0);
                   cookie.setPath(request.getContextPath());
                   response.addCookie(cookie);
                   pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                   pmResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);
                   return new ResponseEntity<>(pmResponse, HttpStatus.OK);
               }
               pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
               pmResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);
               return new ResponseEntity<>(pmResponse, HttpStatus.OK);
           }
       }else{
           pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
           pmResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);
           return new ResponseEntity<>(pmResponse, HttpStatus.OK);
       }
       throw new UserNotFoundException(WebServiceResponseCode.ERROR_LOGOUT_LABEL);
    }
    private void  cookie(HttpServletResponse response,String username, String password){
        Cookie cookie =new Cookie("_tps_2P_", builCookiePart(username,password));
        cookie.setSecure(true);
        cookie.setDomain(travelPostDomain);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }
    private String builCookiePart(String username, String password){

        return PasswordGenerator.encrypt(username).concat(password.substring(0, password.length()-4).concat(password.substring(2,password.length()-1)));

    }




}
