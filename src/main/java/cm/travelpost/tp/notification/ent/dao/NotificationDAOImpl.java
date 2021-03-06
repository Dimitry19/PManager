/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.notification.ent.vo.NotificationVO;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationDAOImpl extends Generic implements NotificationDAO {


    @Override
    public List<NotificationVO> all() throws Exception {
        return (List<NotificationVO>) all(NotificationVO.class);
    }

    @Override
    public void persistNotification(NotificationVO notification) throws Exception {
        persist(notification);
    }

    @Override
    public NotificationVO read(Long id) throws Exception {
         NotificationVO notification=(NotificationVO) findById(NotificationVO.class,id);

         if(notification==null){
             return null;
         }
         notification.setStatus(StatusEnum.COMPLETED);
         update(notification);
        return notification;
    }

    @Override
    public boolean updateDelete(Object id) throws BusinessResourceException, UserException {
        return false;
    }

}
