package cm.travelpost.tp.ws.responses.authentication;

import cm.travelpost.tp.user.ent.vo.UserInfo;
import cm.travelpost.tp.user.ent.vo.UserVO;


public class AuthenticationResponse extends UserVO {

	private String accessToken;
	private boolean authenticated;


	public AuthenticationResponse() {
		super();
	}
	public AuthenticationResponse(String accessToken, boolean authenticated, UserInfo user) {
		this.accessToken = accessToken;
		this.authenticated = authenticated;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

}
