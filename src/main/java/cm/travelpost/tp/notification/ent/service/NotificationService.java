/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.service;

import cm.travelpost.tp.common.exception.NotificationException;
import cm.travelpost.tp.notification.ent.vo.NotificationVO;

import java.util.List;

public interface NotificationService {

    NotificationVO read(Long id) throws Exception;
    void readAll(List<Long> ids) throws NotificationException;
}
