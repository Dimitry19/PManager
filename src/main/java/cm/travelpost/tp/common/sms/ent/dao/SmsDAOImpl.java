package cm.travelpost.tp.common.sms.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.sms.ent.vo.SmsOTPVO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SmsDAOImpl extends Generic implements SmsDAO {



	@Override
	public void saves(SmsOTPVO sms)  throws Exception {
			save(sms);

	}

	@Override
	public boolean updateDelete(Object id) throws BusinessResourceException, UserException {
		return false;
	}
}
