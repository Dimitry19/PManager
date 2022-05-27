package cm.framework.ds.common.authentication.filter;


import cm.framework.ds.common.CustomOncePerRequestFilter;
import cm.framework.ds.common.security.jwt.TokenProvider;
import cm.travelpost.tp.common.exception.TokenExpiredException;
import cm.travelpost.tp.common.utils.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static cm.travelpost.tp.constant.WSConstants.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ManagedBean
public class TokenAuthenticationFilter  extends CustomOncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

	@Autowired
	TokenProvider tokenProvider;

	private  String decryptedTokenName=null;
	private  String decryptedToken=null;

	@PostConstruct
	public void init(){
		this.decryptedToken=encryptorBean.decrypt(token);
		this.decryptedTokenName=encryptorBean.decrypt(tokenName);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {

			String uri =request.getRequestURI();
			String keyName= StringUtils.isNotEmpty(decryptedTokenName) ?decryptedTokenName:null;
			String apiKeyRequest=encryptorBean.decrypt(request.getHeader(keyName));

			String apiKey= StringUtils.isNotEmpty(apiKeyRequest) ?apiKeyRequest:"null";
			String username=request.getHeader(sessionHeader);

			boolean isApiKey=(StringUtils.isNotEmpty(apiKey) && apiKey.equals(decryptedToken));
			boolean isRegister=uri.contains(REGISTRATION) && isApiKey;
			boolean isLogout=uri.contains(LOGOUT);
			boolean isService=uri.contains(service) && isApiKey;
			boolean isFind=uri.contains(FIND);
			boolean isLogin=uri.contains(USER_WS_LOGIN);
			boolean isGuest=StringUtils.equals(username,encryptorBean.decrypt(guest)) && isService;
			boolean isServiceLogin=isLogin && isService;
			boolean isServiceLogout=isService && isLogout;
			boolean isOnlyService= !uri.contains(service);
			boolean isVerifyService=uri.contains(AUTHENTICATION_WS_VERIFICATION) && isService;

			boolean activate= isVerifyService || isOnlyService || isGuest || isRegister || isFind || isLogout || isServiceLogin || isServiceLogout;

			 if (BooleanUtils.isFalse(postman) && BooleanUtils.isFalse(activate)){
				 tokenProvider.setAuthentication(request);
			 }
		} catch (Exception ex) {
			log.error("Could not set user authentication in security context", ex);
			error(response);
			throw new TokenExpiredException("Token expiré, se connecter de nouveau");
		}

		filterChain.doFilter(request, response);
	}

}
