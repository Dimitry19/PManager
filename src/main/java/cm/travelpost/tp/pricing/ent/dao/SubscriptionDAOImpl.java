package cm.travelpost.tp.pricing.ent.dao;

import cm.framework.ds.common.ent.vo.KeyValue;
import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.pricing.ent.vo.PricingSubscriptionVOId;
import cm.travelpost.tp.pricing.ent.vo.SubscriptionVO;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SubscriptionDAOImpl extends Generic implements SubscriptionDAO {

	@Override
	public boolean delete(PricingSubscriptionVOId id) throws Exception {
		return updateDelete(id);
	}

	@Override
	@Transactional(readOnly = true)
	public SubscriptionVO byType(SubscriptionPricingType type) throws Exception {
		setMap(new KeyValue(TYPE_PARAM,type));
		return (SubscriptionVO) findUniqueResult(SubscriptionVO.FINDBYTYPE,SubscriptionVO.class,getMap());
	}

	@Override
	public boolean updateDelete(Object o) throws BusinessResourceException {
		boolean result = false;

		try {

			PricingSubscriptionVOId id=(PricingSubscriptionVOId)o;
			SubscriptionVO subscription = (SubscriptionVO) findById(SubscriptionVO.class,id);
			if (subscription != null) {

				subscription.cancel();
				merge(subscription);
				subscription = (SubscriptionVO) get(SubscriptionVO.class, id);
				result = (subscription != null) && (subscription.isCancelled());
			}
		} catch (Exception e) {
			throw new BusinessResourceException(e.getMessage());
		}
		return result;
	}
}
