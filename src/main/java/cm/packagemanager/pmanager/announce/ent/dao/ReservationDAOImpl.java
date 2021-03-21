package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.BigDecimalUtils;
import cm.packagemanager.pmanager.constant.FieldConstants;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.ValidateReservationDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Repository("reservationDAO")
public class ReservationDAOImpl extends CommonFilter implements ReservationDAO{

	private static Logger logger = LoggerFactory.getLogger(ReservationDAOImpl.class);

	@Autowired
	private AnnounceDAO announceDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	CategoryDAO categoryDAO;


	@Override
	@Transactional
	public int count(String queryname,Long id,String paramName,PageBy pageBy) throws Exception {

		return countByNameQuery(queryname,ReservationVO.class,id,paramName,pageBy);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public ReservationVO addReservation(ReservationDTO reservationDTO) throws BusinessResourceException {
		logger.info("add reservation ");
		Session session = sessionFactory.getCurrentSession();

		UserVO user = userDAO.findById(reservationDTO.getUserId());
		AnnounceVO announce=announceDAO.findById(reservationDTO.getAnnounceId());
		CategoryVO category=categoryDAO.findByCode(reservationDTO.getCategory());

		if(user!=null && announce!=null){
			checkRemainWeight(announce,reservationDTO);
			announce.setRemainWeight(announce.getRemainWeight().subtract(reservationDTO.getWeight()));

			ReservationVO reservation=new ReservationVO();
			reservation.setUser(user);
			reservation.setAnnounce(announce);
			if (category==null){
				category=new CategoryVO();
				category.setCode(FieldConstants.DEFAULT_CATEGORY);
				category.setDescription(FieldConstants.DEFAULT_CATEGORY);
			}
			reservation.setCategory(category);
			reservation.setWeight(reservationDTO.getWeight());
			reservation.setDescription(reservationDTO.getDescription());
			session.save(reservation);
			session.flush();
			return session.get(ReservationVO.class,reservation.getId());
		}
		logger.error("add reservation {},{} non trouvé ou {} non trouvée","La reservation n'a pas été ajoutée","Utilisateur avec id="+reservationDTO.getUserId()," Annonce avec id="+reservationDTO.getAnnounceId());
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public ReservationVO updateReservation(UpdateReservationDTO reservationDTO) throws BusinessResourceException {
		UserVO user = userDAO.findById(reservationDTO.getUserId());
		if(user==null){
			throw new RecordNotFoundException("Aucun utilisateur trouvé");
		}

		AnnounceVO announce=announceDAO.findById(reservationDTO.getAnnounceId());
		CategoryVO category=categoryDAO.findByCode(reservationDTO.getCategory());
		ReservationVO reservation=(ReservationVO) findById(ReservationVO.class,reservationDTO.getId());

		if(announce!=null && user!=null && category!=null && reservation!=null){

			checkRemainWeight(announce, reservationDTO);

			if(BigDecimalUtils.lessThan(reservation.getWeight(), reservationDTO.getWeight())) {
				announce.setRemainWeight(announce.getRemainWeight().subtract(reservationDTO.getWeight().subtract(reservation.getWeight())));
			}else {
				announce.setRemainWeight(announce.getRemainWeight().add(reservation.getWeight().subtract(reservationDTO.getWeight())));
			}

			reservation.setWeight(reservationDTO.getWeight());
			reservation.setDescription(reservationDTO.getDescription());
			Session session = sessionFactory.getCurrentSession();
			session.merge(reservation);
			session.flush();
			return  session.get(ReservationVO.class, reservation.getId());
		}

		return null;
	}

	@Override
	@Transactional
	public boolean deleteReservation(Long id) throws BusinessResourceException{

		ReservationVO reservation= (ReservationVO)findById(ReservationVO.class,id);
		if(reservation!=null){
			AnnounceVO announce=reservation.getAnnounce();
			announce.setRemainWeight(announce.getRemainWeight().add(reservation.getWeight()));
			Session session=sessionFactory.getCurrentSession();
			session.update(announce);
			session.flush();
			//delete(ReservationVO.class,id,true);
			return updateDelete(id);
		}
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public boolean validate(ValidateReservationDTO reservationDTO) throws BusinessResourceException {
		ReservationVO reservation=(ReservationVO) findById(ReservationVO.class,reservationDTO.getId());

		if(reservation!=null){
			if(!reservationDTO.isValidate()){
				reservation.setWeight(BigDecimal.ZERO);
				reservation.setCancelled(true);
				reservation.getAnnounce().setRemainWeight(reservation.getAnnounce().getRemainWeight().add(reservation.getWeight()));
			}
			reservation.setValidate(reservationDTO.isValidate());
			Session session = sessionFactory.getCurrentSession();
			session.merge(reservation);
			session.flush();
			return  session.get(ReservationVO.class, reservation.getId())!=null;
		}
		return false;
	}

	public boolean updateDelete(ReservationVO reservation) throws BusinessResourceException, UserException {
		boolean result=false;
		try{

			Session session=sessionFactory.getCurrentSession();
			if(reservation!=null) {
				reservation.setCancelled(true);
				session.merge(reservation);
				reservation =session.get(ReservationVO.class, reservation.getId());

				result= (reservation!=null) && (reservation.isCancelled());

			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}
		return result;
	}


	@Override
	public boolean updateDelete(Long id) throws BusinessResourceException, UserException {
		return false;
	}
	@Override
	public String composeQuery(Object o, String alias) throws Exception {
		return null;
	}

	@Override
	public void composeQueryParameters(Object o, Query query) throws Exception {

	}


	void checkRemainWeight(AnnounceVO announce,ReservationDTO reservationDTO){
		if(BigDecimalUtils.lessThan(announce.getRemainWeight(),reservationDTO.getWeight()) || BigDecimalUtils.lessThan(announce.getWeight(), reservationDTO.getWeight())) {
			throw new BusinessResourceException("Impossible de reserver une quantité superieure à la quantité disponible");
		}
	}
}
