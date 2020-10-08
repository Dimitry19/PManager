package cm.packagemanager.pmanager.user.service;

import cm.packagemanager.pmanager.user.ent.dao.RoleDAO;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.ws.requests.users.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
public class RoleService {

	@Autowired
	RoleDAO roleDAO;

	@Transactional
	public RoleVO add(RoleDTO role) {

		return roleDAO.addRole(new RoleVO(role.getRole()));
	}
}
