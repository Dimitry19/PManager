package cm.travelpost.tp.configuration.filters;


import cm.travelpost.tp.common.exception.ErrorResponse;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.security.jwt.service.JwtService;
import cm.travelpost.tp.security.jwt.utils.JwtTokenUtils;
import cm.travelpost.tp.user.ent.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cm.travelpost.tp.constant.WSConstants.*;


@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter extends CommonFilter {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public static final String JWT_AUTHORIZATION_HEADER ="Authorization";
    public static final String JWT_AUTHORIZATION_BEARER ="Bearer ";

    @Autowired
    JwtService jwtService;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    UserService userService;




    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("########## Initiating AuthenticationFilter filter ##########");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

        try {
            if(postman){
                filterChain.doFilter(request, response);
                return;
            }
            if(!authorized(servletRequest,  servletResponse)){
                  return;
              }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //call next filter in the filter chain
        filterChain.doFilter(request, response);

        logger.info("Logging Response :{}", response.getContentType());

    }

    @Override
    public void destroy() {

    }

    private boolean authorized(ServletRequest servletRequest,ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri=request.getRequestURI();
        String apiKey=request.getHeader(tokenName);
        boolean isService=uri.contains(service);
        boolean isConfirm=uri.contains(USER_WS_CONFIRMATION);
        boolean isNotApiKey=(StringUtils.isEmpty(apiKey)|| !apiKey.equals(token));
        boolean isNotUpload=!uri.contains(WSConstants.UPLOAD);
        boolean isDashBoard=uri.contains(WSConstants.DASHBOARD);
        String username=request.getHeader(sessionHeader);
        boolean isAdminRole=false;

      /*  if(StringUtils.isNotEmpty(username)){
            // Ici  je verifie si l'utilisateur a le role ADMIN pour pouvoir acceder à la dashboard
            UserVO user=userService.findByUsername(username, Boolean.FALSE);
            isAdminRole = CollectionsUtils.size(user.getRoles()
                    .stream()
                    .map(RoleVO::getDescription)
                    .filter(r->r==RoleEnum.ADMIN).collect(Collectors.toList()))!=0;
        }*/

        if((!isConfirm && isService && isNotApiKey && isNotUpload)){

           return validate(request,response);
        }
        return true;
    }


    private boolean validate( HttpServletRequest request, HttpServletResponse response) throws Exception {

        String uri=request.getRequestURI();

        String apiKey=request.getHeader(tokenName);
        String username=request.getHeader(sessionHeader);

        boolean isApiKey=(StringUtils.isNotEmpty(apiKey) && apiKey.equals(token));
        boolean isService=uri.contains(service) && isApiKey;

        boolean isLogout=uri.contains(USER_WS_LOGOUT);
        boolean isRegister=uri.contains(USER_WS_REGISTRATION);
        boolean isFind=uri.contains(FIND);

        boolean isAuthenticate=uri.contains(AUTH);

        boolean isServiceLogout=isService && isLogout;


        if(StringUtils.equals(username,guest) || isServiceLogout || isRegister || isFind || isAuthenticate){

            return Boolean.TRUE;
        }

         return  tokenValidator(request,response);

       /*
          UserVO user = (UserVO)sessionManager.getFromSession(username+apiKey);

          if(user==null) return Boolean.FALSE;

            String tk= (String)sessionManager.getFromSession(user.getUsername());

        return tokenUtils.validateToken(apiKey,tk);
        */

    }


    protected boolean tokenValidator(HttpServletRequest request, HttpServletResponse response ) throws IOException {

        final String requestTokenHeader = request.getHeader(JWT_AUTHORIZATION_HEADER);

        String username = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith(JWT_AUTHORIZATION_BEARER)) {

            jwtToken = requestTokenHeader.substring(7);

            try {
                username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
                request.setAttribute("exception", e);
                error(response, "Verifier les données d'accès (username:password)");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
                request.setAttribute("exception", e);
                error(response,"Votre token de connexion a expiré, Veuillez vous reconnecter");
            }
        }

        //Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.jwtService.loadUserByUsername(username);

            // if token is valid configure Spring Security to manually set authentication
            if (BooleanUtils.isTrue(jwtTokenUtils.validationToken(jwtToken, userDetails))) {

                UsernamePasswordAuthenticationToken usernamePassAuthToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePassAuthToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePassAuthToken);
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    public void error(HttpServletResponse response, String errorMessage) throws IOException {

        ErrorResponse errorResponse = new ErrorResponse();
        String[] codes=new String[1];
        codes[0]= "401";
        List<String> details= new ArrayList();
        details.add(errorMessage);
        errorResponse.setCode(codes);
        errorResponse.setDetails(details);
        errorResponse.setMessage(errorMessage);

        byte[] responseToSend = restResponseBytes(errorResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.getOutputStream().write(responseToSend);
    }
}
