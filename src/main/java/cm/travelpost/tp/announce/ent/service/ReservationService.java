package cm.travelpost.tp.announce.ent.service;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.enums.FindBy;
import cm.travelpost.tp.announce.ent.vo.ReservationVO;
import cm.travelpost.tp.announce.enums.ReservationType;
import cm.travelpost.tp.ws.requests.announces.ReservationDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateReservationDTO;
import cm.travelpost.tp.ws.requests.announces.ValidateReservationDTO;

import java.util.List;

public interface ReservationService {

    ReservationVO addReservation(ReservationDTO reservationDTO) throws Exception;

    ReservationVO updateReservation(UpdateReservationDTO reservationDTO) throws Exception;

    ReservationVO getReservation(long id) throws Exception;

    ReservationVO validate(ValidateReservationDTO reservationDTO) throws Exception;

    boolean deleteReservation(Long id) throws Exception;

    int count(Long id, PageBy pageBy, FindBy findBy, ReservationType type) throws Exception;

    List<ReservationVO> reservationsByUser(Long userId, ReservationType type, PageBy pageBy) throws Exception;

    List<ReservationVO> receivedReservations(Long userId, PageBy pageBy) throws Exception;

    List<ReservationVO> reservationsByAnnounce(Long announceId, PageBy pageBy) throws Exception;
}
