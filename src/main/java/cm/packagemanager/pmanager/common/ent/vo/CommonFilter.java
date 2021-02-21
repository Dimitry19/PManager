package cm.packagemanager.pmanager.common.ent.vo;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.query.Query;

public  abstract class CommonFilter {


	public abstract  String composeQuery( Object o, String alias);
	public abstract void composeQueryParameters( Object o, Query query);

	public   Object manualFilter(Object o) {

		if (o instanceof UserVO){
			 UserVO user =(UserVO)o;
			if(!user.isCancelled() && user.getActive().equals(1)){
				return user;
			}else return null;

		}

		if (o instanceof AnnounceVO){
			AnnounceVO announce =(AnnounceVO)o;
			if(!announce.isCancelled()){
				return announce;
			}else return null;
		}

		if (o instanceof MessageVO){
			MessageVO message =(MessageVO)o;
			if(!message.isCancelled()){
				return message;
			}else return null;
		}

		return o;
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
