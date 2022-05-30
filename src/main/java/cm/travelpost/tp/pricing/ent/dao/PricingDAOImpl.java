package cm.travelpost.tp.pricing.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.pricing.ent.vo.PricingSubscriptionVOId;
import cm.travelpost.tp.pricing.ent.vo.PricingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class PricingDAOImpl extends Generic implements PricingDAO {

	private static Logger logger = LoggerFactory.getLogger(PricingDAOImpl.class);

	@Override
	public boolean delete(PricingSubscriptionVOId id) throws Exception {
		return updateDelete(id);
	}

	@Override
	public boolean updateDelete(Object o) throws BusinessResourceException {
		boolean result = false;

		try {

			PricingSubscriptionVOId id=(PricingSubscriptionVOId)o;
			PricingVO pricing = (PricingVO) findById(PricingVO.class,id);
			if (pricing != null) {

				pricing.cancel();
				merge(pricing);
				pricing = (PricingVO) get(PricingVO.class, id);
				result = (pricing != null) && (pricing.isCancelled());
			}
		} catch (Exception e) {
			throw new BusinessResourceException(e.getMessage());
		}
		return result;
	}


}
