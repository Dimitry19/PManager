/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.dao;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.notification.ent.vo.NotificationVO;

import java.util.List;


public interface NotificationDAO extends CommonDAO {

    List<NotificationVO> all() throws Exception;

    List<NotificationVO> notificationToSend(PageBy pageBy) throws Exception;

    NotificationVO read(Long id) ;

    void readAll(List<Long> ids) ;

   void  deleteOldCompletedNotifications() throws Exception;
}
