package cm.framework.ds.common.authentication.service;

import dev.samstevens.totp.exceptions.QrGenerationException;

public interface AuthenticationService {

	public  String qrCodeGenerator(String label, String secret, String issuer) throws QrGenerationException;

	public boolean verifyCode(String code, String secret);
}
