package cm.travelpost.tp.configuration.filters;



import cm.travelpost.tp.common.exception.ErrorResponse;
import cm.travelpost.tp.common.session.SessionManager;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.user.ent.service.UserService;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.common.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static cm.travelpost.tp.constant.WSConstants.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter extends OncePerRequestFilter implements IFilter {



    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);


    @Autowired
    UserService userService;

    @Value("${custom.user.guest}")
    private String guest;

    @Value("${custom.api.auth.http.tokenValue}")
    protected String token;

    @Value("${custom.api.auth.http.tokenName}")
    protected String tokenName;

    @Value("${custom.session.user}")
    protected String sessionHeader;

    @Value("${url.service}")
    protected String service;

    @Autowired
    protected SessionManager sessionManager;

    @Value("${jwt.expirationDateInMs}")
    private int jwtExpirationInMs;

    @Value("${tp.travelpost.app.escape.announces}")
    private String escapeAnnounce;

    @Value("${tp.travelpost.app.escape.other}")
    private String escapeOther;

    @Value("${tp.travelpost.app.escape.home}")
    private String escapeHome;

    @Value("${tp.travelpost.postman.enable}")
    private boolean postman;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        try{
            // JWT Token is in the form "Bearer token". Remove Bearer word and
            // get  only the Token
            logger.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());


            if(!validate(request) && !postman){
                error(response);
            }else{
                filterChain.doFilter(request, response);
            }
        }catch(ExpiredJwtException ex) {
            request.setAttribute("exception", ex);
            error(response);
            //throw ex;
        }catch(BadCredentialsException ex){
            request.setAttribute("exception", ex);
            error(response);
            //throw ex;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void error(HttpServletResponse response) throws IOException {

        logger.error("Into Invalid token");

        ErrorResponse errorResponse = new ErrorResponse();
        String[] codes=new String[1];
        codes[0]= String.valueOf(HttpStatus.SC_GATEWAY_TIMEOUT);
        List<String> details= new ArrayList();
        details.add("Session expiree, se connecter de nouveau ");
        errorResponse.setCode(codes);
        errorResponse.setDetails(details);
        errorResponse.setMessage(details.get(0));

        byte[] responseToSend = restResponseBytes(errorResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpStatus.SC_GATEWAY_TIMEOUT);
        response.getOutputStream().write(responseToSend);
    }

    private byte[] restResponseBytes(ErrorResponse errorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(errorResponse);
        return serialized.getBytes();
    }

    private boolean validate(HttpServletRequest request) throws Exception {

        String uri=request.getRequestURI();

        String apiKey=request.getHeader(tokenName);
        String username=request.getHeader(sessionHeader);

        boolean isApiKey=(StringUtils.isNotEmpty(apiKey) && apiKey.equals(token));
        boolean isService=uri.contains(service) && isApiKey;

        boolean isLogout=uri.contains(USER_WS_LOGOUT);
        boolean isLogin=uri.contains(USER_WS_LOGIN);
        boolean isRegister=uri.contains(USER_WS_REGISTRATION);
        boolean isFind=uri.contains(FIND);


        boolean isServiceLogin=isService && isLogin;
        boolean isServiceLogout=isService && isLogout;

        if(StringUtils.equals(username,guest)){
            return Boolean.TRUE;
        }

        UserVO user = (UserVO)sessionManager.getFromSession(username+apiKey);

        if(isServiceLogin){

                 user=userService.findByUsername(username, Boolean.FALSE);

                if(user!=null){

                    UserDetails userDetails = new User(user.getUsername(), "", CommonFilter.getRolesAuthoritiesUser(user));
                    String  tokenGen=generateToken(userDetails);
                    sessionManager.addToSession(user.getUsername(),tokenGen);
                    sessionManager.addToSession(username+apiKey,user);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
        }

        if(isServiceLogout){
            sessionManager.removeToSession(user.getUsername());
            return Boolean.TRUE;
        }

        if(isService && !isRegister && !isServiceLogin && !isServiceLogout && !isFind){

            if(user==null) return Boolean.FALSE;

            String tk= (String)sessionManager.getFromSession(user.getUsername());

            return validateToken(apiKey,tk);


        }
        return Boolean.TRUE;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        if (roles.contains(new SimpleGrantedAuthority(CommonUtils.ROLE_ADMIN))) {
            claims.put(CommonUtils.claimsAdminKey, true);
        }
        if (roles.contains(new SimpleGrantedAuthority(CommonUtils.ROLE_USER))) {
            claims.put(CommonUtils.claimsUserKey, true);
        }

        return doGenerateToken(claims, userDetails.getUsername());
    }




    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, token).compact();

    }

    public boolean validateToken(String apiKey, String authToken) {
        try {

            if((StringUtils.isNotEmpty(authToken) && apiKey.equals(token))){
                Jws<Claims> claims = Jwts.parser().setSigningKey(token).parseClaimsJws(authToken);
                return claims!=null;
            }
            return false;


        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Controler les données d'accès {username , mot de passe}", ex);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(token).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(token).parseClaimsJws(token).getBody();

        List<SimpleGrantedAuthority> roles = null;

        Boolean isAdmin = claims.get(CommonUtils.claimsAdminKey, Boolean.class);
        Boolean isUser = claims.get(CommonUtils.claimsUserKey, Boolean.class);

        if (isAdmin != null && isAdmin) {
            roles = Arrays.asList(new SimpleGrantedAuthority(CommonUtils.ROLE_ADMIN));
        }

        if (isUser != null && isAdmin) {
            roles = Arrays.asList(new SimpleGrantedAuthority(CommonUtils.ROLE_USER));
        }
        return roles;
    }


}
