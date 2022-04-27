package cm.travelpost.tp.common.sms.ent.vo;

import com.textmagic.sdk.RestClient;
import com.textmagic.sdk.resource.instance.TMNewMessage;

public class TxtMagicMessage  extends TMNewMessage {

	public TxtMagicMessage(RestClient client) {
		super(client);
	}
}
