package cm.packagemanager.pmanager.announce.service;

import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.ValidateReservationDTO;

import java.util.List;

public interface ReservationService {

	ReservationVO addReservation(ReservationDTO reservationDTO) throws Exception;

	ReservationVO updateReservation(UpdateReservationDTO reservationDTO) throws Exception;

	boolean validate(ValidateReservationDTO reservationDTO) throws Exception;

	boolean deleteReservation(Long id)  throws Exception;

	int count(Long id,PageBy pageBy, boolean isUser)  throws Exception;

	List<ReservationVO> findByUser(Long userId, PageBy pageBy) throws Exception;

	List<ReservationVO> findByAnnounce(Long announceId, PageBy pageBy) throws Exception;
}
