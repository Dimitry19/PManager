package cm.travelpost.tp.user.ent.service.otp;

import cm.travelpost.tp.user.ent.vo.otp.SmsVO;
import cm.travelpost.tp.ws.requests.users.otp.SmsDTO;
import org.springframework.util.MultiValueMap;

import java.text.ParseException;


public interface SmsService {

	void send(SmsDTO sms)  throws ParseException;

	void receive(MultiValueMap<String,String> callBack);
}
