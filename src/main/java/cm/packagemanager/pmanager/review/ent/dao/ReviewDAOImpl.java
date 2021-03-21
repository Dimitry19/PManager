package cm.packagemanager.pmanager.review.ent.dao;

import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public  class ReviewDAOImpl extends CommonFilter implements ReviewDAO{

	private static Logger logger = LoggerFactory.getLogger(ReviewDAOImpl.class);

	@Override
	public Page<ReviewVO> findByUser(UserVO user, Pageable pageable) {
		return null;//session.find();
	}

	@Override
	public ReviewVO findByUserAndIndex(UserVO user, int index) {
		return null;
	}

	@Override
	@Transactional(propagation = Propagation. REQUIRED)
	public ReviewVO save(ReviewVO review) throws Exception{
		Session session = sessionFactory.getCurrentSession();
		session.save(review);
		return session.get(ReviewVO.class,review.getId());
	}

	@Override
	@Transactional(propagation = Propagation. REQUIRED)
	public ReviewVO update(ReviewVO review) throws BusinessResourceException {
		  logger.info("Review: update ");
			Session session = this.sessionFactory.getCurrentSession();
			if(review!=null){
				session.update(review);
				return review;
			}
			return null;
	}

	@Override
	@Transactional(propagation = Propagation. REQUIRED)
	public ReviewVO findById(Long id) throws Exception {
		try{
			logger.info("Review: find by id");
			Session session = sessionFactory.getCurrentSession();
			session.enableFilter(FilterConstants.CANCELLED);
			return  (ReviewVO) session.find(ReviewVO.class,id);
		}catch (Exception e){
			throw  new Exception(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation. REQUIRED)
	public boolean delete(Long id) throws Exception {
		try {
			return updateDelete(id);//delete(ReviewVO.class,id,true);
		} catch (Exception e) {
			throw new BusinessResourceException(e.getMessage());
		}
	}


	@Override
	public boolean updateDelete(Long id) throws BusinessResourceException, UserException {
		boolean result=false;

		try{

			Session session=sessionFactory.getCurrentSession();
			ReviewVO review= (ReviewVO) findByIdViaSession(ReviewVO.class,id).get();
			if(review!=null) {
				review.setCancelled(true);
				session.merge(review);
				review = session.get(ReviewVO.class, id);
				result= (review!=null) && (review.isCancelled());
			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}
		return result;
	}

	@Override
	public String composeQuery(Object o, String alias) throws Exception {
		return null;
	}

	@Override
	public void composeQueryParameters(Object o, Query query) throws Exception {

	}
}
