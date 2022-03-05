package cm.packagemanager.pmanager.airline.ent.dao;

import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.common.ent.dao.Generic;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AirlineDAOImpl extends Generic implements AirlineDAO {

    private static Logger logger = LoggerFactory.getLogger(AirlineDAOImpl.class);


    @Override
    public AirlineVO findByCode(String code) throws Exception {

        logger.info(AirlineDAOImpl.class.getName() + " find by code ");

        filters= new String[1];

        filters[0]=FilterConstants.CANCELLED;


        return (AirlineVO)findByUniqueResult(AirlineVO.FINDBYCODE,AirlineVO.class, code,"code",null,filters);
    }

    @Override
    public List<AirlineVO> all() throws Exception {
        all(AirlineVO.class);

        return null;
    }

    @Override
    public boolean updateDelete(Long id) throws BusinessResourceException, UserException {
        return false;
    }

    @Override
    public String composeQuery(Object o, String alias) throws Exception {
        return null;
    }

    @Override
    public void composeQueryParameters(Object o, Query query) throws Exception {

    }
}
