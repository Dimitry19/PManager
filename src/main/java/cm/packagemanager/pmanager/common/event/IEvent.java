/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

/***
 * Pour utiliser cet interface il faudrait remplir les attributs principaux de la map "props"
 * qui sont : {id},{message},{user}
 */
package cm.packagemanager.pmanager.common.event;

import java.util.HashMap;
import java.util.Map;

public interface IEvent<T> {

    static final String PROP_ID="id";
    static final String PROP_MSG="message";
    static final String PROP_USR_ID ="userId";
    static final String PROP_USR_NAME ="username";

    Map props=new HashMap();

    void generateEvent();

}
