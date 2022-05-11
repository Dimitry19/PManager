package cm.framework.ds.common;

import cm.travelpost.tp.common.session.SessionManager;
import cm.travelpost.tp.user.ent.service.UserService;
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

@Component
public abstract class CustomOncePerRequestFilter  extends OncePerRequestFilter {


	@Autowired
	protected UserService userService;
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

	@Autowired
	protected SessionManager sessionManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

	}
}
