package cm.packagemanager.pmanager.user.ent.dao;

import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.apache.commons.collections4.IteratorUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;


@Repository
public  class RoleDAOImpl implements RoleDAO {

	@Autowired
	private SessionFactory sessionFactory;



	public RoleDAOImpl(){

	}

	@Override
	public void addRole(RoleVO role){
		Session session=this.sessionFactory.getCurrentSession();
		if(role!=null){
			session.save(role);
		}

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
		return null;
	}
}
