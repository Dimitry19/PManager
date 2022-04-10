package cm.travelpost.tp.announce.ent.service;

import cm.travelpost.tp.announce.ent.dao.CategoryDAO;
import cm.travelpost.tp.announce.ent.vo.CategoryVO;
import cm.travelpost.tp.common.exception.BusinessResourceException;
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
