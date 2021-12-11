package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.common.ent.dao.CommonGenericDAO;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CategoryDAOImpl extends CommonGenericDAO implements CategoryDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryVO findByCode(String code) throws ResourceNotFoundException {

        if (StringUtils.isEmpty(code)) return null;
        CategoryVO category = (CategoryVO) findById(CategoryVO.class, code.toUpperCase());
        return category;
    }
}
