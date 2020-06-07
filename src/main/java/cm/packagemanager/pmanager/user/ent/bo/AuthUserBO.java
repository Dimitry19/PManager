package cm.packagemanager.pmanager.user.ent.bo;

import cm.packagemanager.pmanager.user.ent.vo.AuthUserIdVO;
import cm.packagemanager.pmanager.user.ent.vo.AuthUserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserBO extends JpaRepository<AuthUserVO, AuthUserIdVO> {

}
