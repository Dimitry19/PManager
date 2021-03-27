package cm.packagemanager.pmanager.common.mail.ent.dao;

import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;

public interface ContactUSDAO {

	public void save(ContactUSVO contact) throws BusinessResourceException;
}
