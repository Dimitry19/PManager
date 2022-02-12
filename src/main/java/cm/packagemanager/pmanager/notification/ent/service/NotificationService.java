/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.ent.service;

import cm.packagemanager.pmanager.notification.ent.vo.NotificationVO;

public interface NotificationService {

    NotificationVO read(Long id) throws Exception;


}
