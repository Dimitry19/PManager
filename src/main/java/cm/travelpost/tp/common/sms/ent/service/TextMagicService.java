package cm.travelpost.tp.common.sms.ent.service;

import cm.travelpost.tp.common.sms.ent.vo.TxtMagicVO;

import java.util.List;

public interface TextMagicService {

	void otp(List<String> destinations) throws Exception;

	void send(String message, List<String> destinations) throws Exception;

	void send(TxtMagicVO txtMagic) throws Exception;
}
