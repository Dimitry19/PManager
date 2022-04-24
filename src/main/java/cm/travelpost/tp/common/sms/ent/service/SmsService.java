package cm.travelpost.tp.common.sms.ent.service;

import cm.travelpost.tp.ws.requests.users.otp.SmsDTO;
import org.springframework.util.MultiValueMap;


public interface SmsService {

	void send(SmsDTO sms) throws Exception;

	void receive(MultiValueMap<String, String> callBack);

	boolean validate(int otpCode) throws Exception;

	boolean validate(String  username, int otpCode) throws Exception;
}
