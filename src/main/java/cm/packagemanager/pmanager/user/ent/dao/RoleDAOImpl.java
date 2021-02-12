package cm.packagemanager.pmanager.user.ent.dao;


import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import org.apache.commons.collections4.IteratorUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;


@Repository
public  class RoleDAOImpl implements RoleDAO {


	private static Logger logger = LoggerFactory.getLogger(RoleDAOImpl.class);


	@Autowired
	private SessionFactory sessionFactory;

	//@Autowired
	//HibernateTransactionManager tx;


	public RoleDAOImpl(){

	}

	@Override
	public RoleVO addRole(RoleVO role){
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
	public RoleVO findByDescription(String description) {

		Session session = sessionFactory.getCurrentSession();

		Query query=session.getNamedQuery(RoleVO.FINDBYDESC);
		if(description.equals(RoleEnum.USER.name())){
			query.setParameter("description", RoleEnum.USER);
		}else {
			query.setParameter("description", RoleEnum.ADMIN);
		}


		List<RoleVO> roles=query.list();
		if(roles!=null && roles.size()>0) {
			return roles.get(0);
		}
		return null;
	}
}
