/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.communication.ent.dao;


import cm.travelpost.tp.communication.ent.vo.CommunicationVO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommunicationHelper {

    @Query(value = CommunicationVO.FIND_BY_USER_ID)
    List allCommunicationsBy(@Param("adminId") String username) throws Exception;
}
