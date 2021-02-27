package cm.packagemanager.pmanager.common.ent.dao;

import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;

public interface CommonDAO {

	public void deleteObject(Object object ) throws RecordNotFoundException;
}
