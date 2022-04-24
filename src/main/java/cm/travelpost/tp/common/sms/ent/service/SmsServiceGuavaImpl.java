package cm.travelpost.tp.common.sms.ent.service;

import cm.travelpost.tp.common.sms.configuration.GuavaOTPConfig;
import cm.travelpost.tp.ws.requests.users.otp.SmsDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;


@Service("smsServiceGuava")
@Transactional
public class SmsServiceGuavaImpl implements SmsService {

    @Autowired
    GuavaOTPConfig guavaOTPConfig;


    @Override
    public void send(SmsDTO sms) throws Exception {


        int otp = guavaOTPConfig.generateOTP(sms.getTo());
    }

    @Override
    public void receive(MultiValueMap<String, String> callBack) {

    }

    @Override
    public boolean validate(int otpCode) throws Exception {

        return false;
    }

    @Override
    public boolean validate(String username , int otpCode) throws Exception {

       int userOtpCode=  guavaOTPConfig.getOtp(username);

       boolean found= userOtpCode==otpCode;
       if(BooleanUtils.isTrue(found)){
           guavaOTPConfig.clearOTP(username);
       }

        return found;
    }
}
