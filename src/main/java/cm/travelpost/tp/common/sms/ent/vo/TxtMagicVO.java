package cm.travelpost.tp.common.sms.ent.vo;

import java.util.ArrayList;
import java.util.List;

public class TxtMagicVO {

	private String message;
	private String destination;
	private List<String> destinations = new ArrayList<>();

	public TxtMagicVO(String message, String destination) {
		this.message = message;
		this.destination = destination;
		this.destinations.add(destination);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public List<String> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<String> destinations) {
		this.destinations = destinations;
	}
}
