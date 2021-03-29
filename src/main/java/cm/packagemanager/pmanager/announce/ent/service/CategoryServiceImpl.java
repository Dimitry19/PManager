package cm.packagemanager.pmanager.announce.ent.service;

import cm.packagemanager.pmanager.announce.ent.dao.CategoryDAO;
import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService{
	@Autowired
	CategoryDAO categoryDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public CategoryVO findByCode(String code) throws Exception{
		return categoryDAO.findByCode(code);
	}
}
