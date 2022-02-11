package cm.packagemanager.pmanager.announce.ent.service;

import cm.packagemanager.pmanager.announce.ent.dao.CategoryDAO;
import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDAO dao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryVO findByCode(String code) throws Exception {
        return dao.findByCode(code);
    }

    @Override
    public CategoryVO add(String code, String description) throws Exception {
        return dao.add(code,description);
    }

    @Override
    public void delete(String code) throws BusinessResourceException {
        dao.delete(code);
    }
}
