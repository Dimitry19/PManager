package cm.travelpost.tp.common.sms.configuration;

import com.twilio.Twilio;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilloConfig {

    private final String account_sid ="AC515f54b72001580033718870009d177b";

    private final String auth_token = "8f6e5efce6ed7c8d392f2e75d76c9899";

    private final String from_number = "+19379143415";

    private String from;



    public void init(){
        Twilio.init(account_sid, auth_token);
        this.from = from_number;
    }

    public String getFrom() {
        return from;
    }
}
