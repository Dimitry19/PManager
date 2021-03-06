package cm.packagemanager.pmanager.ws.controller.rest;

import cm.packagemanager.pmanager.administrator.api.ApiError;
import cm.packagemanager.pmanager.common.exception.ResponseException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
import cm.packagemanager.pmanager.constant.WSConstants;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * L'annotation @CrossOrigin(origins = "http://localhost:8080", maxAge = 3600) permet de favoriser une communication distante entre le client et le serveur,
		c'est-à-dire lorsque le client et le serveur sont déployés dans deux serveurs distincts, ce qui permet d'éviter des problèmes réseaux.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Component
public class CommonController {



	protected final Log logger = LogFactory.getLog(CommonController.class);
	public static final String INTERNAL_SERVER_ERROR="internal server error";
	public static final String DEFAULT_SIZE = "12";
	public static final String DEFAULT_PAGE = "0";
	public static final String HEADER_TOTAL = "x-total-count";

	/************ USER REQUEST*************/
	public static final String USER_WS="/ws/user/*";
	public static final String USER_WS_REGISTRATION="/register";
	public static final String USER_WS_CONFIRMATION="/confirm";
	public static final String USER_WS_LOGIN="/ulogin";
	public static final String USER_WS_USERS="/users";
	public static final String USER_WS_USERS_PAGE_NO="/users/{pageno}";
	public static final String USER_WS_MAIL="/mail";
	public static final String USER_WS_DELETE_USER= "/delete";
	public static final String USER_WS_ROLE="/role";
	public static final String USER_WS_UPDATE_ID="/update/{id}";
	public static final String USER_WS_UPDATE="/update";
	public static final String USER_WS_PASSWORD="/password";
	public static final String USER_WS_USER_ID="/info/{id}";


	/************ ANNOUNCE REQUEST*************/
	public static final String ANNOUNCE_WS="/ws/announce/*";
	public static final String ANNOUNCES_WS="/announces";
	public static final String ANNOUNCE_WS_CREATE="/create";
	public static final String ANNOUNCE_WS_FIND="/find";
	public static final String ANNOUNCE_WS_USER_ID_PAGE_NO="/announces/{pageno}";
	public static final String ANNOUNCE_WS_DELETE= "/delete";
	public static final String ANNOUNCE_WS_BY_ID= "/announce";
	public static final String ANNOUNCE_WS_BY_USER= "/user";
	public static final String ANNOUNCE_WS_UPDATE="/update";
	public static final String ANNOUNCE_WS_ALL="/all";



	/************ MESSAGE REQUEST*************/
	public static final String MESSAGE_WS="/ws/message/*";
	public static final String MESSAGES_WS="/messages";
	public static final String WS_ADD_MESSAGE="/add";
	public static final String MESSAGE_WS_BY_USER= "/user";
	public static final String MESSAGE_WS_UPDATE="/update";
	public static final String MESSAGE_WS_DELETE= "/delete";
	public static final String MESSAGE_WS_FIND="/find";
	public static final String MESSAGE_WS_USER_ID_PAGE_NO="/messages/{pageno}";
	public static final String MESSAGE_WS_ALL="/all";


	/************ ROLE REQUEST*************/
	public static final String ROLE_WS="/ws/role/*";
	public static final String ROLE_WS_ADD="/add";
	public static final String ROLES_WS="/roles";
	public static final String ROLE_WS_USER_ID_PAGE_NO="/messages/{pageno}";
	public static final String ROLE_WS_DELETE= "/delete";
	public static final String ROLE_WS_UPDATE="/update";



	/************ REVIEW REQUEST*************/
	public static final String REVIEW_WS="/ws/review/*";
	public static final String REVIEW_WS_ADD="/add";
	public static final String REVIEWS_WS="/reviews";
	public static final String REVIEW_WS_DELETE= "/delete";
	public static final String REVIEW_WS_UPDATE="/update";



	@Autowired
	public ServletContext servletContext;

	@Qualifier("jaegerTracer")
	@Autowired
	protected Tracer gTracer;

	@Value("${pagination.size}")
	public Integer size;

	@Value("${ws.redirect.user}")
	public String redirect;

	protected Span packageManagerSpan;


	@PostConstruct
	public void init() {
		System.out.println("CommonController  starts...." );
		GlobalTracer.register(gTracer);
	}


	@ExceptionHandler({ ResponseException.class})

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
	@ExceptionHandler({UserNotFoundException.class})
	public List<ApiError> handleNotFoundExceptions(UserNotFoundException ex) {
		return Collections.singletonList(new ApiError("user.notfound", ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler({UserException.class})
	public List<ApiError> handleUserErrorExceptions(UserException ex) {
		return Collections.singletonList(new ApiError("user.error", ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({Exception.class,ValidationException.class})
	public List<ApiError> handleOtherException(Exception ex) {
		return Collections.singletonList(new ApiError(ex));
	}

	protected void createOpentracingSpan(String spanName){
		packageManagerSpan = GlobalTracer.get().buildSpan(spanName).start();
	}


	protected void finishOpentracingSpan(){
		if (packageManagerSpan!=null){
			packageManagerSpan.finish();
		}
	}
}
