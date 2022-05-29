package cm.travelpost.tp.user.ent.dao;


import cm.framework.ds.hibernate.dao.CommonGenericDAO;
import cm.travelpost.tp.user.ent.vo.RoleVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.user.enums.RoleEnum;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


@Repository
public class RoleDAOImpl extends CommonGenericDAO implements RoleDAO {


    private static Logger logger = LoggerFactory.getLogger(RoleDAOImpl.class);

    public RoleDAOImpl() {

    }

    @Override
    public RoleVO addRole(RoleVO role) {
        logger.info("Add role");
        if (role != null) {
            persist(role);
        }
        return role;
    }

    @Override
    public Collection<RoleVO> getAllRoles() throws Exception {

        List<RoleVO> roles = all(RoleVO.class);

        return IteratorUtils.toList(roles.iterator());
    }


    @Override
    @Transactional
    public RoleVO findByDescription(String description) throws Exception {

        return (RoleVO) findByUniqueResult(RoleVO.FINDBYDESC, RoleVO.class, RoleEnum.fromValue(description), "description");
    }

    @Override
    @Transactional
    public RoleVO find(Long id) {

        return (RoleVO) findById(RoleVO.class, id);
    }

    @Override
    @Transactional
    public Collection<UserVO> usersWithRole(String description) {
        return null;
    }
}
