package cm.packagemanager.pmanager.common.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigInteger;

public class SQLUtils {

	public static Long  calcolateId(SessionFactory sessionFactory,String sqlnative){
		Session sess = sessionFactory.openSession();
		BigInteger id = (BigInteger)sess.createSQLQuery(sqlnative).getResultList().get(0);
		sess.close();

		if(id!=null){
			return new Long(String.valueOf(id))+ new Long(1);
		}
		return new Long(1);
	}
}
