package cm.framework.ds.common.authentication.service;


import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

	@Autowired
	private QrDataFactory qrDataFactory;

	@Autowired
	private QrGenerator qrGenerator;

	@Autowired
	private CodeVerifier verifier;

	@Override
	public String qrCodeGenerator(String label, String secret, String issuer) throws QrGenerationException {
		QrData data = qrDataFactory
				.newBuilder()
				.label(label)
				.secret(secret)
				.issuer(issuer).build();

		return getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());
	}

	@Override
	public boolean verifyCode(String code, String secret) {

		return verifier.isValidCode(secret, code);
	}
}
