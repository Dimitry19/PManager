package cm.packagemanager.pmanager.review.ent.dao;

import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public  class ReviewDAOImpl implements ReviewDAO{

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
	public void deleteObject(Object object) throws RecordNotFoundException {

	}
}
