package cm.packagemanager.pmanager.user.ent.bo;

import cm.packagemanager.pmanager.user.ent.vo.UserIdVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBO extends JpaRepository<UserVO, UserIdVO> {
}
