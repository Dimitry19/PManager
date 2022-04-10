package cm.travelpost.tp.user.ent.service;

import cm.travelpost.tp.user.ent.vo.RoleVO;
import cm.travelpost.tp.ws.requests.users.RoleDTO;

public interface RoleService {

    RoleVO add(RoleDTO role) throws Exception;
}
