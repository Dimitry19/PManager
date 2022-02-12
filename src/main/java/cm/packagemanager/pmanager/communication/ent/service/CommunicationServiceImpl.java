/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.communication.ent.service;



import cm.packagemanager.pmanager.communication.ent.dao.CommunicationDAO;
import cm.packagemanager.pmanager.communication.ent.dao.CommunicationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommunicationServiceImpl implements CommunicationService{

    @Autowired
    CommunicationDAO dao;

    //@Autowired
    CommunicationHelper helper;


    @Override
    public List allCommunicationsBy(String user) throws Exception {
        return helper.allCommunicationsBy(user);
    }
}
