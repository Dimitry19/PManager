/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.user.ent.dao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHelper {

    //@Query(value = UserVO.ALL)
    List allUsers() throws Exception;
}
