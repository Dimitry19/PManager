package cm.framework.ds.hibernate.dao;

import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.event.IEvent;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


public abstract class Generic<T, ID extends Serializable> extends CommonGenericDAO {

    public static String where=" where ";
    public String notificationMessagePattern = "{0} {1} {2}";
    public abstract boolean updateDelete(Long id) throws BusinessResourceException, UserException;

    public abstract String composeQuery(Object o, String alias) throws Exception;

    public abstract void composeQueryParameters(Object o, Query query) throws Exception;


    public void fillProps(Map props, Long id, String message, Long userId,Set subscribers) throws Exception {

            props.put(IEvent.PROP_ID,id);
            props.put(IEvent.PROP_MSG,message);
            props.put(IEvent.PROP_USR_ID,userId);
            props.put(IEvent.PROP_SUBSCRIBERS,subscribers);

    }


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
