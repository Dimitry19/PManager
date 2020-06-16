package cm.packagemanager.pmanager.user.ent.bo;

import cm.packagemanager.pmanager.common.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component("authUserBO")
//@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BusinessException.class)
public class AuthUserBOImpl implements AuthUserBO {

}
