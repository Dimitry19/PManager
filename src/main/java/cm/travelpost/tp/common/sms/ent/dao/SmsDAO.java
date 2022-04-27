package cm.travelpost.tp.common.sms.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.common.sms.ent.vo.SmsOTPVO;

public interface SmsDAO extends CommonDAO {

	void saves(SmsOTPVO sms)  throws Exception;

}
