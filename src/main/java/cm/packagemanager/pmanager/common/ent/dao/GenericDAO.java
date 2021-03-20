package cm.packagemanager.pmanager.common.ent.dao;

import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;

import java.io.Serializable;
import java.util.Optional;

public interface GenericDAO <T, ID extends Serializable> {

	Optional<T> find(Class<T> clazz, ID id);

	Optional<T> findByIdViaSession(Class<T> clazz, ID id);

	void delete(Class<T> clazz, ID id,boolean enableFlushSession) throws RecordNotFoundException;
	boolean updateDelete(Class<T> clazz, ID id,boolean enableFlushSession) throws BusinessResourceException;
}