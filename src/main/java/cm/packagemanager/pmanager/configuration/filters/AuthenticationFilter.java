package cm.packagemanager.pmanager.configuration.filters;


import cm.packagemanager.pmanager.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
//@WebFilter(urlPatterns = "/users")
public class AuthenticationFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Value("${custom.api.auth.http.tokenValue}")
    private String token;

    @Value("${url.service}")
    private String service;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("########## Initiating AuthenticationFilter filter ##########");
        // this method will be called by container while deployment


        System.out.println("init() method has been get invoked");
        System.out.println("Filter name is " + filterConfig.getFilterName());
        System.out.println("ServletContext name is" + filterConfig.getServletContext());
        System.out.println("init() method is ended");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());
/*
		String apiKey=request.getHeader("AUTH_API_KEY");
		String calledUrl=request.getRequestURI();

		if(!calledUrl.contains("confirm")&&calledUrl.contains(service) && (StringUtils.isEmpty(apiKey)|| !apiKey.equals(token))){
			ErrorResponse errorResponse = new ErrorResponse();
			List<String> details= new ArrayList<>();
			errorResponse.setCode(HttpStatus.UNAUTHORIZED);
			errorResponse.setDetails(details);
			errorResponse.setMessage("Unauthorized Access");

			byte[] responseToSend = restResponseBytes(errorResponse);
			((HttpServletResponse) response).setHeader("Content-Type", "application/json");
			((HttpServletResponse) response).setStatus(401);
			response.getOutputStream().write(responseToSend);
			return;
		}
*/
        //call next filter in the filter chain
        filterChain.doFilter(request, response);

        logger.info("Logging Response :{}", response.getContentType());

    }

    @Override
    public void destroy() {

    }

    private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
        return serialized.getBytes();
    }
}
