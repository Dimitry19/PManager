package cm.packagemanager.pmanager.common.utils;

import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;

@Component
public class QueryUtils {

	@Autowired
	SessionFactory sessionFactory;

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public  Long  calcolateId(String namedQuery){
		Session sess = sessionFactory.getCurrentSession();
		sess.enableFilter(FilterConstants.CANCELLED);
		Query query= sess.createNamedQuery(namedQuery);

		BigInteger id = (BigInteger) query.uniqueResult();;//(BigInteger)sess.createSQLQuery(sqlnative).getResultList().get(0);

		if(id!=null){
			return new Long(String.valueOf(id))+ new Long(1);
		}
		return new Long(1);
	}
}
