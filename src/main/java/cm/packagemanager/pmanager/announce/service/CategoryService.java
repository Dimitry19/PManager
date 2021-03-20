package cm.packagemanager.pmanager.announce.service;

import cm.packagemanager.pmanager.announce.ent.dao.CategoryDAO;
import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	@Autowired
	CategoryDAO categoryDAO;

	public CategoryService(){
	}

	public CategoryVO findByCode(String code){
		return categoryDAO.findByCode(code);
	}

}
