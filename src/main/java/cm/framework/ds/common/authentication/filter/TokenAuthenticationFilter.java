package cm.framework.ds.common.authentication.filter;

import cm.framework.ds.common.CustomOncePerRequestFilter;
import cm.framework.ds.common.security.jwt.TokenProvider;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.user.ent.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class TokenAuthenticationFilter  extends CustomOncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

	@Autowired
	TokenProvider tokenProvider;
	@Autowired
	UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {

			String tkName=request.getHeader(encryptorBean.decrypt(tokenName));
			String apiKey= StringUtils.isNotEmpty(tkName) ?encryptorBean.decrypt(tkName):null;
			tokenProvider.setAuthentication(request);
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}
}
