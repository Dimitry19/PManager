/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.city.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityHelper extends CommonDAO {

    //@Query(value = CityVO.AUTOCOMPLETE)
    List autocomplete() throws Exception;
}
