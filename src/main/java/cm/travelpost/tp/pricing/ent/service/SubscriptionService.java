package cm.travelpost.tp.pricing.ent.service;


import cm.travelpost.tp.pricing.ent.vo.SubscriptionVO;
import cm.travelpost.tp.ws.requests.pricing.CreateSubscriptionDTO;
import cm.travelpost.tp.ws.requests.pricing.ManageSubscriptionUserDTO;
import cm.travelpost.tp.ws.requests.pricing.UpdateSubscriptionDTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

public interface SubscriptionService extends APricingSubscriptionService{


	@Transactional(propagation = Propagation.REQUIRED)
	SubscriptionVO create(@NotNull CreateSubscriptionDTO dto) throws Exception;
	SubscriptionVO update(@NotNull String code, @NotNull String token, @NotNull UpdateSubscriptionDTO dto) throws Exception;
	boolean addToUser( @NotNull ManageSubscriptionUserDTO dto) throws Exception;
}
