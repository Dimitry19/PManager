package cm.travelpost.tp.pricing.ent.service;


import cm.travelpost.tp.pricing.ent.vo.PricingVO;
import cm.travelpost.tp.ws.requests.pricing.CreatePricingDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public interface PricingService extends PricingSubscriptionService<PricingVO> {

	public PricingVO create(@NotNull CreatePricingDTO dto) throws Exception;

	public PricingVO update(@NotNull String code, @NotNull  String token, @NotNull BigDecimal amount) throws Exception;

	public boolean delete(@NotNull String code, @NotNull  String token) throws Exception;

	public PricingVO byPrice(@NotNull BigDecimal amount) throws Exception;


}
