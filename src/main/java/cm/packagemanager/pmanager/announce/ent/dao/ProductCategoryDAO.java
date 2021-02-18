package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryIdVO;
import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Repository;


public interface ProductCategoryDAO {//extends JpaRepository<ProductCategoryVO, ProductCategoryIdVO> {
	public ProductCategoryVO findByCode(String code) throws ResourceNotFoundException;
}
