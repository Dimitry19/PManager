package cm.travelpost.tp.pricing.ent.service;

import org.springframework.stereotype.Service;

@Service("pricingService")
public  abstract class APricingSubscriptionServiceImpl<T> implements APricingSubscriptionService {
	@Override
	public abstract boolean delete(String code, String token) throws Exception ;

	@Override
	public abstract T object(String code, String token) throws Exception;
}
