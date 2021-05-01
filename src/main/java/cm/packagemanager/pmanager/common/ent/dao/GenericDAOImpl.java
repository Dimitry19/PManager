package cm.packagemanager.pmanager.common.ent.dao;


import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.common.utils.FileUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.rating.ent.vo.RatingCountVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
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
import java.util.List;
import java.util.Optional;


public class GenericDAOImpl <T, ID extends Serializable,NID extends Serializable> implements GenericDAO<T,ID,NID> {


	private static Logger logger = LoggerFactory.getLogger(GenericDAOImpl.class);

	private static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);


	private static final String FROM =" FROM ";
	private static final String DESC =" desc ";
	private static final String ASC =" asc ";
	private static final String USER_PARAM ="userId";
	private static final String ALIAS_ORDER =" as t order by t. ";

	@Autowired
	protected SessionFactory sessionFactory;

	@Autowired
	protected FileUtils fileUtils;

	@Value("${profile.user.img.folder}")
	protected String imagesFolder;


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
	public Optional<T> findByIdViaSession(Class<T> clazz, ID id) {

		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}

		Session session = sessionFactory.getCurrentSession();

		return Optional.ofNullable(session.get(clazz, id));
	}

	@Override
	@Transactional
	public  T findById(Class<T> clazz, ID id) {

		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}

		Session session = sessionFactory.getCurrentSession();

		return session.get(clazz, id);
	}

	@Override
	@Transactional
	public void delete(Class<T> clazz, ID id,boolean enableFlushSession) {
		try{
			Session session=sessionFactory.getCurrentSession();

			T ent= (T) findByIdViaSession(clazz,id).get();
			if(ent!=null){
				session.remove(ent);
				if(enableFlushSession){
					session.flush();
					//session.flush(); // Etant donné que la suppression de l'entité UserVO entraine la suppression des autres en
					// entités pas besoin de faire le flush car le flush fait la synchronisation entre l'entité et la session hibernate
					// du coup cree une transaction et enverra en erreur la remove
				}
			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findByUser(Class<T> clazz, Long userId, PageBy pageBy) throws Exception {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createQuery(FROM +clazz.getName()+" as elt where elt.user.id =:userId",clazz);
		query.setParameter(USER_PARAM, userId);
		pageBy(query, pageBy);

		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> all(Class<T> clazz) throws Exception {

		Session session = this.sessionFactory.getCurrentSession();
		StringBuilder queryBuilder= new StringBuilder(FROM);
		queryBuilder.append(clazz.getName());

		Query query=session.createQuery(queryBuilder.toString(),clazz);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> all(Class<T> clazz,PageBy pageBy) throws Exception {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		StringBuilder queryBuilder= new StringBuilder(FROM);
		queryBuilder.append(clazz.getName());

		Query query=session.createQuery(queryBuilder.toString(),clazz);
		pageBy(query, pageBy);

		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> all(Class<T> clazz, PageBy pageBy, String... filters) throws Exception {

		StringBuilder queryBuilder= new StringBuilder(FROM);
		queryBuilder.append(clazz.getName());

		Session session = this.sessionFactory.getCurrentSession();
		for (String filter:filters) {
			session.enableFilter(filter);
		}

		Query query=session.createQuery(queryBuilder.toString(),clazz);

		pageBy(query, pageBy);
		return query.getResultList();
	}


	@Override
	@Transactional(readOnly = true)
	public List<T> allAndOrderBy(Class<T> clazz, String field, boolean desc, PageBy pageBy) throws Exception {

		Session session = this.sessionFactory.getCurrentSession();
		StringBuilder queryBuilder= new StringBuilder(FROM);
		queryBuilder.append(clazz.getName());
		queryBuilder.append(ALIAS_ORDER);
		queryBuilder.append(field);

		if(desc){
			queryBuilder.append(DESC);
		}else {
			queryBuilder.append(ASC);
		}

		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createQuery(queryBuilder.toString() ,clazz);
		pageBy(query, pageBy);
		return query.getResultList();
	}

	/**
	 * Prend une NamedQuery et retourne un resultat unique
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
		Query query=session.createNamedQuery(queryName,clazz);

		if(StringUtils.isNotEmpty(paramName) && id!=null){
			query.setParameter(paramName, id);
		}

		return (T) query.uniqueResult();
	}

	/**
	 * Prend une NamedQuery et retourne un resultat unique
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
		Query query=session.createNamedQuery(queryName,clazz);

		if(filters!=null){
			for (String filter: filters){
				session.enableFilter(filter);
			}
		}

		if(StringUtils.isNotEmpty(paramName) && id!=null){
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
		Query query=session.createNamedQuery(namedQuery,clazz);

		if(StringUtils.isNotEmpty(paramName) && id!=null){
			query.setParameter(paramName, id);
		}

		pageBy(query, pageBy);

		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findByUserNameQuery(String queryName,Class<T> clazz,Long userId, PageBy pageBy) throws Exception {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createQuery(queryName,clazz);
		query.setParameter(USER_PARAM, userId);
		pageBy(query, pageBy);

		return query.getResultList();
	}

	@Override
	public int countByNameQuery(String queryName, Class<T> clazz, ID id, String paramName,PageBy pageBy) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query = session.createNamedQuery(queryName,clazz);
		query.setParameter(paramName, id);
		pageBy(query, pageBy);
		List results=query.list();
		return CollectionsUtils.isNotEmpty(results)?results.size():0;
	}

	@Override
	@Transactional
	public int count(Class<T> clazz,PageBy pageBy) {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query = session.createQuery(FROM +clazz.getName());
		pageBy(query, pageBy);
		List results=query.list();
		return CollectionsUtils.isNotEmpty(results)?results.size():0;
	}




	@Override
	@Transactional
	public boolean updateDelete(Class<T> clazz, ID id, boolean enableFlushSession) throws BusinessResourceException {

		boolean result=false;

		try{

			//Session session=sessionFactory.getCurrentSession();
			T ent= (T)findById(clazz,id);
			if(ent!=null) {
				/*ent.setCancelled(true);
				session.merge(announce);
				ent = session.get(clazz, id);
				result= (ent!=null) && (ent.isCancelled());*/
			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(T t) {
		Session session= sessionFactory.getCurrentSession();
		session.save(t);
		//session.flush();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={BusinessResourceException.class, Exception.class})
	public T update(T t) throws BusinessResourceException {
		logger.info("Generic update");
		Session session = this.sessionFactory.getCurrentSession();
		if(t!=null){
			session.update(t);
			return t;
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={BusinessResourceException.class, Exception.class})
	public T merge(T t) throws BusinessResourceException {
		logger.info("Generic merge");
		Session session = this.sessionFactory.getCurrentSession();
		if(t!=null){
			session.merge(t);
			return t;
		}
		return null;
	}

	@Override
	public void pageBy(Query query, PageBy pageBy){
		if(pageBy!=null){
			query.setFirstResult(pageBy.getPage());
			query.setMaxResults(pageBy.getSize());
		}
	}




	@Override
	public double calcolateAverage(T t){
		if(t==null)
			return 0.0;
		UserVO user=(UserVO)t;
		int totalRating=0;
		int counterRating=0;
		List<RatingCountVO> ratingCounts = findRatingCounts(user);
		if(CollectionsUtils.isEmpty(ratingCounts)){
			return 0.0;
		}
		for(int i=0;i<ratingCounts.size();i++){
			RatingCountVO ratingCount=ratingCounts.get(i);
			counterRating+=ratingCount.getCount();
			totalRating+=ratingCount.getRating().toValue()*ratingCount.getCount();
		}
		double averageRating = new BigDecimal(((double) totalRating/(double) counterRating),MATH_CONTEXT).doubleValue();
		user.setRating(averageRating);
		return averageRating;
	}

	@Override
	@Transactional
	public List<RatingCountVO> findRatingCounts(UserVO user) {
		Session session= sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createNamedQuery(ReviewVO.RATING,RatingCountVO.class);
		query.setParameter("userId", user);
		return query.getResultList();
	}

}
