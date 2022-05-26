package cm.travelpost.tp.authentication.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.UserException;
import org.springframework.stereotype.Repository;

@Repository
public  class AuthenticationDAOImpl extends Generic implements AuthenticationDAO {
    @Override
    public boolean updateDelete(Object id) throws BusinessResourceException, UserException {
        return false;
    }
}
