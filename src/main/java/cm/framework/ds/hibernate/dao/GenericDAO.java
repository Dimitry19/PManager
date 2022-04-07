package cm.framework.ds.hibernate.dao;

import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.event.IEvent;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.rating.ent.vo.RatingCountVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenericDAO<T, ID extends Serializable, NID extends Serializable>  extends  IEvent {

    Optional<T> find(Class<T> clazz, ID id);

    @Transactional
    T find(Class<T> clazz, ID id, String... filters);

    Optional<T> findByIdViaSession(Class<T> clazz, ID id);

    int count(Class<T> clazz, PageBy pageBy);

    T findById(Class<T> clazz, ID id);

    @Transactional
    T findById(Class<T> clazz, ID id, String... filters);

    void delete(Class<T> clazz, ID id, boolean enableFlushSession) throws RecordNotFoundException;

    List<T> findByUser(Class<T> clazz, Long userId, PageBy pageBy) throws Exception;

    List<T> findByUserId(Class<T> clazz, Long userId, PageBy pageBy) throws Exception;

    List<T> all(Class<T> clazz, PageBy pageBy) throws Exception;

    List<T> all(Class<T> clazz) throws Exception;

    List<T> all(Class<T> clazz, PageBy pageBy, String... filters) throws Exception;

    List<T> allAndOrderBy(Class<T> clazz, String field, boolean desc, PageBy pageBy) throws Exception;

    @Transactional
    T findByUniqueResult(String queryName, Class<T> clazz, ID id, String paramName) throws Exception;

    T findByUniqueResult(String queryName, Class<T> clazz, ID id, String paramName, PageBy pageBy, String... filters) throws Exception;

    List<T> findBy(String queryname, Class<T> clazz, ID id, String paramName, PageBy pageBy) throws Exception;

    List<T> findByUserNameQuery(String queryName, Class<T> clazz, Long userId, PageBy pageBy) throws Exception;

    int countByNameQuery(String queryName, Class<T> clazz, ID id, String paramName, PageBy pageBy) throws Exception;

    boolean updateDelete(Class<T> clazz, ID id, boolean enableFlushSession) throws BusinessResourceException;

    T save(T t) throws Exception;

    void persist(T t) throws Exception;

    void evict(T t) throws Exception;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    T findByNaturalIds(Class<T> clazz, Map naturalIds, String... filters) throws BusinessResourceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = {BusinessResourceException.class, Exception.class})
    T findByNaturalId(Class<T> clazz, NID naturalId, String... filters) throws BusinessResourceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = {BusinessResourceException.class, Exception.class})
    T findByNaturalId(Class<T> clazz, NID naturalId) throws BusinessResourceException;


    void flush() throws Exception;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
     void update(T t) throws BusinessResourceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    void remove(T t) throws BusinessResourceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    T merge(T t) throws BusinessResourceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    T get(Class<T> clazz, ID id) throws BusinessResourceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    T load(Class<T> clazz, ID id) throws BusinessResourceException;

    @Transactional(propagation = Propagation.REQUIRED)
    T checkAndResolve(Class<T> clazz, ID id) throws BusinessResourceException,ClassCastException;

    void pageBy(org.hibernate.query.Query query, PageBy pageBy);

    double calcolateAverage(T t) throws Exception;

    @Transactional
    List<RatingCountVO> findRatingCounts(UserVO user);

    Query search(String sqlQuery, String where,String... filters);
}