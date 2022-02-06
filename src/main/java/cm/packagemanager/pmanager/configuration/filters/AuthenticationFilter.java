package cm.packagemanager.pmanager.configuration.filters;


import cm.packagemanager.pmanager.common.exception.ErrorResponse;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println("ServletContext name is " + filterConfig.getServletContext());
        System.out.println("init() method is ended");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

       /* if(!authorized(servletRequest,  servletResponse)){
           return;
        }*/

            //call next filter in the filter chain
        filterChain.doFilter(request, response);

        logger.info("Logging Response :{}", response.getContentType());

    }

    @Override
    public void destroy() {

    }

    private byte[] restResponseBytes(ErrorResponse errorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(errorResponse);
        return serialized.getBytes();
    }

    private boolean authorized(ServletRequest servletRequest,ServletResponse servletResponse) throws IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String[] codes=new String[1];
        codes[0]="401";


        String apiKey=request.getHeader("AUTH_API_KEY");
        String calledUrl=request.getRequestURI();

        if(!calledUrl.contains("confirm")&&calledUrl.contains(service) && (StringUtils.isEmpty(apiKey)|| !apiKey.equals(token))){
            ErrorResponse errorResponse = new ErrorResponse();
            List<String> details= new ArrayList();
            errorResponse.setCode(codes);
            errorResponse.setDetails(details);
            errorResponse.setMessage("Unauthorized Access");

            byte[] responseToSend = restResponseBytes(errorResponse);
            ((HttpServletResponse) response).setHeader("Content-Type", "application/json");
            ((HttpServletResponse) response).setStatus(401);
            response.getOutputStream().write(responseToSend);
            return false;
        }

        return true;

    }
}
