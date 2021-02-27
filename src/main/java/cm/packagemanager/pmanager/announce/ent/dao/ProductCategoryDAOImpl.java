package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryVO;
import cm.packagemanager.pmanager.common.utils.StringUtils;
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
		if(StringUtils.isEmpty(code)) return null;


		ProductCategoryVO productCategory=session.find(ProductCategoryVO.class, code.toUpperCase());
		return productCategory;
	}
}
