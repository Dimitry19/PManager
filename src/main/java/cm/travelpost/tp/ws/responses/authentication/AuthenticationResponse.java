package cm.travelpost.tp.ws.responses.authentication;

import cm.travelpost.tp.user.ent.vo.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AuthenticationResponse {


	private String accessToken;
	private boolean authenticated;
	private UserInfo user;
}
