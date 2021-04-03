package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAOImpl extends CommonFilter implements CategoryDAO {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public CategoryVO findByCode(String code) throws ResourceNotFoundException {

		Session session=sessionFactory.getCurrentSession();
		if(StringUtils.isEmpty(code)) return null;

		CategoryVO category=session.find(CategoryVO.class, code.toUpperCase());
		return category;
	}
}
