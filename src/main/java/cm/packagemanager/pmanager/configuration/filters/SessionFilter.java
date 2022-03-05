package cm.packagemanager.pmanager.configuration.filters;



import cm.packagemanager.pmanager.common.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import io.jsonwebtoken.Jwts;


import static cm.packagemanager.pmanager.constant.WSConstants.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter extends CommonFilter{

    private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Value("${jwt.expirationDateInMs}")
    private int jwtExpirationInMs;



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


    //https://www.javainuse.com/webseries/spring-security-jwt/chap7

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            claims.put("isAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            claims.put("isUser", true);
        }

        return doGenerateToken(claims, userDetails.getUsername());
    }


    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(token).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, token).compact();

    }
}
