package cm.packagemanager.pmanager.configuration.filters;



import cm.packagemanager.pmanager.common.exception.ErrorResponse;
import cm.packagemanager.pmanager.common.session.SessionManager;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.common.utils.CommonUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.user.ent.service.UserService;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.BooleanUtils;
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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


import static cm.packagemanager.pmanager.common.utils.CommonUtils.ROLE_ADMIN;
import static cm.packagemanager.pmanager.common.utils.CommonUtils.ROLE_USER;
import static cm.packagemanager.pmanager.constant.WSConstants.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter extends OncePerRequestFilter implements IFilter {



    private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);

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


    @Autowired
    UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        try{
            // JWT Token is in the form "Bearer token". Remove Bearer word and
            // get  only the Token
            logger.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

            if(!validate(request)){
                error(response);
            }else{
                filterChain.doFilter(request, response);
            }
        }catch(ExpiredJwtException ex)
        {
            request.setAttribute("exception", ex);
            error(response);
            //throw ex;
        }
        catch(BadCredentialsException ex){
            request.setAttribute("exception", ex);
            error(response);
            //throw ex;

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void error(HttpServletResponse response) throws IOException {

        String[] codes=new String[1];
        codes[0]= "401";
        ErrorResponse errorResponse = new ErrorResponse();
        List<String> details= new ArrayList();
        errorResponse.setCode(codes);
        errorResponse.setDetails(details);
        errorResponse.setMessage("Token invalide , se connecter de nouveau");

        byte[] responseToSend = restResponseBytes(errorResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(401);
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

        boolean isServiceLogin=isService && isLogin;
        boolean isServiceLogout=isService && isLogout;

        UserVO user = (UserVO)sessionManager.getFromSession(username+apiKey);

        if(isServiceLogin){

                 user=userService.findByUsername(username, Boolean.FALSE);

                if(user!=null){

                    UserDetails userDetails = new User(user.getUsername(), "",getRolesAuthoritiesUser(user));
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

            return Boolean.TRUE;
        }

        if(isService && !isRegister && !isServiceLogin && !isServiceLogout){

            if(user==null) return Boolean.FALSE;

            String tk= (String)sessionManager.getFromSession(user.getUsername());

            return validateToken(apiKey,tk);


        }
        return Boolean.TRUE;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        if (roles.contains(new SimpleGrantedAuthority(ROLE_ADMIN))) {
            claims.put("isAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority(ROLE_USER))) {
            claims.put("isUser", true);
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
            throw new BadCredentialsException("Controler les credentials", ex);
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

        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Boolean isUser = claims.get("isUser", Boolean.class);

        if (isAdmin != null && isAdmin) {
            roles = Arrays.asList(new SimpleGrantedAuthority(ROLE_ADMIN));
        }

        if (isUser != null && isAdmin) {
            roles = Arrays.asList(new SimpleGrantedAuthority(ROLE_USER));
        }
        return roles;
    }

    public List getRolesAuthoritiesUser(UserVO user){

        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        if(CollectionsUtils.isUnique(user.getRoles())){
            RoleVO role = (RoleVO)CollectionsUtils.getFirstOrNull(user.getRoles());
            return Arrays.asList(new SimpleGrantedAuthority(CommonUtils.decodeRole(role)));
        }
        user.getRoles().stream().forEach(r->roles.add(new SimpleGrantedAuthority(CommonUtils.decodeRole(r))));

        return roles;
    }
}
