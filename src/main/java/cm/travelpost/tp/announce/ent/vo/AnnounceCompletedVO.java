/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.announce.ent.vo;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("COMPLETED")
//@Where(clause = FilterConstants.FILTER_ANNOUNCE_CANC_COMPLETED)
public class AnnounceCompletedVO extends AnnounceMasterVO {

}
