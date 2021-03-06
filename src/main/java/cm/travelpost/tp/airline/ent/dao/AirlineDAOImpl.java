package cm.travelpost.tp.airline.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.airline.ent.vo.AirlineIdVO;
import cm.travelpost.tp.airline.ent.vo.AirlineVO;
import cm.travelpost.tp.common.Constants;
import cm.travelpost.tp.common.exception.AnnounceException;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.RecordNotFoundException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.ws.requests.CommonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Repository
@Transactional
public class AirlineDAOImpl extends Generic implements AirlineDAO {

    private static Logger logger = LoggerFactory.getLogger(AirlineDAOImpl.class);

    private  String errorPattern = "La compagnie aerienne [{0}] {1}";


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public AirlineVO add(String code,String description) throws Exception {

        if(StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(description)){

            AirlineIdVO id = new AirlineIdVO(code, Constants.DEFAULT_TOKEN);
            AirlineVO airline= (AirlineVO) checkAndResolve(AirlineVO.class,id);

            if(airline!=null)
                throw new Exception(MessageFormat.format(errorPattern,code, "existe déjà"));

            airline= new AirlineVO();
            airline.setId(id);
            airline.setDescription(description);

             save(airline);
            return (AirlineVO) get(AirlineVO.class,id);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public AirlineVO update(CommonDTO updAirline) throws Exception {


        AirlineIdVO airlineId = new AirlineIdVO(updAirline.getCode(), Constants.DEFAULT_TOKEN);

        if (airlineId == null) {
            throw new RecordNotFoundException("Aucune compagnie  trouvée avec le code :"+updAirline.getCode());
        }
        AirlineVO airline=(AirlineVO) checkAndResolve(AirlineVO.class,airlineId);
        airline.setDescription(updAirline.getName());

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
                throw new Exception(MessageFormat.format(errorPattern,code, " inexistante"));

            return updateDelete(airline);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,BusinessResourceException.class, AnnounceException.class})
    public boolean delete(AirlineIdVO id) throws BusinessResourceException {
        logger.info("Airline delete");

        try {
            AirlineVO airline = (AirlineVO) findById(AirlineVO.class,id);
            if(airline==null)
                throw new Exception(MessageFormat.format(errorPattern,id.getCode(), " inexistante"));

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

        filters[0]= FilterConstants.CANCELLED;


        return (AirlineVO)findByUniqueResult(AirlineVO.FINDBYCODE,AirlineVO.class, code,"code",null,filters);
    }

    public boolean updateDelete(AirlineVO airline) throws Exception {

        if(airline!=null){
            airline.cancel();
            merge(airline);
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
    public boolean updateDelete(Object o) throws BusinessResourceException, UserException {
        return false;
    }

}
