package cm.packagemanager.pmanager.ws.controller.rest;

import cm.packagemanager.pmanager.api.ApiError;
import cm.packagemanager.pmanager.common.exception.ResponseException;
import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
import cm.packagemanager.pmanager.constant.WSConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.MappedSuperclass;
import javax.servlet.ServletContext;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*L'annotation @CrossOrigin(origins = "http://localhost:8080", maxAge = 3600) permet de favoriser une communication distante entre le client et le serveur,
		c'est-à-dire lorsque le client et le serveur sont déployés dans deux serveurs distincts, ce qui permet d'éviter des problèmes réseaux.*/
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommonController {


	protected final Log logger = LogFactory.getLog(CommonController.class);
	protected static final String INTERNAL_SERVER_ERROR="internal server error";

	/************ USER REQUEST*************/
	protected static final String USER_WS="/ws/user/*";
	protected static final String USER_WS_REGISTRATION="/register";
	protected static final String USER_WS_CONFIRMATION="/confirm";
	protected static final String USER_WS_LOGIN="/ulogin";
	protected static final String USER_WS_USERS="/users";
	protected static final String USER_WS_USERS_PAGE_NO="/users/{pageno}";
	protected static final String USER_WS_MAIL="/mail";
	protected static final String USER_WS_DELETE_USER= "/delete/user/{userId}";
	protected static final String USER_WS_ROLE="/role";
	protected static final String USER_WS_UPDATE_ID="/update/{id}";
	protected static final String USER_WS_UPDATE="/update";
	protected static final String USER_WS_PASSWORD="/password";
	protected static final String USER_WS_USER_ID="/info/{id}";


	/************ ANNOUNCE REQUEST*************/
	protected static final String ANNOUNCE_WS="/ws/announce/*";
	protected static final String ANNOUNCE_WS_CREATE="/create";
	protected static final String ANNOUNCE__WS_CONFIRMATION="/confirm";
	protected static final String ANNOUNCE__WS_LOGIN="/ulogin";
	protected static final String ANNOUNCE__WS_USERS="/users";
	protected static final String ANNOUNCE__WS_USERS_PAGE_NO="/users/{pageno}";
	protected static final String ANNOUNCE__WS_MAIL="/mail";
	protected static final String ANNOUNCE__WS_DELETE_USER= "/delete/user/{userId}";
	protected static final String ANNOUNCE__WS_ROLE="/role";
	protected static final String ANNOUNCE__WS_UPDATE_ID="/update/{id}";







	@Autowired
	protected ServletContext servletContext;


	@Value("${pagination.size}")
	protected Integer size;

	@Value("${ws.redirect.user}")
	protected String redirect;

	//@ExceptionHandler({ ResponseException.class, Exception.class })

	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(Exception e)
	{
		if (e instanceof ResponseException)	{
			return e.getMessage();
		}else{
			logger.error("Error", e);
		}
		return INTERNAL_SERVER_ERROR;
	}




	@RequestMapping(value = "/", method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public String goToHomePage() {
		return redirect;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public List<ApiError> handleValidationExceptions(ConstraintViolationException ex) {
		return ex.getConstraintViolations()
				.stream()
				.map(err -> new ApiError(err.getPropertyPath().toString(), err.getMessage()))
				.collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public List<ApiError> handleNotFoundExceptions(UserNotFoundException ex) {
		return Collections.singletonList(new ApiError("user.notfound", ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public List<ApiError> handleOtherException(Exception ex) {
		return Collections.singletonList(new ApiError(ex.getClass().getCanonicalName(), ex.getMessage()));
	}



}