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
import cm.packagemanager.pmanager.common.utils.QueryUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
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

	@Autowired
	Constants constants;

	@Override
	public int count(int page, int size) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
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
		Query query = session.createQuery("from AnnounceVO order by startDate desc");
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

				Long id= QueryUtils.calcolateId(sessionFactory,MessageVO.GET_ID_SQL);

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
	public List<AnnounceVO> findByUser(Long userId, int page, int size) throws BusinessResourceException, UserException {

		UserVO user = userDAO.findById(userId);
		if(user==null){
			throw new UserException("Aucun utilisateur trouvé avec cet id " +userId);
		}

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createQuery(AnnounceVO.SQL_FIND_BY_USER,AnnounceVO.class);
		query.setParameter("userId", userId);
		query.setFirstResult(page);
		query.setMaxResults(size);

		List announces=query.getResultList();
		return announces;
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
				throw new Exception("Aucun utilisateur trouvé avec cet id " +adto.getUserId());
			}

			AnnounceVO announce= new AnnounceVO();
			setAnnounce(announce,user,adto);

			announce.setStatus(StatusEnum.VALID);
			announce.setAnnounceId(new AnnounceIdVO(constants.DEFAULT_TOKEN));
			announce.setMessages(null);
			announce.setCancelled(false);

			Session session = this.sessionFactory.getCurrentSession();
			session.save(announce);
			session.flush();
			return session.get(AnnounceVO.class,announce.getId());
		}
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
	public List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, int page, int size) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);

		String where=composeQuery(announceSearchDTO, "a");
		Query query=session.createQuery("from AnnounceVO  as a "+ where);
		composeQueryParameters(announceSearchDTO,query);
		query.setFirstResult(page);
		query.setMaxResults(size);
		List announces = query.list();
		return announces;
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
	    fillProductCategory(adto);
	    setAnnounceType(adto.getAnnounceType(),announce);
	    setProductCategory(announce,adto.getCategory());
	    setTransport(adto.getTransport(), announce);
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

	private TransportEnum getTransport(String transport){
		switch (transport){
			case AP:
				return TransportEnum.PLANE;
			case Constants.AUT:
				return TransportEnum.AUTO;
			case Constants.NV:
				return TransportEnum.NAVE;
		}
		return null;
	}

	private void setAnnounceType(String announceType, AnnounceVO announce){
		switch (announceType){
			case Constants.BUYER:
				announce.setAnnounceType(AnnounceType.BUYER);
				break;
			case Constants.SELLER:
				announce.setAnnounceType(AnnounceType.SELLER);
				break;
		}
	}

	private AnnounceType getAnnounceType(String announceType){
		switch (announceType){
			case Constants.BUYER:
				 return AnnounceType.BUYER;
			case Constants.SELLER:
				 return AnnounceType.SELLER;
		}
		return null;
	}

	private void fillProductCategory(AnnounceDTO adto){
		switch (adto.getAnnounceType()){
			case Constants.BUYER:
				break;
			case Constants.SELLER:
				if (StringUtils.isEmpty(adto.getCategory())){
					adto.setCategory(constants.DEFAULT_PROD_CAT_CODE);
				}
				break;
		}
	}

	private void setProductCategory(AnnounceVO announce,String category){

		ProductCategoryVO productCategory= productCategoryService.findByCode(category);
		announce.setCategory(productCategory);
	}

	@Override
	public <AnnounceSearchDTO> String composeQuery(AnnounceSearchDTO   announceSearchDTO, String alias) {
		cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO
				announceSearch=(cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO)announceSearchDTO;

		StringBuilder hql = new StringBuilder(" where ");
		try {
			boolean and = false;

			if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
				hql.append(alias+".transport=:transport ");
				and = true;
			}

			if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
				if (and) {
					hql.append(" and ");
				}
				hql.append(alias+".announceType=:announceType ");
			}

			and = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getPrice())) {
				BigDecimal price = BigDecimalUtils.convertStringToBigDecimal(announceSearch.getPrice());
				AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
				if (price.compareTo(BigDecimal.ZERO)>0 && announceType == AnnounceType.SELLER){
					if (and) {
						hql.append(" and ");
					}
					hql.append(" ( "+alias+".goldPrice=:goldPrice or "+alias+".price=:price or " +alias+".preniumPrice=:preniumPrice) ");
					and = StringUtils.isNotEmpty(hql.toString()) && StringUtils.equals(hql.toString(), " where ");
				}

			}
			if (StringUtils.isNotEmpty(announceSearch.getStartDate())) {
				if (and) {
					hql.append(" and ");
				}
				hql.append(alias+".startDate=:startDate ");

			}

			and = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getEndDate())) {
				if (and) {
					hql.append(" and ");
				}
				hql.append(alias+".endDate=:endDate ");
			}

			and = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getDeparture())) {
				if (and) {
					hql.append(" and ");
				}
				hql.append(alias+".departure like:departure ");
			}
			and = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getArrival())) {
				if (and) {
					hql.append(" and ");
				}
				hql.append(alias+".arrival like:arrival ");

			}
			and = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getCategory())) {
				if (and) {
					hql.append(" and ");
				}
				hql.append(alias+".category.code=:category ");
			}
			and = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (announceSearch.getUserId()!=null) {
				if (and) {
					hql.append(" and ");
				}
				hql.append(alias+".user.id=:user ");
			}
			hql.append(" order by " +alias+".startDate desc");
		}catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return hql.toString();
	}

	@Override
	public <AnnounceSearchDTO> void composeQueryParameters(AnnounceSearchDTO announceSearchDTO, Query query) {

		cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO
				announceSearch=(cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO)announceSearchDTO;

		try {

			if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
				TransportEnum transport = getTransport(announceSearch.getTransport());
				query.setParameter("transport", transport);
			}

			if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
				AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
				query.setParameter("announceType", announceType);
			}

			if (StringUtils.isNotEmpty(announceSearch.getPrice())) {
				BigDecimal price = BigDecimalUtils.convertStringToBigDecimal(announceSearch.getPrice());
				AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
				if (price.compareTo(BigDecimal.ZERO)>0 && announceType == AnnounceType.SELLER){
					query.setParameter("goldPrice", price);
					query.setParameter("price", price);
					query.setParameter("preniumPrice", price);
				}
			}
			if (StringUtils.isNotEmpty(announceSearch.getStartDate())) {
				Date startDate = DateUtils.StringToDate(announceSearch.getStartDate());
				query.setParameter("startDate", startDate);

			}
			if (StringUtils.isNotEmpty(announceSearch.getEndDate())) {
				Date endDate = DateUtils.StringToDate(announceSearch.getEndDate());
				query.setParameter("endDate", endDate);
			}

			if (StringUtils.isNotEmpty(announceSearch.getDeparture())) {
				query.setParameter("departure", "%"+announceSearch.getDeparture().trim().toUpperCase()+"%");
			}

			if (StringUtils.isNotEmpty(announceSearch.getArrival())) {
				query.setParameter("arrival", "%"+announceSearch.getArrival().trim().toUpperCase()+"%");
			}

			if (StringUtils.isNotEmpty(announceSearch.getCategory())) {
				query.setParameter("category", announceSearch.getCategory());
			}
			if (announceSearch.getUserId()!=null) {
				query.setParameter("user", announceSearch.getUserId());
			}

		}catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}
}
