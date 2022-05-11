package cm.travelpost.tp.ws.controller.rest.users;


import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.common.ent.vo.WSCommonResponseVO;
import cm.framework.ds.hibernate.enums.CountBy;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.exception.UserNotFoundException;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.security.PasswordGenerator;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.controller.RedirectType;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.mail.MailDTO;
import cm.travelpost.tp.ws.requests.users.*;
import cm.travelpost.tp.ws.responses.PaginateResponse;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@RestController
@RequestMapping(WSConstants.USER_WS)
@Api(value = "user-service", description = "User Operations",tags ="user" )
public class UserController extends CommonController {

    protected final Logger logger = LoggerFactory.getLogger(UserController.class);



    @Value("${tp.travelpost.active.registration.enable}")
    protected boolean enableAutoActivateRegistration;



    @ApiOperation(value = "Register an user ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful registration",
                    response = Response.class, responseContainer = "Object")})
    @PostMapping(path = WSConstants.USER_WS_REGISTRATION, consumes = {MediaType.APPLICATION_JSON}, produces = {MediaType.APPLICATION_JSON}, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Response> register(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid RegisterDTO register) throws ValidationException, IOException {

        logger.info("register request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

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

                if (enableAutoActivateRegistration){
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_ACTIVE_LABEL);
                    response.setStatus(200);
                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);
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

            }
        } catch (Exception e) {
            logger.error("Erreur durant l'execution de register: ", e);
            return getResponseMailResponseEntity(pmResponse, e,"Votre compte sera activé d'ici 24h");
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
    @GetMapping(path = WSConstants.USER_WS_CONFIRMATION, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody void confirmation(HttpServletResponse response, HttpServletRequest request, @RequestParam("token") String token) throws Exception {

        Response pmResponse = new Response();


        try {
            logger.info("confirm request in");
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

            createOpentracingSpan("UserController -confirmation");

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
                    pmResponse.setRetDescription(WebServiceResponseCode.USER_REGISTER_ACTIVE_LABEL);
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

    @ApiOperation(value = " Login user ", response = UserVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful registration",
                    response = UserVO.class, responseContainer = "Object")})
    //@RequestMapping(value = USER_WS_LOGIN, method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @PostMapping(path = WSConstants.USER_WS_LOGIN, consumes = {MediaType.APPLICATION_JSON}, produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Object> login(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginDTO login) throws Exception {
        logger.info("login request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        UserVO user = null;



        try {
            createOpentracingSpan("UserController - login");

            if (login != null) {
                user = userService.login(login);

                if (user != null) {
                    user.setRetCode(WebServiceResponseCode.OK_CODE);
                    user.setRetDescription(WebServiceResponseCode.LOGIN_OK_LABEL);
                    cookie(response,user.getUsername(),user.getPassword());
                    return new ResponseEntity<>(user, HttpStatus.OK);

                } else {
                    WSCommonResponseVO commonResponse = new WSCommonResponseVO();
                    commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    commonResponse.setRetDescription(WebServiceResponseCode.ERROR_LOGIN_LABEL);
                    return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            logger.error("Erreur durant l'execution de login: ", e);
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
    @GetMapping(value = WSConstants.ENABLE_NOTIFICATION_WS, produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<UserVO> manageNotification(HttpServletRequest request,
                                              HttpServletResponse response,
                                              @RequestParam("userId") @Valid Long userId,
                                              @RequestParam("enable") @Valid boolean enable) throws UserException {
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
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
    @PutMapping(value = WSConstants.USER_WS_UPDATE_ID, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<UserVO> update(HttpServletResponse response, HttpServletRequest request, @PathVariable long userId,
                                  @RequestBody @Valid UpdateUserDTO userDTO) throws UserException, ValidationException, IOException {

        logger.info("update user request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
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
                return new ResponseEntity<>(user, HttpStatus.OK);
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
    @PutMapping(value = WSConstants.USER_WS_PASSWORD_UPDATE_ID, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Response> editPassword(HttpServletResponse response, HttpServletRequest request, @PathVariable long userId,
                                          @RequestBody @Valid ManagePasswordDTO managePassword) throws UserException, ValidationException, IOException {

        logger.info("edit  user password  request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        try {
            createOpentracingSpan("UserController - edit password");
            Response pmresponse = new Response();

            if (managePassword != null) {
                boolean edited = userService.editPassword(userId, managePassword.getOldPassword(), managePassword.getNewPassword());
                if (edited) {
                    pmresponse.setRetCode(0);
                    pmresponse.setRetDescription(WebServiceResponseCode.UPDATE_PASSWORD_LABEL);
                }
                return new ResponseEntity<>(pmresponse, HttpStatus.OK);
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
    @RequestMapping(value = WSConstants.USER_WS_PASSWORD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Response> password(HttpServletResponse response, HttpServletRequest request, @RequestBody PasswordDTO password) throws Exception {


        logger.info("password request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        Response pmResponse = new Response();

        try {
            createOpentracingSpan("UserController -password");
            if (password != null) {

                if (userService.managePassword(password.getEmail())) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.RETRIVEVE_PASSWORD_LABEL + " " + password.getEmail());
                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);
                } else {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.ERROR_MAIL_SERVICE_UNAVAILABLE_LABEL);
                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            logger.error("Erreur durant la recuperation du mot de passe de l'utilisateur  " + password.toString() + "{}", e);
            return getResponseMailResponseEntity(pmResponse, e,"Veuillez reessayer dans quelques instants!");
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }

    @ApiOperation(value = " Update user role ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successfully updated user role",
                    response = ResponseEntity.class, responseContainer = "Object")})
    @PostMapping(value = WSConstants.USER_WS_ROLE, consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<UserVO> setRole(@RequestBody @Valid RoleToUserDTO roleToUser) throws Exception {

        logger.info("set role request in");

        try {
            createOpentracingSpan("UserController -setRole");
            if (userService.setRoleToUser(roleToUser)) {
                userService.findByEmail(roleToUser.getEmail());
                return new ResponseEntity<>(userService.findByEmail(roleToUser.getEmail()), HttpStatus.FOUND);
            } else {
                return new ResponseEntity<>(userService.findByEmail(roleToUser.getEmail()), HttpStatus.NOT_FOUND);
            }
        } catch (UserException e) {
            logger.error("Erreur durant l'ajournement  du role de l'utilisateur  " + roleToUser.toString() + "{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erreur durant l'ajournement  du role de l'utilisateur  " + roleToUser.toString() + "{}", e);
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
    @DeleteMapping(value = WSConstants.DELETE, headers = WSConstants.HEADER_ACCEPT)
    public Response delete(HttpServletResponse response, HttpServletRequest request, @Valid @RequestParam("id") Long id) throws UserException {
        logger.info("delete request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
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
    @RequestMapping(value = WSConstants.USER_WS_USER_ID, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT,produces = MediaType.APPLICATION_JSON)
   // @GetMapping(value = USER_WS_USER_ID, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<UserVO> infosUser(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") @Valid Long id) throws UserNotFoundException, IOException {
        try {
            createOpentracingSpan("UserController -getUser");
            logger.info("retrieve user request in");
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
            UserVO user = userService.getUser(id);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
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
    @RequestMapping(value = WSConstants.USER_WS_MAIL, method = RequestMethod.POST, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Response> sendEmail(HttpServletResponse response, HttpServletRequest request, @RequestBody MailDTO mail) throws MessagingException, IOException {

        logger.info("send mail request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        Response pmResponse = new Response();

        try {
            createOpentracingSpan("UserController - sendMail");
            if (mail != null) {
                if (mailSenderSendGrid.manageResponse(mailService.sendMail(mail, true))) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.MAIL_SENT_LABEL);
                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);
                } else {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_MAIL_SERVICE_UNAVAILABLE_LABEL, "Veuillez reessayez plutard , Merci!"));
                    return new ResponseEntity<>(pmResponse, HttpStatus.SERVICE_UNAVAILABLE);
                }

            }
        } catch (Exception e) {
            logger.error("Erreur durant l'execution de l'envoi du mail: ", e);
            return getResponseMailResponseEntity(pmResponse, e,"Veuillez reessayez plutard , Merci!");
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
    @GetMapping(value = WSConstants.USER_WS_USERS, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> users(HttpServletResponse response, HttpServletRequest request,
                                                  @Valid @Positive(message = "la page doit etre nombre positif") @RequestParam(required = false, defaultValue = WSConstants.DEFAULT_PAGE) int page,
                                                  @Valid @Positive(message = "Page size should be a positive number") @RequestParam(required = false, defaultValue = WSConstants.DEFAULT_SIZE) int size) throws Exception {


        logger.info("get all users request in");
        HttpHeaders headers = new HttpHeaders();
        PageBy pageBy = new PageBy(page, size);
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            createOpentracingSpan("UserController -users");
            int count = userService.count(null,null,pageBy);
            List<UserVO> users = userService.getAllUsers(pageBy);
            return getPaginateResponseResponseEntity(headers, paginateResponse,count, users);
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
    @PostMapping(value = WSConstants.USER_ADD_SUBSCRIBER_WS, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}, consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<Response> subscribe(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid SubscribeDTO subscribe) throws ValidationException, UserException,Exception {

        logger.info("subscribe request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

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
            return new ResponseEntity<>(pmResponse, HttpStatus.INTERNAL_SERVER_ERROR);

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
    @PostMapping(value = WSConstants.UNSUBSCRIBE_WS, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity<Response> unsubscribe(HttpServletRequest request, HttpServletResponse response,
                                                @RequestBody @Valid SubscribeDTO subscribe) throws ValidationException, Exception {

        logger.info("unsubscribe request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

        Response pmResponse = new Response();

        try {
            createOpentracingSpan("UserController - unsubscribe");

            if (subscribe != null) {

                if (subscribe.getSubscriberId().equals(subscribe.getSubscriptionId())) {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(WebServiceResponseCode.CONFLICT_SUBSCRIBE_LABEL);
                    response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                    return new ResponseEntity<>(pmResponse, HttpStatus.NOT_ACCEPTABLE);
                }
                userService.unsubscribe(subscribe);
                userService.unsubscribe(subscribe);

                pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                pmResponse.setRetDescription(WebServiceResponseCode.UNSUBSCRIBE_LABEL);
                response.setStatus(200);
                return new ResponseEntity<>(pmResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Erreur durant l'execution de  unsubscribe: ", e);
            pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            pmResponse.setRetDescription(WebServiceResponseCode.ERROR_UNSUBSCRIBE_LABEL);
            return new ResponseEntity<>(pmResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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
    @RequestMapping(value = WSConstants.USER_SUBSCRIPTION_WS, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<PaginateResponse> subscriptions(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") @Valid Long userId) throws ValidationException, IOException {

        logger.info("subscriptions request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            createOpentracingSpan("UserController - subscriptions");

            int count = userService.count(CountBy.SUBSCRIPTIONS,userId,null);
            List<UserVO> users = userService.subscriptions(userId);

            return getPaginateResponseResponseEntity(headers, paginateResponse, count,users);

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
    @RequestMapping(value = WSConstants.USER_SUBSCRIBER_WS, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<PaginateResponse> subscribers(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") @Valid Long userId) throws ValidationException, Exception {

        logger.info("subscribers request in");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            createOpentracingSpan("UserController - subscribers");

            int count= userService.count(CountBy.SUBSCRIBERS,userId,null);
            List<UserVO> users = userService.subscribers(userId);
            return getPaginateResponseResponseEntity(headers, paginateResponse,count, users);

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

    @RequestMapping(value = WSConstants.USER_WS_LOGOUT, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
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

//               cookie.setValue(null);
//               cookie.setMaxAge(0);
//               cookie.setPath(request.getContextPath());
//               response.addCookie(cookie);
//               pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
//               pmResponse.setRetDescription(WebServiceResponseCode.LOGOUT_OK_LABEL);
//               return new ResponseEntity<>(pmResponse, HttpStatus.OK);

               if (StringUtils.equals(cookie.getValue(),_cookie(user.getUsername(),user.getPassword()))) {

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
        Cookie cookie =new Cookie("_tps_2P_", _cookie(username,password));
        cookie.setSecure(true);
        cookie.setDomain(travelPostDomain);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }
    private String _cookie(String username, String password){

        return PasswordGenerator.encrypt(username).concat(password.substring(0, password.length()-4).concat(password.substring(2,password.length()-1)));

    }

}
