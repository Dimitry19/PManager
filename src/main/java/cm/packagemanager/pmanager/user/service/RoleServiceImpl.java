package cm.packagemanager.pmanager.user.service;

import cm.packagemanager.pmanager.user.ent.dao.RoleDAO;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.ws.requests.users.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service("roleService")
public class RoleServiceImpl implements RoleService{

	@Autowired
	RoleDAO roleDAO;

	@PostConstruct
	public void init() {
		System.out.println("Role service starts...." );
	}

	@Transactional
	public RoleVO add(RoleDTO role) throws Exception{

		return roleDAO.addRole(new RoleVO(role.getRole()));
	}
}
