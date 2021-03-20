package cm.packagemanager.pmanager.announce.ent.dao;


import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.ValidateReservationDTO;


public interface ReservationDAO extends CommonDAO {

	int count(String queryName,Long id, String paramName,PageBy pageBy)throws Exception   ;

	ReservationVO addReservation(ReservationDTO reservation) throws BusinessResourceException;

	ReservationVO updateReservation(UpdateReservationDTO reservation) throws BusinessResourceException;

	boolean deleteReservation(Long id) throws BusinessResourceException;

	boolean validate(ValidateReservationDTO reservationDTO) throws BusinessResourceException;
}
