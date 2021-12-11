package cm.packagemanager.pmanager.common.mail.ent.dao;


import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class ContactUSDAOImpl implements ContactUSDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ContactUSVO contact) throws BusinessResourceException {
        Session session = sessionFactory.getCurrentSession();
        session.save(contact);
        session.flush();
    }

}
