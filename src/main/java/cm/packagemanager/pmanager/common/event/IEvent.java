/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.common.event;

import java.util.Map;

public interface IEvent<T> {

    void generateEvent(Map props);

}
