package cm.travelpost.tp.ws.responses.authentication;

import cm.travelpost.tp.user.ent.vo.UserInfo;


public class AuthenticationResponse {

	private String accessToken;
	private boolean authenticated;
	private UserInfo user;

	public AuthenticationResponse(String accessToken, boolean authenticated, UserInfo user) {
		this.accessToken = accessToken;
		this.authenticated = authenticated;
		this.user = user;
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

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}
}
