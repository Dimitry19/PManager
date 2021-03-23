package cm.packagemanager.pmanager.user.ent.dao;


import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.apache.commons.collections4.IteratorUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


@Repository
public  class RoleDAOImpl extends CommonFilter implements RoleDAO {


	private static Logger logger = LoggerFactory.getLogger(RoleDAOImpl.class);

	public RoleDAOImpl(){

	}

	@Override
	public RoleVO addRole(RoleVO role){
		logger.info("Add role");
		Session session=this.sessionFactory.getCurrentSession();
		if(role!=null){
			session.save(role);
		}
		return role;
	}
	@Override
	public Collection<RoleVO> getAllRoles() {

		Session session=this.sessionFactory.getCurrentSession();

		List<RoleVO> roles = session.createQuery("from RoleVO").list();

		return IteratorUtils.toList(roles.iterator());
	}

	/*@Override
	public Stream<RoleVO> getAllRolesStream() {
		return this.getAllRolesStream();
	}*/

	@Override
	@Transactional
	public RoleVO findByDescription(String description) throws Exception {

		return (RoleVO) findByUniqueResult(RoleVO.FINDBYDESC, RoleVO.class,RoleEnum.fromValue(description),"description");
	}

	@Override
	@Transactional
	public RoleVO find(Long id) {

		return (RoleVO) findById(RoleVO.class,id);

	}

	@Override
	@Transactional
	public Collection<UserVO> usersWithRole(String description) {
		return null;
	}
}
