package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
import cm.packagemanager.pmanager.common.utils.BigDecimalUtils;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={Exception.class,UserNotFoundException.class})
	public ReservationVO addReservation(ReservationDTO reservationDTO) throws Exception {
		logger.info("add reservation ");

		UserVO user = userDAO.findById(reservationDTO.getUserId());
		if (user==null)
			throw  new UserNotFoundException("Utilisateur non trouve");

		AnnounceVO announce=announceDAO.findById(reservationDTO.getAnnounceId());
		if (announce==null){
			logger.error("add reservation {},{} non trouvé ou {} non trouvée","La reservation n'a pas été ajoutée","Utilisateur avec id="+reservationDTO.getUserId()," Annonce avec id="+reservationDTO.getAnnounceId());
			throw  new Exception("Announce non trouve");
		}

		//List<CategoryVO> allCategories=categoryDAO.all(CategoryVO.class);

			checkRemainWeight(announce,reservationDTO.getWeight());
			announce.setRemainWeight(announce.getRemainWeight().subtract(reservationDTO.getWeight()));

			ReservationVO reservation=new ReservationVO();
			reservation.setUser(user);
			reservation.setAnnounce(announce);
			handleCategories(reservation,reservationDTO.getCategories());
			reservation.setWeight(reservationDTO.getWeight());
			reservation.setDescription(reservationDTO.getDescription());
			save(reservation);
			return reservation;
	}



	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
	public ReservationVO updateReservation(UpdateReservationDTO reservationDTO) throws Exception {

			UserVO user = userDAO.findById(reservationDTO.getUserId());
			if(user==null){
				throw new RecordNotFoundException("Aucun utilisateur trouvé");
			}

			ReservationVO reservation=(ReservationVO) findById(ReservationVO.class,reservationDTO.getId());
			if (reservation==null){
				throw  new Exception("Reservation non trouve");
			}
			AnnounceVO announce=reservation.getAnnounce();
			//checkRemainWeight(announce, reservationDTO.getWeight());

			if(BigDecimalUtils.lessThan(reservation.getWeight(), reservationDTO.getWeight())) {
				announce.setRemainWeight(announce.getRemainWeight().subtract(reservationDTO.getWeight().subtract(reservation.getWeight())));
			}else {
				announce.setRemainWeight(announce.getRemainWeight().add(reservation.getWeight().subtract(reservationDTO.getWeight())));
			}
			handleCategories(reservation,reservationDTO.getCategories());
			reservation.setWeight(reservationDTO.getWeight());
			reservation.setDescription(reservationDTO.getDescription());
			update(reservation);
			return reservation;
	}

	@Override
	@Transactional
	public boolean deleteReservation(Long id) throws Exception {

		ReservationVO reservation= (ReservationVO)findById(ReservationVO.class,id);
		if (reservation==null){
			throw  new Exception("Reservation non trouve");
		}

			AnnounceVO announce=reservation.getAnnounce();
			announce.setRemainWeight(announce.getRemainWeight().add(reservation.getWeight()));
			update(announce);
			//delete(ReservationVO.class,id,true);
			return updateDelete(reservation);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public boolean validate(ValidateReservationDTO reservationDTO) throws Exception {
		ReservationVO reservation=(ReservationVO) findById(ReservationVO.class,reservationDTO.getId());
		if (reservation==null){
			throw  new Exception("Reservation non trouve");
		}
		if(!reservationDTO.isValidate()){
			reservation.setWeight(BigDecimal.ZERO);
			reservation.setCancelled(true);
			reservation.getAnnounce().setRemainWeight(reservation.getAnnounce().getRemainWeight().add(reservation.getWeight()));
		}
		reservation.setValidate(reservationDTO.isValidate());
		update(reservation);

		return  reservation.getId()!=null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public ReservationVO getReservation(long id) throws Exception {
		return (ReservationVO) findById(ReservationVO.class,id);
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


	void checkRemainWeight(AnnounceVO announce, BigDecimal weight){

		if(BigDecimalUtils.lessThan(announce.getRemainWeight(),weight) || BigDecimalUtils.lessThan(announce.getWeight(), weight)) {
			throw new BusinessResourceException("Impossible de reserver une quantité superieure ["+weight+" kg] à la quantité disponible ["+announce.getRemainWeight()+" kg]");
		}
	}

	private void handleCategories(ReservationVO reservation, List<String> rcategories) {
		Set<CategoryVO> categories= new HashSet<>();

		if(CollectionsUtils.isNotEmpty(rcategories)){
			rcategories.forEach(x->{
				CategoryVO category=categoryDAO.findByCode(x);
				if(category!=null){
					categories.add(category);
				}
			});
		}
		reservation.setCategories(categories);
	}
}
