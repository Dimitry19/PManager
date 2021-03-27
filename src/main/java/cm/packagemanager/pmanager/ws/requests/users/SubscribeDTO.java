package cm.packagemanager.pmanager.ws.requests.users;

import javax.validation.constraints.NotNull;

public class SubscribeDTO {

	@NotNull(message = "Subscriber Id should not be empty")
	private Long subscriberId;

	@NotNull(message = "Subscription Id should not be empty")
	private Long subscriptionId;

	public Long getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(Long subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
}
