package cm.packagemanager.pmanager.common.mail.ent.dao;



import cm.framework.ds.hibernate.dao.CommonGenericDAO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class ContactUSDAOImpl  extends CommonGenericDAO implements ContactUSDAO {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saves(ContactUSVO contact) throws BusinessResourceException {
        save(contact);
    }

}
