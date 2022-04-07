package cm.packagemanager.pmanager.airline.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.packagemanager.pmanager.airline.ent.vo.AirlineIdVO;
import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.exception.*;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.ws.requests.airplane.UpdateAirlineDTO;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AirlineDAOImpl extends Generic implements AirlineDAO {

    private static Logger logger = LoggerFactory.getLogger(AirlineDAOImpl.class);


    @Override
    public AirlineVO add(String code,String description) throws Exception {

        if(StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(description)){

            AirlineIdVO airlineId = new AirlineIdVO(code, Constants.DEFAULT_TOKEN);
            AirlineVO airline=(AirlineVO) checkAndResolve(AirlineVO.class,airlineId);

            if(airline!=null)
                throw new Exception(" Compagnie aerienne ["+code+"] existe deja");

            airline= new AirlineVO();
            airline.setId(airlineId);
            airline.setDescription(description);

            Long id =(Long) save(airline);
            return (AirlineVO) get(AirlineVO.class,id);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public AirlineVO update(UpdateAirlineDTO updAirline) throws Exception {


        AirlineIdVO airlineId = new AirlineIdVO(updAirline.getCode(), Constants.DEFAULT_TOKEN);

        if (airlineId == null) {
            throw new RecordNotFoundException("Aucune compagnie  trouvéé avec le code :"+updAirline.getCode());
        }
        AirlineVO airline=(AirlineVO) checkAndResolve(AirlineVO.class,airlineId);
        airline.setDescription(updAirline.getDescription());

        update(airline);
        return airline;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,BusinessResourceException.class, AnnounceException.class})
    public boolean delete(String code) throws BusinessResourceException {
        logger.info("Airline delete");

        try {
            AirlineVO airline = findByCode(code);
            if(airline==null)
                throw new Exception(" Compagnie aerienne ["+code+"] inexistante");

            return updateDelete(airline);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public AirlineVO findByCode(String code) throws Exception {

        logger.info(AirlineDAOImpl.class.getName() + " find by code ");

        filters= new String[1];

        filters[0]=FilterConstants.CANCELLED;


        return (AirlineVO)findByUniqueResult(AirlineVO.FINDBYCODE,AirlineVO.class, code,"code",null,filters);
    }

    public boolean updateDelete(AirlineVO airline) throws Exception {

        if(airline!=null){
            airline.setCancelled(true);
            update(airline);
            evict(airline);

            AirlineVO upAirline = (AirlineVO) get(AirlineVO.class,airline.getId());
            return upAirline.isCancelled();
        }
        return false;
    }


    @Override
    public List<AirlineVO> all() throws Exception {

        return all(AirlineVO.class);
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
