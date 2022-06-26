package cm.travelpost.tp.authentication.ent.service;

import cm.travelpost.tp.authentication.ent.dao.AuthenticationDAO;
import cm.travelpost.tp.authentication.ent.vo.AuthenticationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("authService")
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    AuthenticationDAO dao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long save(AuthenticationVO authentication) throws Exception {
        return (Long) dao.save(authentication);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public AuthenticationVO findById(Long id) throws Exception {
        return (AuthenticationVO)dao.findById(AuthenticationVO.class,id);
    }


}
