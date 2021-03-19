package cm.packagemanager.pmanager.announce.service;

import cm.packagemanager.pmanager.announce.ent.dao.ReservationDAO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService{

	@Autowired
	ReservationDAO reservationDAO;

	@Override
	public ReservationVO addReservation(ReservationDTO reservationDTO)  throws Exception{
		return reservationDAO.addReservation(reservationDTO);
	}
	@Override
	public boolean deleteReservation(Long id)  throws Exception{
		return reservationDAO.deleteReservation(id);
	}
}
