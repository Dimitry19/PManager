package cm.travelpost.tp.review.ent.service;

import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.review.ent.vo.ReviewVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

	Page<ReviewVO> findByUser(UserVO user, Pageable pageable) throws Exception;

	ReviewVO findByUserAndIndex(UserVO user, int index) throws Exception;

	ReviewVO save(ReviewVO review) throws Exception;

	ReviewVO update(ReviewVO review) throws BusinessResourceException;

	ReviewVO findById(Long id) throws Exception;

	boolean delete(Long id) throws Exception;

}