package cm.packagemanager.pmanager.announce.ent.service;


import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;

public interface CategoryService {

    CategoryVO findByCode(String code) throws Exception;

    CategoryVO add(String code, String description) throws Exception;

    void delete(String code) throws BusinessResourceException;


}
