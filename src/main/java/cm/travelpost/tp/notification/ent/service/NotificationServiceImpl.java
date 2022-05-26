/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.service;

import cm.travelpost.tp.common.exception.NotificationException;
import cm.travelpost.tp.notification.ent.dao.NotificationDAO;
import cm.travelpost.tp.notification.ent.vo.NotificationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationDAO dao;


    @Override
    public NotificationVO read(Long id) throws Exception {
        if(logger.isDebugEnabled()){
            logger.info("Read notification with id {}", id);
        }
        return dao.read(id);
    }

    @Override
    public void  readAll(List<Long> ids) throws NotificationException {
        if(logger.isDebugEnabled()){
            logger.info("Read all notifications  {}", ids);
        }
        dao.readAll(ids);
    }
}
