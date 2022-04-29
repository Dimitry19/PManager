package cm.travelpost.tp.user.ent.service;

import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.user.ent.dao.RoleDAO;
import cm.travelpost.tp.user.ent.vo.RoleVO;
import cm.travelpost.tp.ws.requests.users.RoleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

    private static Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);


    @Autowired
    RoleDAO roleDAO;

    @PostConstruct
    public void init() {
        log.info("Role service starts at {}", DateUtils.getDateStandardHMS(new Date()));
        if(log.isDebugEnabled()){
            log.debug("Role service starts at {}", DateUtils.getDateStandardHMS(new Date()));
        }


    }

    @Transactional
    public RoleVO add(RoleDTO role) throws Exception {

        return roleDAO.addRole(new RoleVO(role.getRole()));
    }
}
