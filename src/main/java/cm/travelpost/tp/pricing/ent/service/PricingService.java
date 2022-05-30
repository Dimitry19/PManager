package cm.travelpost.tp.pricing.ent.service;


import cm.travelpost.tp.pricing.ent.vo.PricingVO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public interface PricingService {

	PricingVO create(@NotNull BigDecimal amount) throws Exception;
	PricingVO update(@NotNull String code, @NotNull  String token, @NotNull BigDecimal amount) throws Exception;
	boolean delete(@NotNull String code, @NotNull  String token) throws Exception;
}
