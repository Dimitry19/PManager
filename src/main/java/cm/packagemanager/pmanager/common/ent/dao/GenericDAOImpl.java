package cm.packagemanager.pmanager.common.ent.dao;

import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

public class GenericDAOImpl <T, ID extends Serializable> implements GenericDAO<T, ID> {

	@Autowired
	private SessionFactory sessionFactory;


	@Override
	public Optional<T> find(Class<T> clazz, ID id) {

		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}

		return Optional.ofNullable(sessionFactory.getCurrentSession().find(clazz, id));
	}

	@Override
	public Optional<T> findViaSession(Class<T> clazz, ID id) {

		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}

		Session session = sessionFactory.getCurrentSession();

		return Optional.ofNullable(session.get(clazz, id));
	}

	@Override
	@Transactional
	public void delete(Class<T> clazz, ID id,boolean enableFlushSession) {
		try{
			Session session=sessionFactory.getCurrentSession();

			T ent= (T)findViaSession(clazz,id).get();
			if(ent!=null){
				session.remove(ent);
				if(enableFlushSession){
					session.flush();
					//session.flush(); // Etant donné que la suppression de l'entité UserVO entraine la suppression des autres en
					// entités pas besoin de faire le flush car le flush fait la synchronisation entre l'entité et la session hibernate
					// du coup cree une transaction et enverra en erreur la remove
				}
			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}

	}

	@Override
	@Transactional
	public boolean updateDelete(Class<T> clazz, ID id, boolean enableFlushSession) throws BusinessResourceException {

		boolean result=false;

		try{

			/*Session session=sessionFactory.getCurrentSession();
			T ent= (T)findViaSession(clazz,id).get();
			if(ent!=null) {
				ent.setCancelled(true);
				session.merge(announce);
				ent = session.get(clazz, id);
				result= (ent!=null) && (ent.isCancelled());
			}*/
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}
		return result;
	}
}
