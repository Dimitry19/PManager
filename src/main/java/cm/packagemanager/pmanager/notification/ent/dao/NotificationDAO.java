/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.packagemanager.pmanager.notification.ent.vo.NotificationVO;

import java.util.List;


public interface NotificationDAO extends CommonDAO {

    List<NotificationVO> all() throws Exception;

    void persistNotification(NotificationVO notification) throws Exception;

    NotificationVO read(Long id) throws Exception;
}
