/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.user.event;

import cm.packagemanager.pmanager.announce.event.AnnounceEvent;
import cm.packagemanager.pmanager.notification.enums.NotificationType;

import java.util.Date;

public class UserEvent extends AnnounceEvent {

    public UserEvent(Date date, NotificationType type) {
        super(date, type);
    }
}
