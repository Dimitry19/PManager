package cm.travelpost.tp.ws.requests.users.otp;

import lombok.Data;

@Data
public class VerificationDTO {

    private String username;
    private String code;
}
