package cm.travelpost.tp.announce.ent.dao;


import cm.travelpost.tp.announce.ent.vo.ReservationVO;
import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.common.ent.vo.CommonVO;
import cm.travelpost.tp.common.ent.vo.PageBy;
import cm.travelpost.tp.common.enums.ReservationType;
import cm.travelpost.tp.common.enums.ValidateEnum;
import cm.travelpost.tp.ws.requests.announces.ReservationDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateReservationDTO;
import cm.travelpost.tp.ws.requests.announces.ValidateReservationDTO;

import java.util.List;


public interface ReservationDAO<T extends CommonVO> extends CommonDAO {


    ReservationVO addReservation(ReservationDTO reservation) throws Exception;

    ReservationVO updateReservation(UpdateReservationDTO reservation) throws Exception;

    boolean deleteReservation(Long id) throws Exception;

    ReservationVO validate(ValidateReservationDTO reservationDTO) throws Exception;

    ReservationVO getReservation(long id) throws Exception;

    List<ReservationVO> otherReservations(long id, PageBy pageBy) throws Exception;

    List<ReservationVO> reservationByAnnounce(Long announceId, PageBy pageBy) throws Exception;

    List<T> reservationByUser(Long userId, ReservationType type, PageBy pageBy) throws Exception;

    List<T> reservationByAnnounceAndUser(Long userId, Long announceId, ValidateEnum validateEnum,PageBy pageBy) throws Exception;
}
