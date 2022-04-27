package cm.travelpost.tp.ws.requests.users.otp;

import cm.travelpost.tp.ws.requests.users.LoginDTO;
import lombok.Data;

@Data
public class SignupDTO extends LoginDTO {


    private boolean mfa;


}
