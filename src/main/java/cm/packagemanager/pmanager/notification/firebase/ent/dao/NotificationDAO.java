package cm.packagemanager.pmanager.notification.firebase.ent.dao;

import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationVO;

import java.util.List;


public interface NotificationDAO extends CommonDAO {

    List<NotificationVO>  all() throws Exception;
}
