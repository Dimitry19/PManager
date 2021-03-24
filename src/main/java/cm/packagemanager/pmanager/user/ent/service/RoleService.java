package cm.packagemanager.pmanager.user.ent.service;

import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.ws.requests.users.RoleDTO;

public interface RoleService {

	public RoleVO add(RoleDTO role) throws Exception;
}
