package cm.packagemanager.pmanager.announce.ent.service;


import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;

public interface CategoryService {

	public CategoryVO findByCode(String code) throws Exception;

}
