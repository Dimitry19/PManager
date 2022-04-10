package cm.travelpost.tp.image.ent.dao;


import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.image.ent.vo.ImageVO;


public interface ImageDAO extends CommonDAO {
    ImageVO findByName(String name) throws Exception;
}
