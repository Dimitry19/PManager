package cm.framework.ds.common.security.jwt;


import cm.framework.ds.common.constants.Constants;
import cm.framework.ds.common.security.CommonSecurityResource;
import cm.travelpost.tp.common.session.SessionManager;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.CommonUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.configuration.filters.CommonFilter;
import cm.travelpost.tp.user.ent.service.UserService;
import cm.travelpost.tp.user.ent.vo.RoleVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@ManagedBean
public class TokenProvider  extends CommonSecurityResource {

	@Autowired
	protected SessionManager sessionManager;

	@Autowired
	UserService userService;
	protected   String decryptToken=null;

	private static final String AUTHENTICATED = "authenticated";


	@PostConstruct
	public void init(){
		this.decryptToken=encryptorBean.decrypt(token);
	}

	public String getDecryptToken() {
		return decryptToken;
	}

	public void setDecryptToken(String decryptToken) {
		this.decryptToken = decryptToken;
	}

	public void setAuthentication(String username) throws Exception {
		UserVO user=userService.findByUsername(username,false);
		if(user==null){
			return;
		}
		UserDetails userDetails = new User(username, "", getRolesAuthoritiesUser(user));
		String  tokenGen=generateToken(userDetails);
		sessionManager.addToSession(username,tokenGen);

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}

	public void setAuthentication(HttpServletRequest request) throws Exception {

		String jwt = getTokenFromRequest(request);

		if ( validateToken(jwt)) {
			String username =  getUsernameFromToken(jwt);

			UserVO user=userService.findByUsername(username,false);
			if(user==null){
				return;
			}
			UserDetails userDetails = new User(user.getUsername(), "", CommonFilter.getRolesAuthoritiesUser(user));

			Collection<? extends GrantedAuthority> authorities = isAuthenticated(jwt) ? userDetails.getAuthorities()
					: Arrays.asList(new SimpleGrantedAuthority(Constants.ROLE_PRE_VERIFICATION_USER));
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
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

	public String createToken(Long id, boolean authenticated) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + (authenticated ? jwtExpirationInMs: jwtShortExpirationInMs));

		return Jwts.builder().setSubject(Long.toString(id)).claim(AUTHENTICATED, authenticated).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, decryptToken).compact();
	}

	public String createToken(String username, boolean authenticated) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + (authenticated ? jwtExpirationInMs: jwtShortExpirationInMs));

		return Jwts.builder().setSubject(username).claim(AUTHENTICATED, authenticated).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, decryptToken).compact();
	}

	public Boolean isAuthenticated(String token) {
		Claims claims = Jwts.parser().setSigningKey(decryptToken).parseClaimsJws(token).getBody();
		return claims.get(AUTHENTICATED, Boolean.class);
	}

	public boolean validateToken(String authToken) {
		try {

			if((StringUtils.isNotEmpty(authToken))){
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

	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(decryptToken).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
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

	public List getRolesAuthoritiesUser(UserVO user){

		return getRoles(user);
	}

	@NotNull
	static List getRoles(UserVO user) {
		List<SimpleGrantedAuthority> roles = new ArrayList<>();

		if(CollectionsUtils.isUnique(user.getRoles())){
			RoleVO role = (RoleVO)CollectionsUtils.getFirstOrNull(user.getRoles());
			return Arrays.asList(new SimpleGrantedAuthority(CommonUtils.decodeRole(role)));
		}
		user.getRoles().stream().forEach(r->roles.add(new SimpleGrantedAuthority(CommonUtils.decodeRole(r))));

		return roles;
	}


	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}
