package cm.packagemanager.pmanager.common.ent.vo;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.query.Query;

public  abstract class CommonFilter {


	public abstract <T> String composeQuery( T o, String alias);
	public abstract <T> void composeQueryParameters( T o, Query query);

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

		return o;
	}
}
