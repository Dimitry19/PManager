/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.dao;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.NotificationException;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.notification.ent.vo.NotificationVO;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificationDAOImpl extends Generic implements NotificationDAO {

    private static Logger logger = LoggerFactory.getLogger(NotificationDAOImpl.class);

    @Override
    public List<NotificationVO> all() throws Exception {
        return all(NotificationVO.class);
    }


    @Override
    public List<NotificationVO> notificationToSend(PageBy pageBy) throws NotificationException {
        return findByStatus(StatusEnum.VALID);
    }

    @Override
    public List<NotificationVO>  notificationByAnnounce(long id) throws Exception {
        return findBy(NotificationVO.FINDBYANNOUNCEID,NotificationVO.class,id, ANNOUNCE_PARAM,null);
    }

    @Override
    public NotificationVO read(Long id)  {
        NotificationVO notification=(NotificationVO) findById(NotificationVO.class,id);

        if(notification==null){
            return null;
        }
        notification.setStatus(StatusEnum.COMPLETED);
        update(notification);
        return notification;
    }

    @Override
    public  void readAll(List<Long> ids) throws NotificationException {
        if (CollectionsUtils.isNotEmpty(ids)){
           ids.stream().filter(id->findById(NotificationVO.class, id)!=null)
                .forEach(id->{
                      NotificationVO notification = (NotificationVO) findById(NotificationVO.class, id);
                      notification.setStatus(StatusEnum.COMPLETED);
            } );
        }
    }


    @Override
    public void deleteOldCompletedNotifications() throws NotificationException {

        List<NotificationVO> notifications= findByStatus(StatusEnum.COMPLETED);

         if(CollectionsUtils.isNotEmpty(notifications)){
           notifications.stream().forEach(this::delete);
        }
    }

    @Override
    public boolean updateDelete(Object o) throws BusinessResourceException {

        if(o instanceof NotificationVO){

            NotificationVO notification = (NotificationVO)o;
            notification.setStatus(StatusEnum.COMPLETED);
            update(notification);
        }

        return Boolean.TRUE;
    }

    public List<NotificationVO> findByStatus(StatusEnum status){

        try{
            StringBuilder queryBuilder = new StringBuilder(NotificationVO.byStatusNativeQuery);
            Session session = getCurrentSession();
            queryBuilder.append(APPLICE);
            queryBuilder.append(status.name());
            queryBuilder.append(APPLICE);

            Query query = session.createNativeQuery(queryBuilder.toString()).addEntity(NotificationVO.class);

            return query.getResultList();
        }catch (Exception e){
            logger.error("Erreur durant la recuperation des notification byStatus {}", e);
        }
        return new ArrayList<>();
    }
}
