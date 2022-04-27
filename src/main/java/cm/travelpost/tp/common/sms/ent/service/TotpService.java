package cm.travelpost.tp.common.sms.ent.service;

public interface TotpService {

    String generateSecret();
    String getUriForImage(String secret);
    boolean verifyCode(String code, String secret);
}
