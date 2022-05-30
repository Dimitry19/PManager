package cm.travelpost.tp.pricing.ent.service;


import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.common.exception.PricingException;
import cm.travelpost.tp.pricing.ent.vo.PricingVO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public interface PricingService {

	public PricingVO create(@NotNull BigDecimal amount) throws Exception;

	public PricingVO update(@NotNull String code, @NotNull  String token, @NotNull BigDecimal amount) throws Exception;

	public boolean delete(@NotNull String code, @NotNull  String token) throws Exception;

	public PricingVO pricing(@NotNull String code, @NotNull  String token) throws Exception;

	public int count(PageBy pageBy) throws PricingException,Exception;

	public List<PricingVO> pricings(PageBy pageBy) throws PricingException;

	public PricingVO byPrice(@NotNull BigDecimal amount) throws Exception;

}
