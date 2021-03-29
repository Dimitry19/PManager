package cm.packagemanager.pmanager.announce.ent.service;

import cm.packagemanager.pmanager.announce.ent.dao.ReservationDAO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.ValidateReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService{

	@Autowired
	ReservationDAO reservationDAO;

	@Override
	public ReservationVO addReservation(ReservationDTO reservationDTO)  throws Exception{
		return reservationDAO.addReservation(reservationDTO);
	}

	@Override
	public ReservationVO updateReservation(UpdateReservationDTO reservationDTO) throws Exception {
		return reservationDAO.updateReservation(reservationDTO);
	}

	@Override
	public ReservationVO getReservation(long id) throws Exception {
		return reservationDAO.getReservation(id);
	}

	@Override
	public boolean validate(ValidateReservationDTO reservationDTO) throws Exception {
		return reservationDAO.validate(reservationDTO);
	}

	@Override
	public boolean deleteReservation(Long id) throws Exception {
		return reservationDAO.deleteReservation(id);
	}

	@Override
	public int count(Long id,PageBy pageBy, boolean isUser)  throws Exception{
		return (isUser)?reservationDAO.count(ReservationVO.FINDBYUSER,id,"userId", pageBy):
		reservationDAO.count(ReservationVO.FINDBYANNOUNCE,id,"announceId", pageBy);
	}

	@Override
	public List<ReservationVO> findByUser(Long userId, PageBy pageBy) throws Exception {

		return reservationDAO.findByUser(ReservationVO.class,userId,pageBy);
	}

	@Override
	public List<ReservationVO> findByAnnounce(Long announceId, PageBy pageBy) throws Exception {

		return reservationDAO.findBy(ReservationVO.FINDBYANNOUNCE,ReservationVO.class,announceId,"announceId",pageBy);
	}
}