package cm.travelpost.tp.user.ent.service.otp;

import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.user.ent.vo.otp.OPTNumber;
import cm.travelpost.tp.ws.requests.users.otp.SmsDTO;
import com.twilio.Twilio;

import java.text.ParseException;

import org.springframework.util.MultiValueMap;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("smsService")
@Transactional
public class SmsServiceImpl implements SmsService{

	private final String ACCOUNT_SID ="AC515f54b72001580033718870009d177b";

	private final String AUTH_TOKEN = "8f6e5efce6ed7c8d392f2e75d76c9899";

	private final String FROM_NUMBER = "+19379143415";

	@Override
	public void send(SmsDTO sms)  throws ParseException {

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		int min = 100000;
		int max = 999999;
		int number=(int)(Math.random()*(max-min+1)+min);

		String msg ="OTP Code : "+number+ " Please verify this OTP in Travel Post Services";

		Message message = Message.creator(new PhoneNumber(sms.getTo()), new PhoneNumber(FROM_NUMBER), msg).create();


        long millis = DateUtils.currentDate().getTime();
        System.out.println(millis);
		OPTNumber.setOtp(number);
        System.out.println("here is my id:"+message.getSid());// Unique resource ID created to manage this transaction

	}

	@Override
	public void receive(MultiValueMap<String, String> callBack) {

	}
}
