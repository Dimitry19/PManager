package cm.packagemanager.pmanager.review.ent.dao;


import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewDAO extends CommonDAO {

	Page<ReviewVO> findByUser(UserVO user, Pageable pageable) throws Exception;

	ReviewVO findByUserAndIndex(UserVO user, int index)throws Exception;

	ReviewVO save(ReviewVO review) throws Exception;
}
