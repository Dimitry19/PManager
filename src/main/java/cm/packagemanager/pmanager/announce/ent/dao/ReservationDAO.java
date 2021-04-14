package cm.packagemanager.pmanager.announce.ent.dao;


import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.ValidateReservationDTO;

import java.util.List;


public interface ReservationDAO extends CommonDAO {


	ReservationVO addReservation(ReservationDTO reservation) throws BusinessResourceException, Exception;

	ReservationVO updateReservation(UpdateReservationDTO reservation) throws Exception;

	boolean deleteReservation(Long id) throws Exception;

	ReservationVO validate(ValidateReservationDTO reservationDTO) throws Exception;

	ReservationVO getReservation(long id) throws Exception;

	List<ReservationVO> otherReservations(long id,PageBy pageBy) throws Exception;
}
