package cm.packagemanager.pmanager.announce.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;


public interface CategoryDAO extends CommonDAO {

    CategoryVO findByCode(String code) throws ResourceNotFoundException;

    CategoryVO add(String code, String description) throws BusinessResourceException;

    void delete(String code) throws BusinessResourceException;
}
