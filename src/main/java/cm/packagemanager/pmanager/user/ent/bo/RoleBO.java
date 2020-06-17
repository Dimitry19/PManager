package cm.packagemanager.pmanager.user.ent.bo;


import cm.packagemanager.pmanager.user.ent.vo.RoleVO;

import java.util.Collection;
import java.util.stream.Stream;

public interface RoleBO {

	RoleVO findByDescription(String description);

	Collection<RoleVO> getAllRoles();

	Stream<RoleVO> getAllRolesStream();
}
