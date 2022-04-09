package cm.travelpost.tp.review.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.review.ent.vo.ReviewVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import org.hibernate.query.Query;
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
    public ReviewVO saves(ReviewVO review) throws Exception {
                save(review);
        return (ReviewVO) findById(ReviewVO.class, review.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ReviewVO update(ReviewVO review) throws BusinessResourceException {
        logger.info("Review: update ");
        if (review != null) {
            update(review);
            return review;
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ReviewVO findById(Long id) throws Exception {
        try {
            logger.info("Review: find by id");
            return (ReviewVO) find(ReviewVO.class, id, FilterConstants.CANCELLED);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
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
    public boolean updateDelete(Object o) throws BusinessResourceException, UserException {
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

    @Override
    public String composeQuery(Object o, String alias) throws Exception {
        return null;
    }

    @Override
    public void composeQueryParameters(Object o, Query query) throws Exception {

    }
}
