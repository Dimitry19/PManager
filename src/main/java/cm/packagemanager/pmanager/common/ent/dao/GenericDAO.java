package cm.packagemanager.pmanager.common.ent.dao;

import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericDAO <T, ID extends Serializable> {

	Optional<T> find(Class<T> clazz, ID id);

	Optional<T> findByIdViaSession(Class<T> clazz, ID id);

	int  count(Class<T> clazz, PageBy pageBy);

	T findById(Class<T> clazz, ID id);

	void delete(Class<T> clazz, ID id, boolean enableFlushSession) throws RecordNotFoundException;

	List<T> findByUser(Class<T> clazz,Long userId, PageBy pageBy) throws Exception;

	List<T> all(Class<T> clazz, PageBy pageBy) throws Exception;

	@Transactional(readOnly = true)
	List<T> all(Class<T> clazz, PageBy pageBy, String... filters) throws Exception;

	List<T> allAndOrderBy(Class<T> clazz, String field, boolean desc, PageBy pageBy) throws Exception;

	@Transactional
	T findByUniqueResult(String queryName, Class<T> clazz, ID id, String paramName, PageBy pageBy) throws Exception;

	@Transactional
	T findByUniqueResult(String queryName, Class<T> clazz, ID id, String paramName, PageBy pageBy, String... filters) throws Exception;

	List<T> findBy(String queryname, Class<T> clazz, ID id, String paramName, PageBy pageBy) throws Exception;

	List<T> findByUserNameQuery(String queryName,Class<T> clazz,Long userId, PageBy pageBy) throws Exception;

	int countByNameQuery(String queryName,Class<T> clazz,ID id,String paramName, PageBy pageBy) throws Exception;

	boolean updateDelete(Class<T> clazz, ID id, boolean enableFlushSession) throws BusinessResourceException;
}