package cm.framework.ds.common;

import cm.travelpost.tp.common.exception.ErrorResponse;
import cm.travelpost.tp.common.session.SessionManager;
import cm.travelpost.tp.user.ent.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public abstract class CustomOncePerRequestFilter  extends OncePerRequestFilter  {


	@Autowired
	protected UserService userService;

	@Autowired
	protected SessionManager sessionManager;

	@Resource(name ="jasyptStringEncryptor")
	protected StringEncryptor encryptorBean;

	@Value("${jwt.expirationDateInMs}")
	protected int jwtExpirationInMs;

	@Value("${custom.api.auth.http.tokenValue}")
	protected String token;

	@Value("${custom.user.guest}")
	protected String guest;

	@Value("${custom.api.auth.http.tokenName}")
	protected String tokenName;

	@Value("${custom.session.user}")
	protected String sessionHeader;

	@Value("${url.service}")
	protected String service;

	@Value("${tp.travelpost.postman.enable}")
	protected boolean postman;
	@Value("${tp.travelpost.active.session.filter.enable}")
	protected boolean enableFilter;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

	}

	//@Override
	public void error(HttpServletResponse response) throws IOException {

		logger.error("Into Invalid token");

		ErrorResponse errorResponse = new ErrorResponse();
		String[] codes=new String[1];
		codes[0]= String.valueOf(HttpStatus.SC_GATEWAY_TIMEOUT);
		List<String> details= new ArrayList();
		details.add("Token expiré, se connecter de nouveau ");
		errorResponse.setCode(codes);
		errorResponse.setDetails(details);
		errorResponse.setMessage(details.get(0));

		byte[] responseToSend = restResponseBytes(errorResponse);
		response.setHeader("Content-Type", "application/json");
		response.setStatus(HttpStatus.SC_GATEWAY_TIMEOUT);
		response.getOutputStream().write(responseToSend);
	}

	private byte[] restResponseBytes(ErrorResponse errorResponse) throws IOException {
		String serialized = new ObjectMapper().writeValueAsString(errorResponse);
		return serialized.getBytes();
	}
}
