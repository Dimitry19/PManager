package cm.travelpost.tp.common.ent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messages.properties")
public class ApplicationMessageConfig {


	@Value("${message.announce.warning.close}")
	private String announceWarning;

	@Value("${message.announce.warning.close.availability}")
	private String announceWarningAvailability;

	@Value("${message.announce.warning.close.expiration}")
	private String announceWarningExpiration;

	@Value("${message.announce.warning.quantity.reduction}")
	private String announceWarningQtyReduction;

	@Value("${message.announce.warning.delete}")
	private String announceWarningDelete;

	public String getAnnounceWarning() {
		return announceWarning;
	}

	public String getAnnounceWarningAvailability() {
		return announceWarningAvailability;
	}

	public String getAnnounceWarningExpiration() {
		return announceWarningExpiration;
	}

	public String getAnnounceWarningQtyReduction() {return announceWarningQtyReduction;	}

	public String getAnnounceWarningDelete() { return announceWarningDelete;}
}
