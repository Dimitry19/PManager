package cm.travelpost.tp.review.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.review.ent.vo.ReviewVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ReviewDAOImpl extends Generic implements ReviewDAO {

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
    @Transactional(propagation = Propagation.REQUIRED)
    public ReviewVO update(ReviewVO review) throws BusinessResourceException {
        logger.info("Review: update ");
        if (review != null) {
            update(review);
        }
        return review;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean delete(Long id) throws Exception {
        try {
            return updateDelete(id);//delete(ReviewVO.class,id,true);
        } catch (Exception e) {
            throw new BusinessResourceException(e.getMessage());
        }
    }


    @Override
    public boolean updateDelete(Object o) throws BusinessResourceException {
        boolean result = false;

        try {

            Long id = (Long)o;
            ReviewVO review = (ReviewVO) findByIdViaSession(ReviewVO.class, id).get();
            if (review != null) {
                review.cancel();
                review = (ReviewVO) merge(review);
                result = (review != null) && (review.isCancelled());
            }
        } catch (Exception e) {
            throw new BusinessResourceException(e.getMessage());
        }
        return result;
    }

}
