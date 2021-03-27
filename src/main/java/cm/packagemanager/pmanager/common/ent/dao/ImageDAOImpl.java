package cm.packagemanager.pmanager.common.ent.dao;

import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.ent.vo.ImageVO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class ImageDAOImpl extends CommonFilter implements ImageDAO {



	@Override
	@Transactional(readOnly = true)
	public ImageVO findByName(String name) throws Exception {
		return (ImageVO) findByUniqueResult(ImageVO.IMG_BY_NAME,ImageVO.class,name,"name");
	}
}
