package cm.packagemanager.pmanager.review.ent.dao;


import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewDAO extends CommonDAO {

	Page<ReviewVO> findByUser(UserVO user, Pageable pageable);

	ReviewVO findByUserAndIndex(UserVO user, int index);

	ReviewVO save(ReviewVO review);
}
