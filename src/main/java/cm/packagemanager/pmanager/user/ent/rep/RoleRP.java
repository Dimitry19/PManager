package cm.packagemanager.pmanager.user.ent.rep;


import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.stream.Stream;


public interface RoleRP extends JpaRepository<RoleVO, Long> {

	RoleVO findByDescription(String description);

	@Query("select role from RoleVO role")
	Stream<RoleVO> getAllRolesStream();// Java8 Stream : on place la liste des rôles dans un Stream
}