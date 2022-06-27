/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.announce.ent.vo;


import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;


@Entity
@SqlResultSetMapping(
		//https://thorben-janssen.com/result-set-mapping-hibernate-specific-mappings/
	    name = AnnounceVO.ANNOUNCE_FAVORITE_MAPPING,
		/*entities = {
			@EntityResult(
				entityClass = AnnounceVO.class,
					fields = {
							@FieldResult(name = "id",   column = "id"),
							@FieldResult(name = "code", column = "code"),
							@FieldResult(name = "announceId.token", column = "token"),
							@FieldResult(name = "cancelled", column = "cancelled"),
							@FieldResult(name = "dateCreated", column = "dateCreated"),
							@FieldResult(name = "lastUpdated", column = "lastUpdated"),
							@FieldResult(name = "announce_type", column = "announce_type"),
							@FieldResult(name = "arrival", column = "arrival"),
							@FieldResult(name = "departure", column = "departure"),
							@FieldResult(name = "description", column = "description"),
							@FieldResult(name = "end_date", column = "end_date"),
							@FieldResult(name = "gold_price", column = "gold_price"),
							@FieldResult(name = "prenium_price", column = "prenium_price"),
							@FieldResult(name = "price", column = "price"),
							@FieldResult(name = "start_date", column = "start_date"),
							@FieldResult(name = "status", column = "status"),
							@FieldResult(name = "estimate_value", column = "estimate_value"),
							@FieldResult(name = "image_id", column = "image_id"),
							@FieldResult(name = "remain_weight", column = "remain_weight"),
							@FieldResult(name = "transport", column = "transport"),
							@FieldResult(name = "r_user_id", column = "r_user_id"),
							@FieldResult(name = "countreservation", column = "count_reservation"),
							@FieldResult(name = "weight", column = "weight")
						}
					)
				}*/
		columns={
			@ColumnResult(name="id",type = Long.class),
			@ColumnResult(name="code", type = String.class),
			@ColumnResult(name="token", type = String.class),
			@ColumnResult(name="cancelled", type = boolean.class),
			@ColumnResult(name="dateCreated", type = Timestamp.class),
			@ColumnResult(name="lastUpdated", type = Timestamp.class),
			@ColumnResult(name="announce_type", type = String.class),
			@ColumnResult(name="arrival", type = String.class),
			@ColumnResult(name="departure", type = String.class),
			@ColumnResult(name="description", type = String.class),
			@ColumnResult(name="end_date", type = Date.class),
			@ColumnResult(name="gold_price", type = BigDecimal.class),
			@ColumnResult(name="prenium_price", type = BigDecimal.class),
			@ColumnResult(name="price", type = BigDecimal.class),
			@ColumnResult(name="start_date", type = Date.class),
			@ColumnResult(name="status", type = String.class),
			@ColumnResult(name="estimate_value", type = BigDecimal.class),
			@ColumnResult(name="count_reservation", type = Integer.class),
			@ColumnResult(name="remain_weight", type = BigDecimal.class),
			@ColumnResult(name="transport", type = String.class),
			@ColumnResult(name="weight", type = BigDecimal.class),
			@ColumnResult(name="image_id", type = Long.class),
			@ColumnResult(name="r_user_id", type = Long.class)
		}
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
	public static final String ANNOUNCES_FAVORIS_BY_USER_NQ = "select a.id , a.code,a.token, a.cancelled, a.dateCreated ,"
			+" a.lastUpdated ,a.token , a.announce_type, a.arrival , a.departure ,"
			+ " a.description ,a.end_date,a.gold_price , a.prenium_price ,a.price ,a.start_date,"
			+ "a.status,a.transport ,a.weight,a.r_user_id,a.remain_weight,"
			+" a.image_id, a.estimate_value,(select coalesce(count(r.r_announce_id),0)  from reservation r where a.id= r.r_announce_id and r.cancelled='0') as count_reservation from  announce a inner join user_announces_favoris "
			+" uaf on a.id=uaf.announce_id  where uaf.user_id=:userId  and a.status='VALID'";
}
