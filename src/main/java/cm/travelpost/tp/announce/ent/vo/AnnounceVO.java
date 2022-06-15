/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.announce.ent.vo;


import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;


@Entity
@SqlResultSetMapping(
	name = AnnounceVO.ANNOUNCE_FAVORITE_MAPPING,
	entities = @EntityResult(
		entityClass = AnnounceVO.class,
			fields = {
				@FieldResult(name = "id",   column = "id"),
				@FieldResult(name = "code", column = "code"),
				@FieldResult(name = "cancelled", column = "cancelled"),
				@FieldResult(name = "dateCreated", column = "dateCreated"),
				@FieldResult(name = "lastUpdated", column = "lastUpdated"),
				@FieldResult(name = "announce_type", column = "announce7_4_0_"),
				@FieldResult(name = "arrival", column = "arrival"),
				@FieldResult(name = "departure", column = "departure"),
				@FieldResult(name = "description", column = "description"),
				@FieldResult(name = "end_date", column = "end_dat12_4_0_"),
				@FieldResult(name = "gold_price", column = "gold_pr14_4_0_"),
				@FieldResult(name = "prenium_price", column = "prenium15_4_0_"),
				@FieldResult(name = "price", column = "price"),
				@FieldResult(name = "start_date", column = "start_d18_4_0_"),
				@FieldResult(name = "status", column = "status"),
				@FieldResult(name = "estimate_value", column = "estimat13_4_0_"),
				@FieldResult(name = "image_id", column = "image_i21_4_0_"),
				@FieldResult(name = "remain_weight", column = "remain_17_4_0_"),
				@FieldResult(name = "transport", column = "transpo19_4_0_"),
				@FieldResult(name = "r_user_id", column = "r_user_22_4_0_"),
				@FieldResult(name = "countreservation", column = "formula1_0_"),
				@FieldResult(name = "weight", column = "weight20_4_0_")
			}
		)
	)
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
	public static final String FIND_BY_USER_SQL = " FROM AnnounceVO a where a.user.id =:userId order by a.startDate desc";

	public static final String ANNOUNCE_SEARCH_SINGLE = "select  distinct  a ";
	public static final String ANNOUNCE_FAVORITE_MAPPING= "AnnounceFavoriteMapping";
	public static final String ANNOUNCES_FAVORIS_BY_USER_NQ = "select a.id as id, a.code as code, a.cancelled as cancelled, a.dateCreated as dateCreated, a.lastUpdated as lastUpdated ,a.token as token6_4_0_,"
			+" a.announce_type as announce7_4_0_, a.arrival as arrival, a.departure as departure, a.description as description,a.end_date as end_dat12_4_0_,a.gold_price as gold_pr14_4_0_,"
			+" a.prenium_price as prenium15_4_0_,a.price as price,a.start_date as start_d18_4_0_,a.status as status,a.transport as transpo19_4_0_,a.weight as weight20_4_0_,a.r_user_id as r_user_22_4_0_,a.remain_weight as remain_17_4_0_,"
			+" a.image_id as image_i21_4_0_, a.estimate_value as estimat13_4_0_,(select coalesce(count(r.r_announce_id),0)  from reservation r where a.id= r.r_announce_id and r.cancelled='0') as formula1_0_ from  announce a inner join user_announces_favoris "
			+" uaf on a.id=uaf.announce_id  where uaf.user_id=:userId ";
}
