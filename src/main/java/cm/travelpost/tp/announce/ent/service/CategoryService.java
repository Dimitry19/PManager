package cm.travelpost.tp.announce.ent.service;


import cm.travelpost.tp.announce.ent.vo.CategoryVO;
import cm.travelpost.tp.common.exception.BusinessResourceException;

public interface CategoryService {

    CategoryVO findByCode(String code) throws Exception;

    CategoryVO add(String code, String description) throws Exception;

    void delete(String code) throws BusinessResourceException;


}
