/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.dao;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.exception.NotificationException;
import cm.travelpost.tp.notification.ent.vo.NotificationVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface NotificationDAO extends CommonDAO {

    List<NotificationVO> all() throws Exception;

    List<NotificationVO> notificationToSend(PageBy pageBy) throws NotificationException;

    List<NotificationVO> notificationByAnnounce(long id) throws Exception;

    @Transactional(readOnly = true)
    List<NotificationVO> findByStatus(StatusEnum status);

    @Transactional(readOnly = true)
    List<NotificationVO> findByStatus(StatusEnum status, PageBy pageBy);

    NotificationVO read(Long id) ;

    void readAll(List<Long> ids) throws NotificationException;

    void  deleteOldCompletedNotifications() throws NotificationException;
}
