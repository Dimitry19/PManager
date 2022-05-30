package cm.travelpost.tp.pricing.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.pricing.ent.vo.PricingSubscriptionVOId;

public interface PricingDAO extends CommonDAO {

	boolean delete(PricingSubscriptionVOId id) throws Exception;
}
