package cm.travelpost.tp.image.ent.dao;

import cm.framework.ds.hibernate.dao.CommonGenericDAO;
import cm.travelpost.tp.image.ent.vo.ImageVO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class ImageDAOImpl extends CommonGenericDAO implements ImageDAO {


    @Override
    @Transactional(readOnly = true)
    public ImageVO findByName(String name) throws Exception {
        return (ImageVO) findByUniqueResult(ImageVO.IMG_BY_NAME, ImageVO.class, name, "name");
    }
}
