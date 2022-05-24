package cm.travelpost.tp.ws.responses.authentication;

import cm.framework.ds.common.ent.vo.WSCommonResponseVO;
import cm.travelpost.tp.user.ent.vo.UserInfo;


public class AuthenticationResponse extends WSCommonResponseVO {

	private String accessToken;
	private boolean authenticated;
	private String message;


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
