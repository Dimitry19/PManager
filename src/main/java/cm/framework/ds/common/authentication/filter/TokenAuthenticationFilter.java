package cm.framework.ds.common.authentication.filter;

import cm.framework.ds.common.CustomOncePerRequestFilter;
import cm.framework.ds.common.security.jwt.TokenProvider;
import cm.travelpost.tp.common.utils.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cm.travelpost.tp.constant.WSConstants.REGISTRATION;

@Configuration
@ManagedBean
public class TokenAuthenticationFilter  extends CustomOncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

	@Autowired
	TokenProvider tokenProvider;

	private  String decryptedTokenName=null;
	private  String decryptToken=null;

	@PostConstruct
	public void init(){
		this.decryptToken=encryptorBean.decrypt(token);
		this.decryptedTokenName=encryptorBean.decrypt(tokenName);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {


			String apiKey= StringUtils.isNotEmpty(decryptedTokenName) ?decryptedTokenName:null;
			boolean isApiKey=(StringUtils.isNotEmpty(apiKey) && apiKey.equals(decryptToken));
			boolean isRegister=request.getRequestURI().contains(REGISTRATION) &&isApiKey;

			 if (BooleanUtils.isFalse(isRegister)){
				 tokenProvider.setAuthentication(request);
			 }

		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}
}
