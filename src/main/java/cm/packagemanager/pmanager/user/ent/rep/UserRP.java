package cm.packagemanager.pmanager.user.ent.rep;

import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRP extends JpaRepository<UserVO, Long> {


	Optional<UserVO> findByUsername(String username);

	@Query(" select u from UserVO u where u.username = ?1")
	Optional<User> findUserWithName(String username);
}
