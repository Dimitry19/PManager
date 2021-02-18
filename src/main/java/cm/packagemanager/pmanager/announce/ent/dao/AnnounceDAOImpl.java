package cm.packagemanager.pmanager.announce.ent.dao;

/**
 * Dans la methode addMessage() La recherche entre user et announce a été faite dans cet ordre pour eviter le
 * fait que l'attrbut utisateur ne soit pas dechiffré dans l'entité announce
 *  et aussi eviter HibernateException: Found two representations of same collection
 *
 * */
import cm.packagemanager.pmanager.airline.ent.dao.AirlineDAO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceIdVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryIdVO;
import cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryVO;
import cm.packagemanager.pmanager.announce.service.ProductCategoryService;
import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.enums.TransportEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.BigDecimalUtils;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.common.utils.SQLUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import static cm.packagemanager.pmanager.common.Constants.*;



@Repository
public class AnnounceDAOImpl extends CommonFilter implements AnnounceDAO {

	private static Logger logger = LoggerFactory.getLogger(AnnounceDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private AirlineDAO airlineDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	ProductCategoryService productCategoryService;

	@Override
	public int count(int page, int size) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		//session.enableFilter(FilterConstants.ACTIVE_MBR);
		Query query = session.createQuery("from AnnounceVO");
		query.setFirstResult(page);
		query.setMaxResults(size);

		//query.setMaxResults(size);

		int count = query.list()!=null ? (int)query.list().size():0;


		return count;
	}

	@Override
	public List<AnnounceVO> announces(int page, int size) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query = session.createQuery("from AnnounceVO ");
		query.setFirstResult(page);
		query.setMaxResults(size);

		List<AnnounceVO> announces =(List) query.list();

		return announces;
	}

	@Override
	public MessageVO addMessage(MessageDTO mdto) throws BusinessResourceException {


			Session session=null;
			UserVO user = userDAO.findByOnlyUsername(mdto.getUsername(),false);
			AnnounceVO announce=findById(mdto.getAnnounceId());

			if(user!=null && announce!=null){
				MessageVO message =new MessageVO();
				message.setAnnounce(announce);
				message.setContent(mdto.getContent());
				message.setCancelled(false);
				message.setUser(user);

				Long id=SQLUtils.calcolateId(sessionFactory,MessageVO.GET_ID_SQL);

				MessageIdVO messageId=new MessageIdVO(id,announce.getAnnounceId().getToken());
				message.setId(messageId);
				announce.addMessages(message);

				session=sessionFactory.getCurrentSession();
				session.save(message);
				return message;
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
		AnnounceVO announce=(AnnounceVO) manualFilter(session.find(AnnounceVO.class,id));
		return announce;
	}

	@Override
	public AnnounceVO create(AnnounceDTO adto) throws BusinessResourceException, Exception {

		if(adto!=null){

			UserVO user = userDAO.findById(adto.getUserId());

			if(user==null){
				throw new Exception("Aucun utilisateur trouvÃ© avec cet id " +adto.getUserId());
			}

			AnnounceVO announce= new AnnounceVO();
			setAnnounce(announce,user,adto);

			announce.setStatus(StatusEnum.VALID);
			announce.setAnnounceId(new AnnounceIdVO(DEFAULT_TOKEN));
			announce.setMessages(null);
			announce.setCancelled(false);


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
	public boolean delete(Long id) throws BusinessResourceException {
		AnnounceVO announce=updateDelete(id);

		return  (announce!=null) && announce.isCancelled();
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
	public AnnounceVO update(UpdateAnnounceDTO adto) throws BusinessResourceException, UserException,RecordNotFoundException
	{


		UserVO user = userDAO.findById(adto.getUserId());

		if(user==null){
			 throw new RecordNotFoundException("Aucun utilisateur trouvé");
		}

		AnnounceVO announce = findById(adto.getId());
		if(announce==null){
			throw new RecordNotFoundException("Aucune annonce  trouvée");
		}

		setAnnounce(announce,user,adto);
		Session session = sessionFactory.getCurrentSession();
		session.update(user);
		return announce;

	}

	@Override
	public AnnounceVO update(Integer id) throws BusinessResourceException {
		return null;
	}


	@Override
	public Object manualFilter(Object o) {
		return super.manualFilter(o);
	}

	public UserVO addAnnounceToUser(AnnounceVO announce) throws BusinessResourceException {

		UserVO user= announce.getUser();

		if (user!=null){
			user.addAnnounce(announce);
			return userDAO.save(user);
		}else{
			return null;
		}
	}


    private void setAnnounce(AnnounceVO announce,UserVO user,AnnounceDTO adto){

	    BigDecimal price =BigDecimalUtils.convertStringToBigDecimal(adto.getPrice());
	    BigDecimal preniumPrice =BigDecimalUtils.convertStringToBigDecimal(adto.getPreniumPrice());
	    BigDecimal goldenPrice=BigDecimalUtils.convertStringToBigDecimal(adto.getGoldPrice());
	    BigDecimal weigth =BigDecimalUtils.convertStringToBigDecimal(adto.getWeigth());
	    Date startDate =DateUtils.StringToDate(adto.getStartDate());
	    Date endDate =DateUtils.StringToDate(adto.getEndDate());

	    announce.setPrice(price);
	    announce.setPreniumPrice(preniumPrice);
	    announce.setGoldPrice(goldenPrice);
	    announce.setWeigth(weigth);
	    announce.setUser(user);
	    announce.setStartDate(startDate);
	    announce.setEndDate(endDate);
	    announce.setArrival(adto.getArrival());
	    announce.setDeparture(adto.getDeparture());
		announce.setDescription(adto.getDescription());
	    setAnnounceType(adto.getAnnounceType(),announce);
	    setProductCategory(announce,adto.getCategory());
	    setTransport(adto.getTransport(), announce);
    }


	private AnnounceVO updateDelete(Long id) throws BusinessResourceException{

		try{

			Session session=sessionFactory.getCurrentSession();

			AnnounceVO announce=findById(id);
			if(announce!=null){
				announce.setCancelled(true);
				session.merge(announce);
				return session.get(AnnounceVO.class,id);
			}else{
				return null;
			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}
	}
	private void setTransport(String transport, AnnounceVO announce){
		switch (transport){
			case AP:
				announce.setTransport(TransportEnum.PLANE);
				break;
			case Constants.AUT:
				announce.setTransport(TransportEnum.AUTO);
				break;
			case Constants.NV:
				announce.setTransport(TransportEnum.NAVE);
				break;
		}
	}

	private void setAnnounceType(String announceType, AnnounceVO announce){
		switch (announceType){
			case BUYER:
				announce.setAnnounceType(AnnounceType.BUYER);
				break;
			case SELLER:
				announce.setAnnounceType(AnnounceType.SELLER);
				break;
		}
	}

	private void setProductCategory(AnnounceVO announce,String category){

		ProductCategoryVO productCategory= productCategoryService.findByCode(category);
		announce.setCategory(productCategory);
	}
}
