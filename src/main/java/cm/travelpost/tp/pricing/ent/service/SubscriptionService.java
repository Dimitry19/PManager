package cm.travelpost.tp.pricing.ent.service;


import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.pricing.ent.vo.SubscriptionVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.pricing.CreateSubscriptionDTO;
import cm.travelpost.tp.ws.requests.pricing.ManageSubscriptionUserDTO;
import cm.travelpost.tp.ws.requests.pricing.UpdateSubscriptionDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface SubscriptionService extends APricingSubscriptionService{

	SubscriptionVO create(@NotNull CreateSubscriptionDTO dto) throws Exception;
	SubscriptionVO update(@NotNull String code, @NotNull String token, @NotNull UpdateSubscriptionDTO dto) throws Exception;
	boolean addOrRemoveToUser(@NotNull ManageSubscriptionUserDTO dto) throws Exception;
	int countUsers(@NotNull String code, @NotNull String token, PageBy pageBy) throws Exception;
	List<UserVO> retrieveUsers(@NotNull String code, @NotNull String token, PageBy pageBy) throws Exception;
}
