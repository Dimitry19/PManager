package cm.packagemanager.pmanager.user.ent.rep;

import cm.packagemanager.pmanager.user.ent.vo.UserIdVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRP extends CrudRepository<UserVO, UserIdVO> {


}
