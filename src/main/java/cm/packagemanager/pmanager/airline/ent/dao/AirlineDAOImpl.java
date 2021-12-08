package cm.packagemanager.pmanager.airline.ent.dao;

import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
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
public class AirlineDAOImpl implements AirlineDAO {

    private static Logger logger = LoggerFactory.getLogger(AirlineDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public AirlineVO findByCode(String code) throws BusinessResourceException {

        logger.info(AirlineDAOImpl.class.getName() + " find by code ");

        Session session = this.sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);
        Query query = session.getNamedQuery(AirlineVO.FINDBYCODE);
        query.setParameter("code", code);
        List<AirlineVO> airlines = query.list();

        if (airlines != null && airlines.size() > 0) {
            return airlines.get(0);

        }
        return null;
    }

    @Override
    public List<AirlineVO> all() throws BusinessResourceException {
        Session session = this.sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);

        return null;
    }
}
