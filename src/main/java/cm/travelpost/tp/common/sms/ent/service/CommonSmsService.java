package cm.travelpost.tp.common.sms.ent.service;

import cm.travelpost.tp.common.sms.configuration.RandomOtpGenerator;
import cm.travelpost.tp.common.sms.ent.dao.SmsDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonSmsService {

	protected  String otpPattern ="OTP Code : {0} Please verify this OTP in Travel Post Services";

	@Autowired
	protected SmsDAO dao;

	@Autowired
	protected RandomOtpGenerator generator;
}
