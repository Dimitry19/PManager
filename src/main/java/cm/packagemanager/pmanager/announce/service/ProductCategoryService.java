package cm.packagemanager.pmanager.announce.service;

import cm.packagemanager.pmanager.announce.ent.dao.ProductCategoryDAO;
import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryIdVO;
import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCategoryService {

	@Autowired
	ProductCategoryDAO productCategoryDAO;

	public ProductCategoryService(){
	}

	public ProductCategoryVO findByCode(String code){
		return productCategoryDAO.findByCode(code);
	}

}
