package cm.packagemanager.pmanager.user.ent.bo;


import cm.packagemanager.pmanager.user.ent.vo.RoleVO;

import java.util.Collection;
import java.util.Optional;


public interface RoleBO {

    Optional<RoleVO> findById(Long id);

    Collection<RoleVO> getAllRoles();

    //Stream<RoleVO> getAllRolesStream();
}
