/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.user.event;

import cm.travelpost.tp.announce.event.AnnounceEvent;
import cm.travelpost.tp.notification.enums.NotificationType;

import java.util.Date;

public class UserEvent extends AnnounceEvent {

    public UserEvent(Date date, NotificationType type) {
        super(date, type);
    }
}
