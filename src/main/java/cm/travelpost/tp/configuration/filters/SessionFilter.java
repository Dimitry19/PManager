package cm.travelpost.tp.configuration.filters;


import cm.framework.ds.common.CustomOncePerRequestFilter;
import cm.travelpost.tp.common.utils.CommonUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.user.ent.vo.UserVO;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static cm.travelpost.tp.constant.WSConstants.*;

//@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@ManagedBean
public class SessionFilter extends CustomOncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);


    private  String decryptToken=null;

    @PostConstruct
    public void init(){
        this.decryptToken=encryptorBean.decrypt(token);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        try{

            if(logger.isDebugEnabled()){
                logger.debug("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());
            }

            if(postman || !enableFilter){
                filterChain.doFilter(request, response);
                return;
            }
            if(!validate(request)  && enableFilter){
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



    private boolean validate(HttpServletRequest request) throws Exception {

        String uri=request.getRequestURI();

        String decryptedToken=request.getHeader(encryptorBean.decrypt(tokenName));
        String apiKey=StringUtils.isNotEmpty(decryptedToken) ?encryptorBean.decrypt(decryptedToken):null;

        String username=request.getHeader(sessionHeader);

        boolean isApiKey=(StringUtils.isNotEmpty(apiKey) && apiKey.equals(decryptToken));
        boolean isService=uri.contains(service) && isApiKey;

        boolean isLogout=uri.contains(LOGOUT);
        boolean isLogin=uri.contains(USER_WS_LOGIN);
        boolean isRegister=uri.contains(REGISTRATION);
        boolean isFind=uri.contains(FIND);


        boolean isServiceLogin=isService && isLogin;
        boolean isServiceLogout=isService && isLogout;

        if(StringUtils.equals(username,encryptorBean.decrypt(guest))){
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

            if (user!=null){
                sessionManager.removeToSession(user.getUsername());
            }

            return Boolean.TRUE;
        }

        if(isService && !isRegister && !isServiceLogin && !isFind){

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
                .signWith(SignatureAlgorithm.HS512, decryptToken).compact();

    }

    public boolean validateToken(String apiKey, String authToken) {
        try {

            if((StringUtils.isNotEmpty(authToken) && apiKey.equals(decryptToken))){
                Jws<Claims> claims = Jwts.parser().setSigningKey(decryptToken).parseClaimsJws(authToken);
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
        Claims claims = Jwts.parser().setSigningKey(decryptToken).parseClaimsJws(token).getBody();
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
