package cm.framework.ds.activity.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityDAOImpl extends Generic implements ActivityDAO{

	@Override
	public boolean updateDelete(Object id) throws BusinessResourceException {
		return false;
	}
}
