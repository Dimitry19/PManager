package cm.travelpost.tp.pricing.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.pricing.ent.vo.PricingSubscriptionVOId;
import cm.travelpost.tp.pricing.ent.vo.PricingVO;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;

import java.math.BigDecimal;

public interface PricingDAO extends CommonDAO {

	boolean delete(PricingSubscriptionVOId id) throws Exception;

	PricingVO byPrice(BigDecimal amount) throws Exception;

	PricingVO byType(SubscriptionPricingType type) throws Exception;
}
