package cm.travelpost.tp.ws.responses;

import lombok.Getter;

@Getter
public class AuthenticationResponse {

    private String token;

    public AuthenticationResponse(String token){
        this.token=token;
    }
}
