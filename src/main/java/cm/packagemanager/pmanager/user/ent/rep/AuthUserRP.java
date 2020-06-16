package cm.packagemanager.pmanager.user.ent.rep;

import cm.packagemanager.pmanager.user.ent.vo.AuthUserIdVO;
import cm.packagemanager.pmanager.user.ent.vo.AuthUserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AuthUserRP extends JpaRepository<AuthUserVO, AuthUserIdVO> {

	AuthUserVO findByAuthUserId(AuthUserIdVO id);
}
