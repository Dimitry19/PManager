package cm.packagemanager.pmanager.common.mail.ent.dao;


import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;

public interface ContactUSDAO  extends CommonDAO {

    void saves(ContactUSVO contact) throws BusinessResourceException;
}
