package cm.travelpost.tp.security.jwt.utils;

import cm.travelpost.tp.common.utils.CommonUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;


@Component
public class JwtTokenUtils implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5*60*60;

    @Value("${custom.api.auth.http.tokenName}")
    private String secret;


    @Value("${jwt.expirationDateInMs}")
    private long jwtTokenValidity;


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
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity*1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String getUsernameFromToken(String token) {

        return getClaimFromToken(token, Claims::getSubject);
    }

    public Boolean validationToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public Date getIssuedAtDateFromToken(String token) {

        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }


    public boolean validateToken(String apiKey, String authToken) {
        try {

            if((StringUtils.isNotEmpty(authToken) && apiKey.equals(secret))){
                Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
                return claims!=null;
            }
            return false;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Controler les données d'accès {username , mot de passe}", ex);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }
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


    /* public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(token).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }*/
}
