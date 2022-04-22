/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.announce.ent.vo;


import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries(value = {
		@NamedQuery(name = AnnounceVO.FINDBYUSER, query = "select a from AnnounceVO a where a.user.id =:userId order by a.startDate desc"),
		@NamedQuery(name = AnnounceVO.FINDBYTYPE, query = "select a from AnnounceVO a where a.announceType =:announceType order by a.startDate desc"),
		@NamedQuery(name = AnnounceVO.FINDBYTRANSPORT, query = "select a from AnnounceVO a where a.transport =:transport order by a.startDate desc"),
})
@DiscriminatorValue("VALID")
@DiscriminatorOptions(insert = true,force=true)
public class AnnounceVO extends AnnounceMasterVO {

	public static final String FINDBYUSER = "cm.travelpost.tp.announce.ent.vo.AnnounceVO.findByUser";
	public static final String FINDBYTYPE = "cm.travelpost.tp.announce.ent.vo.AnnounceVO.findByType";
	public static final String FINDBYTRANSPORT = "cm.travelpost.tp.announce.ent.vo.AnnounceVO.findByTransport";
	public static final String SQL_FIND_BY_USER = " FROM AnnounceVO a where a.user.id =:userId order by a.startDate desc";
	public static final String ANNOUNCE_SEARCH = "select  distinct  a from AnnounceVO  as a join a.categories as c ";
}
