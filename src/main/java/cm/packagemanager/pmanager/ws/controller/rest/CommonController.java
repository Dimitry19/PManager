package cm.packagemanager.pmanager.ws.controller.rest;

import cm.packagemanager.pmanager.common.exception.ResponseException;
import cm.packagemanager.pmanager.constant.WSConstants;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

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
		GlobalTracer.register(gTracer);
	}


	@ExceptionHandler({ ResponseException.class})

	/*@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(Exception e)
	{
		if (e instanceof ResponseException)	{
			return e.getMessage();
		}else{
			logger.error("Error", e);
		}
		return INTERNAL_SERVER_ERROR;
	}*/



	@RequestMapping(value = "/", method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public String goToHomePage() {
		return redirect;
	}

	/*@ResponseStatus(HttpStatus.BAD_REQUEST)
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

	*/
	protected void createOpentracingSpan(String spanName){
		packageManagerSpan = GlobalTracer.get().buildSpan(spanName).start();
	}
	protected void finishOpentracingSpan(){
		if (packageManagerSpan!=null){
			packageManagerSpan.finish();
		}
	}
}
