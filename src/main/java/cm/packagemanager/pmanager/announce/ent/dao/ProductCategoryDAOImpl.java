package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryVO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class ProductCategoryDAOImpl implements  ProductCategoryDAO{

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public ProductCategoryVO findByCode(String code) throws ResourceNotFoundException {
		Session session=sessionFactory.getCurrentSession();
		ProductCategoryVO productCategory=session.find(ProductCategoryVO.class, code);
		return productCategory;
	}
}
