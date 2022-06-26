package cm.travelpost.tp.review.ent.service;

import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.review.ent.dao.ReviewDAO;
import cm.travelpost.tp.review.ent.vo.ReviewVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewDAO dao;

    @Override
    public Page<ReviewVO> findByUser(UserVO user, Pageable pageable) throws Exception {
        return dao.findByUser(user,pageable);
    }

    @Override
    public ReviewVO findByUserAndIndex(UserVO user, int index) throws Exception {
        return dao.findByUserAndIndex(user,index);
    }

    @Override
    public ReviewVO save(ReviewVO review) throws Exception {
        dao.save(review);
        return (ReviewVO) dao.findById(ReviewVO.class, review.getId());
    }

    @Override
    public ReviewVO update(ReviewVO review) throws BusinessResourceException {
        return dao.update(review);
    }

    @Override
    public ReviewVO findById(Long id) throws Exception {
        return (ReviewVO) dao.findById(ReviewVO.class, id);
    }

    @Override
    public boolean delete(Long id) throws Exception {
        return dao.delete(id);
    }
}
