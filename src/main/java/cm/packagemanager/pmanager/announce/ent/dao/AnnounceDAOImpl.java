package cm.packagemanager.pmanager.announce.ent.dao;

/**
 * Dans la methode addMessage() La recherche entre user et announce a été faite dans cet ordre pour eviter le
 * fait que l'attrbut utisateur ne soit pas dechiffré dans l'entité announce
 *  et aussi eviter HibernateException: Found two representations of same collection
 *
 * */
import cm.packagemanager.pmanager.airline.ent.dao.AirlineDAO;
import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceIdVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.utils.BigDecimalUtils;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.MessageDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;


import static cm.packagemanager.pmanager.common.Constants.BUYER;
import static cm.packagemanager.pmanager.common.Constants.SELLER;

@Repository
public class AnnounceDAOImpl implements AnnounceDAO {

	private static Logger logger = LoggerFactory.getLogger(AnnounceDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private AirlineDAO airlineDAO;

	@Autowired
	private UserDAO userDAO;

	Transaction tx;

	@Override
	public List<AnnounceVO> announces(int page, int size) throws BusinessResourceException {
		return null;
	}

	@Override
	public MessageVO addMessage(MessageDTO mdto) throws BusinessResourceException {


		try {


			UserVO user = userDAO.findByOnlyUsername(mdto.getUsername());
			AnnounceVO announce=findById(mdto.getAnnounceId());

			if(user!=null && announce!=null){
				MessageVO message =new MessageVO();
				message.setAnnounce(announce);
				message.setContent(mdto.getContent());
				message.setCancelled(false);
				message.setUser(user);

				Long id=calcolateId(MessageVO.GET_ID_SQL);

				MessageIdVO messageId=new MessageIdVO(id,announce.getAnnounceId().getToken());
				message.setId(messageId);
				announce.addMessages(message);
				Session session =sessionFactory.getCurrentSession();
				tx = session.getTransaction();
				session.save(message);
				tx.commit();
				return message;
			}
		} catch (Exception e) {

			if (tx != null) {
				tx.rollback();
			}
			logger.error(e.getMessage());
			e.printStackTrace();
		}



		return null;
	}

	@Override
	public AnnounceVO findByUser(UserVO user) throws BusinessResourceException {
		return null;
	}

	@Override
	public AnnounceVO findById(Long id) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.getNamedQuery(AnnounceVO.FINDBYID);
		query.setParameter("id", id);

		AnnounceVO announce= (AnnounceVO) query.uniqueResult();

		return announce;
	}

	@Override
	public AnnounceVO create(AnnounceDTO adto) throws  BusinessResourceException{


		if(adto!=null){

			BigDecimal price =BigDecimalUtils.convertStringToBigDecimal(adto.getPrice());
			BigDecimal weigth =BigDecimalUtils.convertStringToBigDecimal(adto.getWeigth());
			Date startDate =DateUtils.StringToDate(adto.getStartDate());
			Date endDate =DateUtils.StringToDate(adto.getEndDate());

			AirlineVO airline = airlineDAO.findByCode(adto.getAirline());
			UserVO user = userDAO.findById(adto.getUserId());

			if(airline==null || user==null){
				return null;
			}

			AnnounceVO announce= new AnnounceVO();
			announce.setAirline(airline);
			announce.setPrice(price);
			announce.setWeigth(weigth);
			announce.setUser(user);
			announce.setStartDate(startDate);
			announce.setEndDate(endDate);
			announce.setArrival(adto.getArrival());
			announce.setDeparture(adto.getDeparture());
			announce.setStatus(StatusEnum.VALID);
			announce.setMessages(null);
			announce.setCancelled(false);
			announce.setAnnounceId(new AnnounceIdVO(airline.getId().getToken()));


			switch (adto.getAnnounceType()){
				case BUYER:
					announce.setAnnounceType(AnnounceType.BUYER);
					break;
				case SELLER:
					announce.setAnnounceType(AnnounceType.SELLER);
					break;
			}
			Session session = this.sessionFactory.getCurrentSession();
			session.save(announce);
			return announce;
		}
		return null;
	}

	@Override
	public AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException {
		return null;
	}

	@Override
	public AnnounceVO delete(Integer id) throws BusinessResourceException {
		return null;
	}

	@Override
	public AnnounceVO addComment(MessageVO message) throws BusinessResourceException {
		return null;
	}

	@Override
	public AnnounceVO update(AnnounceVO announce) throws BusinessResourceException {
		return null;
	}

	@Override
	public AnnounceVO update(Integer id) throws BusinessResourceException {
		return null;
	}


	private Long  calcolateId(String sqlnative){
		Session sess = sessionFactory.openSession();
		BigInteger id = (BigInteger)sess.createSQLQuery(sqlnative).getResultList().get(0);
		sess.close();


		return new Long(String.valueOf(id))+ new Long(1);
	}
	public UserVO addAnnounceToUser(AnnounceVO announce) throws BusinessResourceException {

		UserVO user= announce.getUser();

		if (user!=null){
			user.addAnnounces(announce);
			return userDAO.save(user);
		}else{
			return null;
		}
	}
}
