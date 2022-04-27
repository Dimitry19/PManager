package cm.travelpost.tp.ws.requests.users.otp;

import lombok.Data;

@Data
public class CodeVerificationDTO {

    private String username;
    private String code;
}
