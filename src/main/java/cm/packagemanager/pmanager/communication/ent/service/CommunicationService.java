/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.communication.ent.service;


import java.util.List;

public interface CommunicationService {

    List allCommunicationsBy(String user) throws Exception;
}
