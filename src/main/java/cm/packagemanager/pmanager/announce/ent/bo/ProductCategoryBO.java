package cm.packagemanager.pmanager.announce.ent.bo;


import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryIdVO;
import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryBO extends JpaRepository<ProductCategoryVO, ProductCategoryIdVO> {
}
