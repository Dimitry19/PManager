package cm.packagemanager.pmanager.common.ent.dao;

import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import org.hibernate.query.Query;

import java.io.Serializable;


public abstract class Generic<T, ID extends Serializable> extends CommonGenericDAO {


    public abstract boolean updateDelete(Long id) throws BusinessResourceException, UserException;

    public abstract String composeQuery(Object o, String alias) throws Exception;

    public abstract void composeQueryParameters(Object o, Query query) throws Exception;


    public void buildAndOr(StringBuilder hql, boolean addCondition, boolean andOrOr) {
        if (addCondition) {
            if (!andOrOr) {
                hql.append(Constants.OR);
            } else {
                hql.append(Constants.AND);
            }
        }
    }
}
