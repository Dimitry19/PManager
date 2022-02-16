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
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static cm.packagemanager.pmanager.constant.WSConstants.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter extends CommonFilter{

    private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

        if(!validateSession(servletRequest,  servletResponse)){
            return;
        }

        //call next filter in the filter chain
        filterChain.doFilter(request, response);

        logger.info("Logging Response :{}", response.getContentType());

    }


    private boolean validateSession(ServletRequest servletRequest,ServletResponse servletResponse) throws IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri=request.getRequestURI();

        String apiKey=request.getHeader(tokenName);
        String user=request.getHeader(sessionHeader);

        boolean isApiKey=(StringUtils.isNotEmpty(apiKey) && apiKey.equals(token));
        boolean isService=uri.contains(service) && isApiKey;

        boolean isLogout=uri.contains(USER_WS_LOGOUT);
        boolean isLogin=uri.contains(USER_WS_LOGIN);
        boolean isRegister=uri.contains(USER_WS_REGISTRATION);

        boolean isServiceLogin=isService && isLogin;
        boolean isServiceLogout=isService && isLogout;

        HttpSession sessionObj = request.getSession(false);

        if(isServiceLogin){
            //check session exist or not if not available create new session
            if (sessionObj == null) {
                logger.info("Session not available, creating new session.");
                sessionObj = request.getSession(Boolean.TRUE);
                sessionManager.addToSession(user,sessionObj);
                return Boolean.TRUE;

            }
        }

        if(isServiceLogout){
            if (sessionObj == null) {
                logger.info("Session  available, invalidate  session.");
                sessionObj=(HttpSession)sessionManager.getFromSession(user);
            }
            if (sessionObj != null) {
                sessionObj.invalidate();

            }
            return Boolean.TRUE;

        }

        if(isService && !isRegister && !isServiceLogin && !isServiceLogout){

            sessionObj=(HttpSession)sessionManager.getFromSession(user);

            if (sessionObj == null) {
                logger.info("Session   not valide invalidate  session.");
                error(response,true);
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
