package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryVO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.BigDecimalUtils;
import cm.packagemanager.pmanager.constant.FieldConstants;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
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


@Repository
public class ReservationDAOImpl extends CommonFilter implements ReservationDAO{

	private static Logger logger = LoggerFactory.getLogger(ReservationDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private AnnounceDAO announceDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	ProductCategoryDAO categoryDAO;



	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public ReservationVO addReservation(ReservationDTO reservationDTO) throws BusinessResourceException {
		Session session = sessionFactory.getCurrentSession();

		UserVO user = userDAO.findById(reservationDTO.getUserId());
		AnnounceVO announce=announceDAO.findById(reservationDTO.getAnnounceId());
		ProductCategoryVO category=categoryDAO.findByCode(reservationDTO.getCategory());

		if(user!=null && announce!=null){

			if(BigDecimalUtils.lessThan(announce.getWeigth(), reservationDTO.getWeight()))
			{
				throw new BusinessResourceException("Impossible de reserver une quantité superieure à la quantité disponible");
			}
			announce.setWeigth(announce.getWeigth().subtract(reservationDTO.getWeight()));

			ReservationVO reservation=new ReservationVO();
			reservation.setUser(user);
			reservation.setAnnounce(announce);
			if (category==null){
				category=new ProductCategoryVO();
				category.setCode(FieldConstants.DEFAULT_CATEGORY);
				category.setDescription(FieldConstants.DEFAULT_CATEGORY);
			}
			reservation.setCategory(category);
			reservation.setWeight(reservationDTO.getWeight());
			session.save(reservation);
			session.flush();
			return session.get(ReservationVO.class,reservation.getId());
		}
		return null;
	}

	@Override
	@Transactional
	public boolean deleteReservation(Long id) {

		ReservationVO reservation= (ReservationVO) findViaSession(ReservationVO.class,id).get();
		if(reservation!=null){
			AnnounceVO announce=reservation.getAnnounce();
			announce.setWeigth(announce.getWeigth().add(reservation.getWeight()));
			Session session=sessionFactory.getCurrentSession();
			session.update(announce);
			session.flush();
			delete(ReservationVO.class,id,true);
			return true;
		}
		return false;
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
}
