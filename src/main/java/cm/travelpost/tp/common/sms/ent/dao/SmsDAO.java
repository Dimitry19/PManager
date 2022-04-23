package cm.travelpost.tp.common.sms.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.common.sms.ent.vo.SmsOTPVO;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

@Repository
@Configuration
public interface SmsDAO extends CommonDAO {

	void saves(SmsOTPVO sms)  throws Exception;

}
