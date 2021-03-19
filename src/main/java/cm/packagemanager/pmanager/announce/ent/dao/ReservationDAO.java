package cm.packagemanager.pmanager.announce.ent.dao;


import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import cm.packagemanager.pmanager.ws.responses.Response;

public interface ReservationDAO extends CommonDAO {

	public ReservationVO addReservation(ReservationDTO reservation) throws BusinessResourceException;

	boolean deleteReservation(Long id);
}
