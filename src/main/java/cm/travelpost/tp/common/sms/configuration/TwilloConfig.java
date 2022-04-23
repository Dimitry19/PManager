package cm.travelpost.tp.common.sms.configuration;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilloConfig {


    @Value("${twillo.account.sid}")
    private  String account_sid ;//="AC515f54b72001580033718870009d177b";

    @Value("${twillo.account.token}")
    private  String auth_token ;//= "8f6e5efce6ed7c8d392f2e75d76c9899";

    @Value("${twillo.account.from}")
    private  String from_number;//= "+19379143415";

    private String from;



    public void init(){
        Twilio.init(account_sid, auth_token);
        this.from = from_number;
    }

    public String getFrom() {
        return from;
    }
}
