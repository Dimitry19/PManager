package cm.travelpost.tp.common.sms.ent.service;

import cm.travelpost.tp.common.sms.configuration.TwilloConfig;
import cm.travelpost.tp.common.sms.ent.vo.OPTNumber;
import cm.travelpost.tp.common.sms.ent.vo.SmsOTPVO;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.ws.requests.users.otp.SmsDTO;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.text.MessageFormat;


/**
 *
 */
@Service("smsService")
@Transactional
public class SmsServiceImpl extends  CommonSmsService implements SmsService {

	protected final Log logger = LogFactory.getLog(SmsServiceImpl.class);

	@Autowired
	TwilloConfig twilloConfig;



	@Override
	public void send(SmsDTO dto) throws Exception {

		twilloConfig.init();


		int number = generator.generate();
		PhoneNumber to = new PhoneNumber(dto.getTo());
		PhoneNumber from = new PhoneNumber(twilloConfig.getFrom());

		String msg  = MessageFormat.format(otpPattern, number);

		MessageCreator creator=Message.creator(to, from, msg);
		Message message =creator.create();

		SmsOTPVO sms = new SmsOTPVO();
		sms.setOtp(number);
		sms.setPhoneNumber(dto.getTo());
		dao.saves(sms);
        long millis = DateUtils.currentDate().getTime();
        System.out.println(millis);
		OPTNumber.setOtp(number);
		logger.info("here is my id:"+message.getSid());// Unique resource ID created to manage this transaction

	}

	@Override
	public void receive(MultiValueMap<String, String> callBack) {

	}

	@Override
	public boolean validate(int otpCode) throws Exception {
		SmsOTPVO sms= (SmsOTPVO)dao.findByUniqueResult(SmsOTPVO.OTP,SmsOTPVO.class,otpCode,"otp");
		boolean response = sms!=null;

		if (BooleanUtils.isTrue(response)){
			sms.cancel();
			dao.update(sms);
		}

		return response;
	}

	@Override
	public boolean validate(String username, int otpCode) throws Exception {
		return false;
	}
}
