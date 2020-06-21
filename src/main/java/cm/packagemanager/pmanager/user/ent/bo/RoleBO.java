package cm.packagemanager.pmanager.user.ent.bo;


import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;


public interface RoleBO {

	Optional<RoleVO> findById(Long id);

	Collection<RoleVO> getAllRoles();

	//Stream<RoleVO> getAllRolesStream();
}
