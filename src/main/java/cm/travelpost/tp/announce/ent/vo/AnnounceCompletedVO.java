/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.announce.ent.vo;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries(value = {
		@NamedQuery(name = AnnounceCompletedVO.FINDBYUSER, query = "select a from AnnounceCompletedVO a where a.user.id =:userId order by a.startDate desc"),
		@NamedQuery(name = AnnounceCompletedVO.FINDBYTYPE, query = "select a from AnnounceCompletedVO a where a.announceType =:announceType order by a.startDate desc"),
		@NamedQuery(name = AnnounceCompletedVO.FINDBYTRANSPORT, query = "select a from AnnounceCompletedVO a where a.transport =:transport order by a.startDate desc"),
})
@DiscriminatorValue("COMPLETED")
public class AnnounceCompletedVO extends AnnounceMasterVO {
	public static final String FINDBYUSER = "cm.travelpost.tp.announce.ent.vo.AnnounceCompletedVO.findByUser";
	public static final String FINDBYTYPE = "cm.travelpost.tp.announce.ent.vo.AnnounceCompletedVO.findByType";
	public static final String FINDBYTRANSPORT = "cm.travelpost.tp.announce.ent.vo.AnnounceCompletedVO.findByTransport";
	public static final String SQL_FIND_BY_USER = " FROM AnnounceCompletedVO a where a.user.id =:userId order by a.startDate desc";
	public static final String ANNOUNCE_SEARCH = "select  distinct  a from AnnounceCompletedVO  as a join a.categories as c ";



}
