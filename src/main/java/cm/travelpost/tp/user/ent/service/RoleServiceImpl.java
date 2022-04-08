package cm.travelpost.tp.user.ent.service;

import cm.travelpost.tp.user.ent.dao.RoleDAO;
import cm.travelpost.tp.user.ent.vo.RoleVO;
import cm.travelpost.tp.ws.requests.users.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDAO roleDAO;

    @PostConstruct
    public void init() {
        System.out.println("Role service starts....");
    }

    @Transactional
    public RoleVO add(RoleDTO role) throws Exception {

        return roleDAO.addRole(new RoleVO(role.getRole()));
    }
}
