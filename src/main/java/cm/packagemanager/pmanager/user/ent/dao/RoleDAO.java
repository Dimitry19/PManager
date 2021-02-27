package cm.packagemanager.pmanager.user.ent.dao;

import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;

import java.util.Collection;


public interface RoleDAO {

	RoleVO findByDescription(String description);
	RoleVO find(String description);

	Collection<UserVO> usersWithRole(String description);

	Collection<RoleVO> getAllRoles();

	RoleVO addRole(RoleVO role);

	//@Query("select role from RoleVO role")
	//Stream<RoleVO> getAllRolesStream();// Java8 Stream : on place la liste des rôles dans un Stream
}
