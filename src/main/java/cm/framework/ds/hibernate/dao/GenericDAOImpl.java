package cm.framework.ds.hibernate.dao;


import cm.framework.ds.hibernate.utils.SQLUtils;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.event.AEvent;
import cm.packagemanager.pmanager.common.event.Event;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.common.utils.FileUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.notification.ent.service.NotificatorServiceImpl;
import cm.packagemanager.pmanager.notification.enums.NotificationType;
import cm.packagemanager.pmanager.rating.ent.vo.RatingCountVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * States of Entity Instances:
 * Any entity instance in our application appears in one of the three main states in relation to the Session persistence context:
 *  - transient — This instance isn't, and never was, attached to a Session. This instance has no corresponding rows in the database; it's usually just a new object that we created to save to the database.
 *  - persistent — This instance is associated with a unique Session object. Upon flushing the Session to the database, this entity is guaranteed to have a corresponding consistent record in the database.
 *  - detached — This instance was once attached to a Session (in a persistent state), but now it’s not. An instance enters this state if we evict it from the context, clear or close the Session, or put the instance through serialization/deserialization process.
 * @param <T>
 * @param <ID>
 * @param <NID>
 */
public class GenericDAOImpl<T, ID extends Serializable, NID extends Serializable> extends AEvent<T> implements GenericDAO<T, ID, NID> {


    private static Logger logger = LoggerFactory.getLogger(GenericDAOImpl.class);

    private static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);


    protected static final String FROM = " FROM ";
    protected static final String DESC = " desc ";
    protected static final String ASC = " asc ";
    protected static final String USER_PARAM = "userId";
    protected static final String TYPE_PARAM = "announceType";
    protected static final String TRANSPORT_PARAM = "transport";
    protected static final String ANNOUNCE_PARAM = "announceId";
    protected static final String START_DATE_PARAM = "startDate";
    protected static final String SEARCH_PARAM = "search";
    protected static final String ALIAS_ORDER = " as t order by t. ";

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    protected FileUtils fileUtils;

    @Value("${profile.user.img.folder}")
    protected String imagesFolder;

    @Autowired
    protected NotificatorServiceImpl notificatorServiceImpl;


    @Override
    public List autocomplete(String namedQuery, ID search, boolean caseInsensitive) {
        return
                sessionFactory.getCurrentSession()
                .getNamedQuery(namedQuery)
                .setParameter(SEARCH_PARAM, SQLUtils.forLike((String) search,caseInsensitive,true,true))
                .getResultList();

    }

    @Override
    @Transactional
    public Optional<T> find(Class<T> clazz, ID id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return Optional.ofNullable(sessionFactory.getCurrentSession().find(clazz, id));
    }

    @Override
    @Transactional
    public T find(Class<T> clazz, ID id, String... filters) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Session session = this.sessionFactory.getCurrentSession();
        for (String filter : filters) {
            session.enableFilter(filter);
        }
        return session.find(clazz, id);
    }

    @Override
    @Transactional
    public Optional<T> findByIdViaSession(Class<T> clazz, ID id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        Session session = sessionFactory.getCurrentSession();

        return Optional.ofNullable(session.get(clazz, id));
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = {BusinessResourceException.class, Exception.class})
    public T findByNaturalIds(Class<T> clazz, Map naturalIds, String... filters) throws BusinessResourceException {

        logger.info(" find by NaturalIds");
        Session session = this.sessionFactory.getCurrentSession();
        for (String filter : filters) {
            session.enableFilter(filter);
        }
        NaturalIdLoadAccess x = session.byNaturalId(clazz);
        naturalIds.forEach((k, v) -> {
            x.using((String) k, v);
        });
        return (T) x.getReference();
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = {BusinessResourceException.class, Exception.class})
    public T findByNaturalId(Class<T> clazz, NID naturalId, String... filters) throws BusinessResourceException {
        logger.info(" find by NaturalId");
        if (CollectionsUtils.isEmpty(Arrays.asList(filters))) {
            return findByNaturalId(clazz, naturalId);
        }
        Session session = this.sessionFactory.getCurrentSession();
        for (String filter : filters) {
            session.enableFilter(filter);
        }
        return session.bySimpleNaturalId(clazz).load(naturalId);
    }

    @Override
    public T findByNaturalId(Class<T> clazz, NID naturalId) throws BusinessResourceException {
        logger.info(" find by NaturalId");
        Session session = this.sessionFactory.getCurrentSession();
        return session.bySimpleNaturalId(clazz).load(naturalId);
    }

    @Override
    @Transactional
    public <T> T findByIdForCheckAndResolve(Class<T> clazz, ID id) {

        if (id == null) {
            this.logger.error("Errore esecuzione findByIdForCheckAndResolve:{}-{}", clazz, null);
            throw new IllegalArgumentException("ID cannot be null");
        }
        return getForCheckAndResolve(clazz, id);
    }
    @Override
    @Transactional
    public T findById(Class<T> clazz, ID id) {

        if (id == null) {
            this.logger.error("Errore esecuzione findById:{}-{}", clazz, null);
            throw new IllegalArgumentException("ID cannot be null");
        }
        return get(clazz, id);
    }

    @Override
    @Transactional
    public T findById(Class<T> clazz, ID id, String... filters) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        Session session = this.sessionFactory.getCurrentSession();
        for (String filter : filters) {
            session.enableFilter(filter);
        }

        return session.get(clazz, id);
    }

    @Override
    @Transactional
    public void delete(Class<T> clazz, ID id, boolean enableFlushSession) {
        try {
            Session session = sessionFactory.getCurrentSession();

            T ent = findByIdViaSession(clazz, id).get();
            if (ent != null) {
                session.remove(ent);
                if (enableFlushSession) {
                    session.flush();
                    //session.flush(); // Etant donné que la suppression de l'entité UserVO entraine la suppression des autres en
                    // entités pas besoin de faire le flush car le flush fait la synchronisation entre l'entité et la session hibernate
                    // du coup cree une transaction et enverra en erreur la remove
                }
            }
        } catch (Exception e) {
            throw new BusinessResourceException(e.getMessage());
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findByUser(Class<T> clazz, Long userId, PageBy pageBy) throws Exception {

        Session session = this.sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);
        return commonFindByUser(clazz,userId,pageBy,session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findByUserId(Class<T> clazz, Long userId, PageBy pageBy) throws Exception {

        Session session = this.sessionFactory.getCurrentSession();
        return commonFindByUser(clazz,userId,pageBy,session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> all(Class<T> clazz) throws Exception {

        Session session = this.sessionFactory.getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder(FROM);
        queryBuilder.append(clazz.getName());

        Query query = session.createQuery(queryBuilder.toString(), clazz);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> all(Class<T> clazz, PageBy pageBy) throws Exception {

        Session session = this.sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);
        StringBuilder queryBuilder = new StringBuilder(FROM);
        queryBuilder.append(clazz.getName());

        Query query = session.createQuery(queryBuilder.toString(), clazz);
        pageBy(query, pageBy);

        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> all(Class<T> clazz, PageBy pageBy, String... filters) throws Exception {

        StringBuilder queryBuilder = new StringBuilder(FROM);
        queryBuilder.append(clazz.getName());

        Session session = this.sessionFactory.getCurrentSession();
        if (filters!=null){
            for (String filter : filters) {
                session.enableFilter(filter);
            }
        }

        Query query = session.createQuery(queryBuilder.toString(), clazz);

        pageBy(query, pageBy);
        return query.getResultList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<T> allAndOrderBy(Class<T> clazz, String field, boolean desc, PageBy pageBy) throws Exception {

        Session session = this.sessionFactory.getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder(FROM);
        queryBuilder.append(clazz.getName());
        if(StringUtils.isNotEmpty(field)){
            queryBuilder.append(ALIAS_ORDER);
            queryBuilder.append(field);

            if (desc) {
                queryBuilder.append(DESC);
            } else {
                queryBuilder.append(ASC);
            }
        }

        session.enableFilter(FilterConstants.CANCELLED);
        Query query = session.createQuery(queryBuilder.toString(), clazz);
        pageBy(query, pageBy);
        return query.getResultList();
    }

    /**
     * Prend une NamedQuery et retourne un resultat unique
     *
     * @param queryName
     * @param clazz
     * @param id
     * @param paramName
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public T findByUniqueResult(String queryName, Class<T> clazz, ID id, String paramName) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createNamedQuery(queryName, clazz);

        if (StringUtils.isNotEmpty(paramName) && id != null) {
            query.setParameter(paramName, id);
        }

        return (T) query.uniqueResult();
    }

    /**
     * Prend une NamedQuery et retourne un resultat unique
     *
     * @param queryName
     * @param clazz
     * @param id
     * @param paramName
     * @param pageBy
     * @param filters
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public T findByUniqueResult(String queryName, Class<T> clazz, ID id, String paramName, PageBy pageBy, String... filters) throws Exception {

        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createNamedQuery(queryName, clazz);

        if (filters != null) {
            for (String filter : filters) {
                session.enableFilter(filter);
            }
        }

        if (StringUtils.isNotEmpty(paramName) && id != null) {
            query.setParameter(paramName, id);
        }

        pageBy(query, pageBy);

        return (T) query.uniqueResult();
    }

    @Override
    @Transactional
    public List<T> findBy(String namedQuery, Class<T> clazz, ID id, String paramName, PageBy pageBy) throws Exception {

        Session session = this.sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);
        Query query = session.createNamedQuery(namedQuery, clazz);

        if (StringUtils.isNotEmpty(paramName) && id != null) {
            query.setParameter(paramName, id);
        }

        pageBy(query, pageBy);

        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findByUserNameQuery(String queryName, Class<T> clazz, Long userId, PageBy pageBy) throws Exception {

        Session session = this.sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);
        Query query = session.createQuery(queryName, clazz);
        query.setParameter(USER_PARAM, userId);
        pageBy(query, pageBy);

        return query.getResultList();
    }

    @Override
    public int countByNameQuery(String queryName, Class<T> clazz, ID id, String paramName, PageBy pageBy) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);
        Query query = session.createNamedQuery(queryName, clazz);
        query.setParameter(paramName, id);
        pageBy(query, pageBy);
        List results = query.list();
        return CollectionsUtils.size(results);
    }

    @Override
    @Transactional
    public int count(Class<T> clazz, PageBy pageBy) {

        Session session = this.sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);
        Query query = session.createQuery(FROM + clazz.getName());
        pageBy(query, pageBy);
        List results = query.list();
        return CollectionsUtils.isNotEmpty(results) ? results.size() : 0;
    }


    @Override
    @Transactional
    public boolean updateDelete(Class<T> clazz, ID id, boolean enableFlushSession) throws BusinessResourceException {

        boolean result = false;

        try {

            //Session session=sessionFactory.getCurrentSession();
            T ent = findById(clazz, id);
            if (ent != null) {
				/*ent.setCancelled(true);
				session.merge(announce);
				ent = session.get(clazz, id);
				result= (ent!=null) && (ent.isCancelled());*/
            }
        } catch (Exception e) {
            throw new BusinessResourceException(e.getMessage());
        }
        return result;
    }

    /**
     * Son but est de maniere basique le meme que persist, mais avec des details dans leurs implementations.
     * La fonction save() retourne la clé primaire de l'entité sauvée.
     *  Si on applique cette methode sur une entité est détachée du context persistent (session.evict(entite)),
     *  un nouveau record se creera dans la base de données.
     *
     * Exemple:
     *          Person person = new Person();
     *          person.setName("John");
     *          Long id1 = (Long) session.save(person);
     *
     *           session.evict(person);
     *          Long id2 = (Long) session.save(person);
     * @param t
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T save(T t) {
        Session session = sessionFactory.getCurrentSession();
        return (T)session.save(t);
    }

    /**
     * The persist() method is used to create or save a new entity in the database.
     * if we try to update an existing record using persist() method it will throw EntityExistsException.
     * if an instance is detached, we'll get an exception
     *
     * Que se passe -t-il après que la methode persist est appellée?
     * L'entité est passé de l'etat TRANSIENT è PERSISTENT, elle est maintenant dans le context persistent, mais pas encore
     * dans la base de données.
     * L'insertion dans la base de données (la INSERT proprement dite ) advient quand : on commit la transaction, on fait la
     * flush (session.flush()) ou ferme (session.close()) de la session.
     *
     * Exemple:
     * Person person = new Person();
     * person.setName("John");
     * session.persist(person);
     *
     * session.evict(person); // On detache l'entité Person du context persistent
     *
     * session.persist(person); // PersistenceException!
     * @param t
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void persist(T t) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(t); // on rend l'instance t persistent
        //session.flush(); // Permet de vider la session le flush fait la synchronisation entre l'entité et la session hibernate

    }

    @Override
    public void evict(T t) throws Exception {

        Session session = sessionFactory.getCurrentSession();
        session.evict(t);
    }

    @Override
    public void flush() throws Exception {

        Session session = sessionFactory.getCurrentSession();
        session.flush();
    }

    /**
     * A la difference de merge() la methode update() ne retourne pas de valeur
     * l'entité passe de detached -> persistent
     *
     * Exemple:
     *      Person person = new Person();
     *      person.setName("John");
     *      session.save(person);
     *      session.evict(person);
     *      person.setName("Mary");
     *      session.update(person);
     *
     * transient -> persistent  leve une execption PersistenceException
     * Exemple:
     *          Person person = new Person();
     *          person.setName("John");
     *          session.update(person); // PersistenceException!
     *
     * @param t
     * @throws BusinessResourceException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    public  void update(T t) throws BusinessResourceException {
        logger.info("Generic update");
        Session session = this.sessionFactory.getCurrentSession();
        session.update(t);

    }

    /**
     *
     * @param t
     * @throws BusinessResourceException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    public void remove(T t) throws BusinessResourceException {
        logger.info("Remove");
        Session session = this.sessionFactory.getCurrentSession();
        if (t != null) {
            session.remove(t);
        }
    }

    /**
     * Using <code>merge()</code> method we can create/save a new record as well as we can update an existing record.
     * Here, entity can be an unmanaged or persistent entity instance.
     * This method returns the managed instance that the state was merged to.
     * After the merge operation executed successfully:
     * a new row would be inserted, or an existing one would be updated.
     * the returned object is different than the passed object.
     * if the passed object is unmanaged, it says unmanaged.
     * the entity manager tracks changes of the returned, managed object within transaction boundary.
     *
     *      Exemple:
     *              Person person = new Person();
     *              person.setName("John");
     *              session.save(person);
     *
     *              session.evict(person);
     *              person.setName("Mary");
     *
     *              Person mergedPerson = (Person) session.merge(person);
     *              mergedPerson is different of person
     *
     *          Merge fonctionne comme suit:
     *             - Si l'entité est detachée, elle se copie sur une entité persistante existante
     *             - Si l'entité est transient, elle se copie sur une entité persistante nouvellement créée.
     *                          cette opération cascade pour toutes les relations avec le mappage cascade=MERGE ou cascade=ALL.
     *             - Si l'entité est persistent, alors cet appel de méthode n'a pas d'effet sur elle (mais la cascade a toujours lieu).
     * @param t
     * @return
     * @throws BusinessResourceException
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    public T merge(T t) throws BusinessResourceException {
        logger.info("Generic merge");
        Session session = this.sessionFactory.getCurrentSession();
        if (t != null) {
             return (T)session.merge(t);
        }
        return null;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    public T get(Class<T> clazz, ID id) throws BusinessResourceException {

        Session session = this.sessionFactory.getCurrentSession();

        return session.get(clazz , id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    public <T> T getForCheckAndResolve(Class<T> clazz, ID id) throws BusinessResourceException {

        Session session = this.sessionFactory.getCurrentSession();

        return session.get(clazz , id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    public T load(Class<T> clazz, ID id) throws BusinessResourceException {

        Session session = this.sessionFactory.getCurrentSession();

        return session.load(clazz , id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
    public  T checkAndResolve(Class<T> clazz, ID id) throws BusinessResourceException,ClassCastException {

        if(clazz== null || id==null) return null;

        return  findByIdForCheckAndResolve(clazz , id);
    }

    @Override
    public void pageBy(Query query, PageBy pageBy) {
        if (pageBy != null) {

            query.setFirstResult(pageBy.getPage());
            query.setMaxResults(pageBy.getSize());
        }
    }


    @Override
    public double calcolateAverage(T t) {
        if (t == null)
            return 0.0;
        UserVO user = (UserVO) t;
        int totalRating = 0;
        int counterRating = 0;
        List<RatingCountVO> ratingCounts = findRatingCounts(user);
        if (CollectionsUtils.isEmpty(ratingCounts)) {
            return 0.0;
        }
        for (int i = 0; i < ratingCounts.size(); i++) {
            RatingCountVO ratingCount = ratingCounts.get(i);
            counterRating += ratingCount.getCount();
            totalRating += ratingCount.getRating().toValue() * ratingCount.getCount();
        }
        double averageRating = new BigDecimal(((double) totalRating / (double) counterRating), MATH_CONTEXT).doubleValue();
        user.setRating(averageRating);
        return averageRating;
    }

    @Override
    @Transactional
    public List<RatingCountVO> findRatingCounts(UserVO user) {
        Session session = sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);
        Query query = session.createNamedQuery(ReviewVO.RATING, RatingCountVO.class);
        query.setParameter(USER_PARAM, user);
        return query.getResultList();
    }

    @Override
    public Query search(String sqlQuery, String where, String... filters) {

        Session session = this.sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);
        return session.createQuery(sqlQuery+where);
    }

    @Override
    public void generateEvent( NotificationType type) throws Exception{
        Event event = new Event(DateUtils.DateToSQLDate(new Date()),type);

        event.setId((Long) props.get(PROP_ID));
        event.setMessage((String) props.get(PROP_MSG));
        event.setUserId((Long) props.get(PROP_USR_ID));
        event.setUsers((Set<UserVO>) props.get(PROP_SUBSCRIBERS));
        notificatorServiceImpl.addEvent(event);

    }

    private List<T> commonFindByUser(Class<T> clazz, Long userId, PageBy pageBy, Session session){
        Query query = session.createQuery(FROM + clazz.getName() + " as elt where elt.userId =:userId", clazz);
        query.setParameter(USER_PARAM, userId);
        pageBy(query, pageBy);

        return query.getResultList();
    }

}
