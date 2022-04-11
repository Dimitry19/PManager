/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

/***
 * Pour utiliser cet interface il faudrait remplir les attributs principaux de la map "props"
 * qui sont : {id},{message},{user}
 */
package cm.travelpost.tp.common.event;

import java.util.HashMap;
import java.util.Map;

public interface IEvent<T> {

    String PROP_ID="id";
    String PROP_MSG="message";
    String PROP_USR_ID ="userId";
    String PROP_ANNOUNCE_ID ="userId";
    String PROP_USR_NAME ="username";
    String PROP_SUBSCRIBERS ="subscribers";

    Map props=new HashMap();



}
