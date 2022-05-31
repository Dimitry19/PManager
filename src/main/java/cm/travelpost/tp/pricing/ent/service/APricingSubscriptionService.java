package cm.travelpost.tp.pricing.ent.service;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface APricingSubscriptionService<T> {

	@Transactional(propagation = Propagation.REQUIRED)
	boolean delete(@NotNull String code, @NotNull  String token) throws Exception;


	@Transactional(propagation = Propagation.REQUIRED)
	T object(@NotNull String code, @NotNull  String token) throws Exception;

	public int count(PageBy pageBy) throws Exception;

	public List<T> all(PageBy pageBy) throws  Exception;
	public T byType(@NotNull SubscriptionPricingType type) throws Exception;
}
