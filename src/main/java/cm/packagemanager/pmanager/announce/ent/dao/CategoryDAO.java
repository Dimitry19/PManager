package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;


public interface CategoryDAO extends CommonDAO {
    CategoryVO findByCode(String code) throws ResourceNotFoundException;
}
