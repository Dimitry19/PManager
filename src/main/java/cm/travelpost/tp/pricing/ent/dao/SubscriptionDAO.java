package cm.travelpost.tp.pricing.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.pricing.ent.vo.PricingSubscriptionVOId;
import cm.travelpost.tp.pricing.ent.vo.SubscriptionVO;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;

public interface SubscriptionDAO extends CommonDAO {
	boolean delete(PricingSubscriptionVOId id) throws Exception;

	SubscriptionVO byType(SubscriptionPricingType type) throws Exception;
}
