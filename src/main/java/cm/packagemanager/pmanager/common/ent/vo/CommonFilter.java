package cm.packagemanager.pmanager.common.ent.vo;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.ent.dao.GenericDAOImpl;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.query.Query;

import java.io.Serializable;


public   class CommonFilter<T, ID extends Serializable> extends GenericDAOImpl {


	public boolean updateDelete(Long id) throws BusinessResourceException, UserException {
		return false;
	}

	public String composeQuery(Object o, String alias) throws Exception {

		return null;
	}

	public void composeQueryParameters(Object o, Query query) throws Exception {
	}


	public void buildAndOr(StringBuilder hql,boolean addCondition,boolean andOrOr){
		if (addCondition){
			if (!andOrOr) {
				hql.append(Constants.OR);
			}else{
				hql.append(Constants.AND);
			}
		}
	}
}
