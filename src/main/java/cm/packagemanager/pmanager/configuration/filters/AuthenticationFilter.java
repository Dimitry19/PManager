package cm.packagemanager.pmanager.configuration.filters;


import cm.packagemanager.pmanager.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cm.packagemanager.pmanager.constant.WSConstants.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter extends CommonFilter {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("########## Initiating AuthenticationFilter filter ##########");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

 /*       if(!authorized(servletRequest,  servletResponse)){
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

    private boolean authorized(ServletRequest servletRequest,ServletResponse servletResponse) throws IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri=request.getRequestURI();
        String apiKey=request.getHeader(tokenName);
        boolean isService=uri.contains(service);
        boolean isConfirm=uri.contains("confirm");
        boolean isNotApiKey=(StringUtils.isEmpty(apiKey)|| !apiKey.equals(token));
        boolean isNotUpload=!uri.contains(UPLOAD);

        if(!isConfirm && isService && isNotApiKey && isNotUpload){
            error(response,false);
            return false;
        }

        return true;
    }



}
