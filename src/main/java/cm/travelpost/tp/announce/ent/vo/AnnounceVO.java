/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.announce.ent.vo;


import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;


@Entity
//@SqlResultSetMapping(
//		name = "AnnounceFavoriteMapping",
//		entities = @EntityResult(
//				entityClass = AnnounceVO.class,
//				fields = {
//						@FieldResult(name = "id",        column = "id"),
//						@FieldResult(name = "firstName", column = "firstName"),
//						@FieldResult(name = "lastName", column = "lastName"),
//						@FieldResult(name = "version", column = "version")}))
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

	public static final String ANNOUNCE_SEARCH_SINGLE = "select  distinct  a ";
}
