/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.firebase.ent.dao;

import cm.packagemanager.pmanager.common.ent.dao.Generic;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationVO;
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
    public void persist(NotificationVO notification) throws Exception {
        persist(notification);
    }

    @Override
    public NotificationVO read(Long id) throws Exception {
         NotificationVO notification=(NotificationVO) findById(NotificationVO.class,id);

         notification.setStatus(StatusEnum.COMPLETED);
         update(notification);
        return notification;
    }

    @Override
    public boolean updateDelete(Long id) throws BusinessResourceException, UserException {
        return false;
    }

    @Override
    public String composeQuery(Object o, String alias) throws Exception {
        return null;
    }

    @Override
    public void composeQueryParameters(Object o, Query query) throws Exception {

    }
}
