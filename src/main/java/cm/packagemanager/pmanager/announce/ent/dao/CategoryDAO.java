package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;


public interface CategoryDAO {//extends JpaRepository<ProductCategoryVO, ProductCategoryIdVO> {
	public CategoryVO findByCode(String code) throws ResourceNotFoundException;
}
