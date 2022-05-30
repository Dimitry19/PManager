package cm.travelpost.tp.pricing.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDAOImpl extends Generic implements SubscriptionDAO {
	@Override
	public boolean updateDelete(Object id) throws BusinessResourceException {
		return false;
	}
}
