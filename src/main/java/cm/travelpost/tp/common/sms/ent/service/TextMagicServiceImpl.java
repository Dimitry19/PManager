package cm.travelpost.tp.common.sms.ent.service;

import cm.travelpost.tp.common.sms.ent.vo.SmsOTPVO;
import cm.travelpost.tp.common.sms.ent.vo.TxtMagicMessage;
import cm.travelpost.tp.common.sms.ent.vo.TxtMagicVO;
import com.textmagic.sdk.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

@Service
public class TextMagicServiceImpl  extends  CommonSmsService implements TextMagicService{

	private static Logger logger = LoggerFactory.getLogger(TextMagicServiceImpl.class);



	@Resource(name = "tmClient")
	TxtMagicMessage client;

	@Override
	public void otp(List<String> destinations) throws Exception {
		int number = generator.generate();
		String msg = MessageFormat.format(otpPattern, number);
		send(msg,destinations);

		SmsOTPVO sms = new SmsOTPVO();
		sms.setOtp(number);
		sms.setPhoneNumber(destinations.get(0));
		dao.saves(sms);
	}

	@Override
	public void send(String message, List<String> destinations) throws Exception {

		try {
			client.setText(message);
			client.setPhones(destinations);
			client.send();
		} catch (final RestException e) {
			logger.error("Erreur survenue durant l'envoi du SMS {}", e.getErrors());
			throw new RuntimeException(e);
		}
		logger.info("Client id {}",client.getId());
	}

	@Override
	public void send(TxtMagicVO txtMagic) throws Exception {

		if (txtMagic!=null){
			 send(txtMagic.getMessage(),txtMagic.getDestinations());
		}
	}
}
