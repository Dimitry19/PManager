package cm.packagemanager.pmanager.ws.controller.rest.users;


import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.users.*;
import cm.packagemanager.pmanager.ws.responses.PaginateResponse;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import com.sun.mail.smtp.SMTPSendFailedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import static cm.packagemanager.pmanager.constant.WSConstants.USER_WS;


@RestController
@RequestMapping(USER_WS)
@Api(value = "user-service", description = "User Operations",tags ="user" )
public class UserController extends CommonController {

    protected final Log logger = LogFactory.getLog(UserController.class);


    @ApiOperation(value = "Register an user ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful registration",
                    response = Response.class, responseContainer = "Object")})
    @PostMapping(path = USER_WS_REGISTRATION, consumes = {MediaType.APPLICATION_JSON}, produces = {MediaType.APPLICATION_JSON}, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Response> register(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid RegisterDTO register) throws ValidationException, IOException {

        logger.info("register request in");
        response.setHeader("Access-Control-Allow-Origin", "*");

        Response pmResponse = new Response();

        try {
            createOpentracingSpan("UserController -register");

            if (register != null) {

                UserVO usr = userService.register(register);

                if (usr != null && StringUtils.isNotEmpty(usr.getError())) {

                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(usr.getError());

                    return new ResponseEntity<>(pmResponse, HttpStatus.CONFLICT);
                }

                if (usr == null) {

                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USER_REGISTER_LABEL);
                    return new ResponseEntity<>(pmResponse, HttpStatus.CONFLICT);
                }

                if(mailService.buildAndSendMail(request, usr)){
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_LABEL);
                    response.setStatus(200);
                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);
                }else {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.ERROR_CONTACT_US_LABEL);
                    response.setStatus(401);
                    return new ResponseEntity<>(pmResponse, HttpStatus.NOT_ACCEPTABLE);
                }


                /*if (mailSenderSendGrid.manageResponse(sent)) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_LABEL);
                    response.setStatus(200);
                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);

                } else {
                    pmResponse.setRetCode(sent.getStatusCode());
                    pmResponse.setRetDescription(sent.getBody());
                    response.setStatus(sent.getStatusCode());
                    userService.remove(usr);
                    return new ResponseEntity<>(pmResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                }*/
            }
        } catch (Exception e) {
            logger.error("Errore eseguendo register: ", e);
            pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            if(e instanceof MessagingException || e instanceof SMTPSendFailedException || e instanceof MailSendException){
                pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_MAIL_SERVICE_UNAVAILABLE_LABEL,"Votre compte sera activé d'ici 24h"));
            }else{
                pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USER_REGISTER_LABEL);
            }
            return new ResponseEntity<>(pmResponse, HttpStatus.SERVICE_UNAVAILABLE);
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }


    @ApiOperation(value = "Confirmation user registration ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful registration",
                    response = Response.class, responseContainer = "Object")})
    //@RequestMapping(value=USER_WS_CONFIRMATION, method = RequestMethod.GET, headers =WSConstants.HEADER_ACCEPT)
    @GetMapping(path = USER_WS_CONFIRMATION, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody void confirmation(HttpServletResponse response, HttpServletRequest request, @RequestParam("token") String token) throws Exception {

        Response pmResponse = new Response();


        try {
            logger.info("confirm request in");
            response.setHeader("Access-Control-Allow-Origin", "*");

            createOpentracingSpan("UserController -confirmation");

            UserVO user = userService.findByToken(token);

            if (user == null) {
                pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                pmResponse.setRetDescription(WebServiceResponseCode.ERROR_INVALID_TOKEN_REGISTER_LABEL);
                response.sendRedirect(redirectConfirmErrorPage);
            } else {

                if (user.getActive() == 1) {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USED_TOKEN_REGISTER_LABEL);
                    response.sendRedirect(redirectConfirmErrorPage);
                }

                user.setActive(1);
                if (userService.update(user) != null) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_ACTIVE_LABEL);
                    response.sendRedirect(redirectConfirmPage);
                }
            }

        } catch (Exception e) {
            logger.error("Errore eseguendo confirm: ", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }


       /* String externalUrl = "https://some-domain.com/path/to/somewhere";
        response.setStatus(302);
        response.setHeader("Location", externalUrl);*/
        //return new ResponseEntity<>(pmResponse, HttpStatus.OK);
    }

    @ApiOperation(value = " Login user ", response = UserVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful registration",
                    response = UserVO.class, responseContainer = "Object")})
    //@RequestMapping(value = USER_WS_LOGIN, method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @PostMapping(path = USER_WS_LOGIN, consumes = {MediaType.APPLICATION_JSON}, produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Object> login(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginDTO login) throws Exception {
        logger.info("login request in");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addCookie(new Cookie("username", login.getUsername()));
        UserVO user = null;



        try {
            createOpentracingSpan("UserController -login");

            if (login != null) {
                user = userService.login(login);

                if (user != null) {
                    user.setRetCode(WebServiceResponseCode.OK_CODE);
                    user.setRetDescription(WebServiceResponseCode.LOGIN_OK_LABEL);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                } else {
                    WSCommonResponseVO commonResponse = new WSCommonResponseVO();
                    commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    commonResponse.setRetDescription(WebServiceResponseCode.ERROR_LOGIN_LABEL);
                    return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            logger.error("Errore eseguendo login: ", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return null;

    }


    @ApiOperation(value = "En/disable notification ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successfully change status of notification",
                    response = ResponseEntity.class, responseContainer = "Object")})
    @GetMapping(value = ENABLE_NOTIFICATION_WS, produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<UserVO> manageNotification(HttpServletRequest request,
                                              HttpServletResponse response,
                                              @RequestParam("userId") @Valid Long userId,
                                              @RequestParam("enable") @Valid boolean enable) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info(" Manage notification request in");
        try {
            createOpentracingSpan("UserController - Manage notification");
            UserVO user = userService.manageNotification(userId, enable);
            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Erreur durant l'upload de l'image", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }


    @ApiOperation(value = " Update user ", response = UserVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful update",
                    response = UserVO.class, responseContainer = "Object")})
    //@RequestMapping(value =UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
    @PutMapping(value = USER_WS_UPDATE_ID, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<UserVO> update(HttpServletResponse response, HttpServletRequest request, @PathVariable long userId,
                                  @RequestBody @Valid UpdateUserDTO userDTO) throws UserException, ValidationException, IOException {

        logger.info("update user request in");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            createOpentracingSpan("UserController -update");

            if (userDTO != null) {
                userDTO.setId(userId);
                UserVO user = userService.updateUser(userDTO);
                if (user == null) {
                    WSCommonResponseVO commonResponse = new WSCommonResponseVO();
                    commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    commonResponse.setRetDescription(WebServiceResponseCode.ERROR_UPDATE_USER_LABEL);
                    throw new UserNotFoundException("Utilisateur non trouv&egrave;");
                }

                user.setRetCode(WebServiceResponseCode.OK_CODE);
                user.setRetDescription(WebServiceResponseCode.UPDATE_LABEL);
                return new ResponseEntity<UserVO>(user, HttpStatus.OK);
            }
        } catch (UserException e) {
            logger.error("Erreur durant l'ajournement de l'utilisateur  " + userDTO.toString() + "{}", e);
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }

    @ApiOperation(value = " Update user ", response = UserVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful update",
                    response = UserVO.class, responseContainer = "Object")})
    //@RequestMapping(value =UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
    @PutMapping(value = USER_WS_PASSWORD_UPDATE_ID, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Response> editPassword(HttpServletResponse response, HttpServletRequest request, @PathVariable long userId,
                                          @RequestBody @Valid ManagePasswordDTO managePassword) throws UserException, ValidationException, IOException {

        logger.info("edit  user password  request in");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            createOpentracingSpan("UserController - edit password");
            Response pmresponse = new Response();

            if (managePassword != null) {
                boolean edited = userService.editPassword(userId, managePassword.getOldPassword(), managePassword.getNewPassword());
                if (edited) {
                    pmresponse.setRetCode(0);
                    pmresponse.setRetDescription(WebServiceResponseCode.UPDATE_PASSWORD_LABEL);
                }
                return new ResponseEntity<Response>(pmresponse, HttpStatus.OK);
            }
        } catch (UserException e) {
            logger.error("Erreur durant l'ajournement de l'utilisateur  " + managePassword.toString() + "{}", e);
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finishOpentracingSpan();
        }

        return null;
    }

    @ApiOperation(value = " Retrieve password", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Password sent successfully",
                    response = Response.class, responseContainer = "Object")})
    @RequestMapping(value = USER_WS_PASSWORD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    Response password(HttpServletResponse response, HttpServletRequest request, @RequestBody PasswordDTO password) throws Exception {


        logger.info("password request in");
        response.setHeader("Access-Control-Allow-Origin", "*");
        Response pmResponse = new Response();

        try {
            createOpentracingSpan("UserController -password");
            if (password != null) {

                if (mailSenderSendGrid.manageResponse(userService.managePassword(password.getEmail()))) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.RETRIVEVE_PASSWORD_LABEL + " " + password.getEmail());
                } else {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.ERROR_RETRIEVE_PASSWORD_LABEL);
                }
            }
            return pmResponse;
        } catch (UserException e) {
            logger.error("Erreur durant la recuperation du mot de passe de l'utilisateur  " + password.toString() + "{}", e);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            logger.error("Erreur durant la recuperation du mot de passe de l'utilisateur  " + password.toString() + "{}", e);
            e.printStackTrace();
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = " Update user role ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successfully updated user role",
                    response = ResponseEntity.class, responseContainer = "Object")})
    @PostMapping(value = USER_WS_ROLE, consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<UserVO> setRole(@RequestBody @Valid RoleToUserDTO roleToUser) throws Exception {

        logger.info("set role request in");

        try {
            createOpentracingSpan("UserController -setRole");
            if (userService.setRoleToUser(roleToUser)) {
                userService.findByEmail(roleToUser.getEmail());
                return new ResponseEntity<UserVO>(userService.findByEmail(roleToUser.getEmail()), HttpStatus.FOUND);
            } else {
                return new ResponseEntity<UserVO>(userService.findByEmail(roleToUser.getEmail()), HttpStatus.NOT_FOUND);
            }
        } catch (UserException e) {
            logger.error("Erreur durant l'ajournement  du role de l'utilisateur  " + roleToUser.toString() + "{}", e);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            logger.error("Erreur durant l'ajournement  du role de l'utilisateur  " + roleToUser.toString() + "{}", e);
            e.printStackTrace();
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }


    @ApiOperation(value = "Delete user ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful deleted",
                    response = Response.class, responseContainer = "Object")})
    @DeleteMapping(value = DELETE, headers = WSConstants.HEADER_ACCEPT)
    public Response delete(HttpServletResponse response, HttpServletRequest request, @Valid @RequestParam("id") Long id) throws UserException {
        logger.info("delete request in");
        response.setHeader("Access-Control-Allow-Origin", "*");
        Response pmResponse = new Response();

        try {
            createOpentracingSpan("UserController -delete");
            if (id != null) {
                if (userService.deleteUser(id)) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.CANCELLED_USER_LABEL);
                } else {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.ERROR_DELETE_USER_CODE_LABEL);
                }
            }
        } catch (Exception e) {
            //response.getWriter().write(e.getMessage());
            pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            pmResponse.setRetDescription(e.getMessage());
            logger.error("Erreur durant la delete de l'utilisateur ayant id:" + id + "{}", e);
            e.printStackTrace();
            throw e;
        } finally {
            finishOpentracingSpan();
        }

        return pmResponse;
    }

    @ApiOperation(value = " Retrieve user with an ID ", response = UserVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful user retrieving",
                    response = ResponseEntity.class, responseContainer = "Object")})
    @RequestMapping(value = USER_WS_USER_ID, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT,produces = MediaType.APPLICATION_JSON)
   // @GetMapping(value = USER_WS_USER_ID, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<UserVO> infosUser(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") @Valid Long id) throws UserNotFoundException, IOException {
        try {
            createOpentracingSpan("UserController -getUser");
            logger.info("retrieve user request in");
            response.setHeader("Access-Control-Allow-Origin", "*");
            UserVO user = userService.getUser(id);
            if (user != null) {
                return new ResponseEntity<UserVO>(user, HttpStatus.OK);
            } else {
                throw new UserNotFoundException("Utilisateur non trouvé");
            }

        } catch (UserNotFoundException e) {
            logger.error("Erreur durant l'execution de recuperation des infos de l'utilisateur: ", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = " Send mail ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Mail sent",
                    response = Response.class, responseContainer = "Object")})
    @RequestMapping(value = USER_WS_MAIL, method = RequestMethod.POST, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Response> sendEmail(HttpServletResponse response, HttpServletRequest request, @RequestBody MailDTO mail) throws MessagingException, IOException {

        logger.info("send mail request in");
        response.setHeader("Access-Control-Allow-Origin", "*");
        Response pmResponse = new Response();

        try {
            createOpentracingSpan("UserController - sendMail");
            if (mail != null) {
                if (mailSenderSendGrid.manageResponse(mailService.sendMail(mail, true))) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.MAIL_SENT_LABEL);
                    return new ResponseEntity<Response>(pmResponse, HttpStatus.OK);
                } else {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_MAIL_SERVICE_UNAVAILABLE_LABEL, "Veuillez reessayez plutard , Merci!"));
                    return new ResponseEntity<Response>(pmResponse, HttpStatus.SERVICE_UNAVAILABLE);
                }

            }
        } catch (Exception e) {
            logger.error("Erreur durant l'execution de l'envoi du mail: ", e);
            //response.getWriter().write(e.getMessage());
            return getResponseMailResponseEntity(pmResponse, e);
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }


    @ApiOperation(value = " Retrieve users ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieving",
                    response = ResponseEntity.class, responseContainer = "Object")})
    @GetMapping(value = USER_WS_USERS, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}, headers = HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> users(HttpServletResponse response, HttpServletRequest request,
                                                  @Valid @Positive(message = "la page doit etre nombre positif") @RequestParam(required = false, defaultValue = DEFAULT_PAGE) int page,
                                                  @Valid @Positive(message = "Page size should be a positive number") @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {


        logger.info("get all users request in");
        HttpHeaders headers = new HttpHeaders();
        PageBy pageBy = new PageBy(page, size);
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            createOpentracingSpan("UserController -users");
            int count = userService.count(pageBy);
            List<UserVO> users = userService.getAllUsers(pageBy);
            return getPaginateResponseResponseEntity(headers, paginateResponse, users);
        } catch (Exception e) {
            response.getWriter().write(e.getMessage());
            logger.info(" UserController -users:Exception occurred while fetching the response from the database.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            finishOpentracingSpan();
        }
    }

    //@RequestMapping(USER_WS_USERS_PAGE_NO)
    @ResponseBody
    public List<UserVO> users(@PathVariable int pageno, @PageableDefault(value = 10, page = 0) SpringDataWebProperties.Pageable pageable) throws Exception {

        PageBy pageBy = new PageBy(pageno, size);
        return userService.getAllUsers(pageBy);
    }


    @ApiOperation(value = "Subscription to an user ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful subscription",
                    response = Response.class, responseContainer = "Object")})
    @PostMapping(value = USER_ADD_SUBSCRIBER_WS, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}, consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<Response> subscribe(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid SubscribeDTO subscribe) throws ValidationException, UserException {

        logger.info("subscribe request in");
        response.setHeader("Access-Control-Allow-Origin", "*");

        Response pmResponse = new Response();

        try {
            createOpentracingSpan("UserController - subscribe");

            if (subscribe != null) {

                if (subscribe.getSubscriberId().equals(subscribe.getSubscriptionId())) {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.CONFLICT_SUBSCRIBE_LABEL);
                    response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                    return new ResponseEntity<>(pmResponse, HttpStatus.NOT_ACCEPTABLE);
                }
                userService.subscribe(subscribe);

                pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                pmResponse.setRetDescription(WebServiceResponseCode.SUBSCRIBE_LABEL);
                response.setStatus(200);
                return new ResponseEntity<>(pmResponse, HttpStatus.OK);

            }
        } catch (UserException e) {
            logger.error("Erreur durant l'execution de  subscribe: ", e);
            pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            pmResponse.setRetDescription(WebServiceResponseCode.ERROR_SUBSCRIBE_LABEL);
            return new ResponseEntity<Response>(pmResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            finishOpentracingSpan();
        }

        return null;
    }

    @ApiOperation(value = "Unsubscription to an user ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful unsubscription",
                    response = Response.class, responseContainer = "Object")})
    @PostMapping(value = UNSUBSCRIBE_WS, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity<Response> unsubscribe(HttpServletRequest request, HttpServletResponse response,
                                                @RequestBody @Valid SubscribeDTO subscribe) throws ValidationException, IOException {

        logger.info("unsubscribe request in");
        response.setHeader("Access-Control-Allow-Origin", "*");

        Response pmResponse = new Response();

        try {
            createOpentracingSpan("UserController - unsubscribe");

            if (subscribe != null) {

                if (subscribe.getSubscriberId().equals(subscribe.getSubscriptionId())) {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.CONFLICT_SUBSCRIBE_LABEL);
                    response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                    return new ResponseEntity<Response>(pmResponse, HttpStatus.NOT_ACCEPTABLE);
                }
                userService.unsubscribe(subscribe);
                userService.unsubscribe(subscribe);

                pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                pmResponse.setRetDescription(WebServiceResponseCode.UNSUBSCRIBE_LABEL);
                response.setStatus(200);
                return new ResponseEntity<Response>(pmResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Erreur durant l'execution de  unsubscribe: ", e);
            pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            pmResponse.setRetDescription(WebServiceResponseCode.ERROR_UNSUBSCRIBE_LABEL);
            return new ResponseEntity<Response>(pmResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }


    @ApiOperation(value = "User subscriptions ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully subscription list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful Subscription list ",
                    response = ResponseEntity.class, responseContainer = "Object")})
    @RequestMapping(value = USER_SUBSCRIPTION_WS, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<PaginateResponse> subscriptions(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") @Valid Long userId) throws ValidationException, IOException {

        logger.info("subscriptions request in");
        response.setHeader("Access-Control-Allow-Origin", "*");

        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            createOpentracingSpan("UserController - subscriptions");
            List<UserVO> users = userService.subscriptions(userId);
            return getPaginateResponseResponseEntity(headers, paginateResponse, users);

        } catch (Exception e) {
            logger.error("Erreur durant l'execution de subscriptions: ", e);
            response.setStatus(500);
            paginateResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            paginateResponse.setRetDescription(WebServiceResponseCode.ERROR_PAGINATE_RESPONSE_LABEL);
            return new ResponseEntity<>(paginateResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = "User subscribers ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully subscription list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful Subscribers list ",
                    response = ResponseEntity.class, responseContainer = "Object")})
    @RequestMapping(value = USER_SUBSCRIBER_WS, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<PaginateResponse> subscribers(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") @Valid Long userId) throws ValidationException, IOException {

        logger.info("subscribers request in");
        response.setHeader("Access-Control-Allow-Origin", "*");

        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            createOpentracingSpan("UserController - subscribers");
            List<UserVO> users = userService.subscribers(userId);
            return getPaginateResponseResponseEntity(headers, paginateResponse, users);

        } catch (Exception e) {
            logger.error("Erreur durant l'execution de subscribers: ", e);
            response.setStatus(500);
            paginateResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            paginateResponse.setRetDescription(WebServiceResponseCode.ERROR_PAGINATE_RESPONSE_LABEL);
            return new ResponseEntity<>(paginateResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            finishOpentracingSpan();
        }
    }

    @RequestMapping(value = USER_WS_LOGOUT, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response, @RequestParam  @Valid String username) throws IOException {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equalsIgnoreCase(username)) {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath(request.getContextPath());
                response.addCookie(cookie);
            }
        }

        Response pmResponse = new Response();
        pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
        pmResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);
        return new ResponseEntity<>(pmResponse, HttpStatus.OK);
    }



}
