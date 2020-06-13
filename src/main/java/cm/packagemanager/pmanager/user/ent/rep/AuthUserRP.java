package cm.packagemanager.pmanager.user.ent.rep;

import cm.packagemanager.pmanager.user.ent.vo.AuthUserIdVO;
import cm.packagemanager.pmanager.user.ent.vo.AuthUserVO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRP extends CrudRepository<AuthUserVO, AuthUserIdVO> {

	AuthUserVO findByAuthUserId(AuthUserIdVO id);
}
