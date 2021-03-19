package cm.packagemanager.pmanager.announce.service;

import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;

public interface ReservationService {

	public ReservationVO addReservation(ReservationDTO reservationDTO) throws Exception;

	boolean deleteReservation(Long id)  throws Exception;
}
