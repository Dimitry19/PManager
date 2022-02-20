package cm.packagemanager.pmanager.image.ent.dao;

import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.image.ent.vo.ImageVO;


public interface ImageDAO extends CommonDAO {
    ImageVO findByName(String name) throws Exception;
}
