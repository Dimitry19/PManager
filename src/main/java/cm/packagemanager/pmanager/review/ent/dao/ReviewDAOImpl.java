package cm.packagemanager.pmanager.review.ent.dao;

import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.dao.UserDAOImpl;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public  class ReviewDAOImpl implements ReviewDAO{

	private static Logger logger = LoggerFactory.getLogger(ReviewDAOImpl.class);


	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Page<ReviewVO> findByUser(UserVO user, Pageable pageable) {
		return null;//session.find();
	}

	@Override
	public ReviewVO findByUserAndIndex(UserVO user, int index) {
		return null;
	}

	@Override
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
	public boolean delete(Long id) throws Exception {
		try {
			Session session = sessionFactory.getCurrentSession();
			ReviewVO review = findById(id);
			if (review != null) {
				session.remove(review);
				session.flush();
				return true;
			}
		} catch (Exception e) {
			throw new BusinessResourceException(e.getMessage());
		}
		return false;
	}

	@Override
	public void deleteObject(Object object) throws RecordNotFoundException {

	}
}
