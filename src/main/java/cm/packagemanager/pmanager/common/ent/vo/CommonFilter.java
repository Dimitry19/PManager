package cm.packagemanager.pmanager.common.ent.vo;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;

public  class CommonFilter {

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
