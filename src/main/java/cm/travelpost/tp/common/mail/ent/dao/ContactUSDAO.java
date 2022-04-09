package cm.travelpost.tp.common.mail.ent.dao;


import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.mail.ent.vo.ContactUSVO;

public interface ContactUSDAO  extends CommonDAO {

    void saves(ContactUSVO contact) throws BusinessResourceException;
}
