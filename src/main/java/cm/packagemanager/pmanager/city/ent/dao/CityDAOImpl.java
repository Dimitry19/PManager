package cm.packagemanager.pmanager.city.ent.dao;


import cm.framework.ds.hibernate.dao.Generic;
import cm.packagemanager.pmanager.city.ent.vo.CityVO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.AnnounceException;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.ws.requests.CommonDTO;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Repository
@Transactional
public class CityDAOImpl extends Generic implements CityDAO {

    private static Logger logger = LoggerFactory.getLogger(CityDAOImpl.class);

   /* @Autowired
    CityHelper helper;*/

    @Override
    @Transactional(readOnly = true)
    public List<CityVO> autocomplete(String search, boolean caseInsensitive) throws Exception {

        return autocomplete(CityVO.AUTOCOMPLETE,search, caseInsensitive);
    }


    @Override
    @Transactional(readOnly = true)
    public int count(Object o,PageBy pageBy) throws AnnounceException,Exception {
        logger.info(" City - count");
         if(o == null) {
            return count(CityVO.class, pageBy);
        }
        String param = (String) o;
        return CollectionsUtils.size(autocomplete(param, true));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityVO> cities(PageBy pageBy) throws Exception {

        List<CityVO> announces = all(CityVO.class, pageBy,null );
        return announces;
    }



    @Override
    @Transactional(readOnly = true)
    public List<CityVO> cities(int page, int size) throws Exception {

        PageBy pageBy = new PageBy(page, size);
        return cities(pageBy);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
    public CityVO city(String id) throws Exception {

        return (CityVO) findById(CityVO.class, id);

    }

    /**
     * Cette methode permet de creer une Ville
     * @param dto  ddonnees de la ville  à creer
     * @return AnnonceVO
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class,Exception.class})
    public CityVO create(CommonDTO dto) throws Exception {

        if (dto != null) {
            CityVO city =   (CityVO)checkAndResolve(CityVO.class, dto.getCode());

            if(city !=null){
                throw new Exception("Cette ville existe déjà");
            }
            city.setId(dto.getCode());
            city.setName(dto.getName());

            save(city);
            return (CityVO)get(CityVO.class,dto.getCode());
        }
        return null;
    }

    /**
     * Cette methode permet d'ajourner une Ville
     * @param unto  ddonnees de la Ville à ajourner
     * @return CityVO
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public CityVO update(CommonDTO unto) throws Exception {
           return update(unto.getCode(),unto.getName());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public CityVO update(String id, String name) throws Exception {
        CityVO city = city(id);

        if (city == null) {
            throw new RecordNotFoundException("Aucune ville trouvée avec ce code "+ id);
        }
        city.setName(name);
        update(city);
        return city;
    }




    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,BusinessResourceException.class})
    public boolean delete(String id) throws Exception {
        logger.info("City: delete");

        try {
            CityVO city = (CityVO)checkAndResolve(CityVO.class,id);
            if (city == null)
                    throw new BusinessResourceException("Aucune ville n'a ete trouvé avec cet id");

        } catch ( Exception e) {
            throw e;
        }
        return updateDelete(id);
    }

    @Override
    public void delete(CityVO city) throws BusinessResourceException {
          updateDelete(city);
    }

    @Override
    public boolean updateDelete(Object id) throws BusinessResourceException {

        try {

            String code = (String)id;
            CityVO city = city(code);
            if (city != null) {
                delete(CityVO.class, code, true);
                return true;
            }
        } catch (Exception e) {
            throw new BusinessResourceException(e.getMessage());
        }

        return false;
    }

    @Override
    public String composeQuery(Object obj, String alias) throws Exception { return null;}

    @Override
    public void composeQueryParameters(Object obj, Query query) throws Exception  {}

}