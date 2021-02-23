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
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.enums.TransportEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.*;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
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

	@Autowired
	QueryUtils queryUtils;

	@Override
	public int count(AnnounceSearchDTO announceSearch, PageBy pageBy) throws Exception {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query =null;
		if(announceSearch!=null){
			String where=composeQuery(announceSearch, "a");
			 query=session.createQuery("from AnnounceVO  as a "+ where);
			composeQueryParameters(announceSearch,query);
		}else{
			query = session.createQuery("from AnnounceVO");
		}

		int count = CollectionsUtils.isNotEmpty(query.list())?query.list().size():0;
		return count;
	}

	@Override
	public List<AnnounceVO> announces(PageBy pageBy) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query = session.createQuery("from AnnounceVO order by startDate desc");
		query.setFirstResult(pageBy.getPage());
		query.setMaxResults(pageBy.getSize());

		List<AnnounceVO> announces =query.list();

		return announces;
	}

	@Override
	public List<AnnounceVO> announces(int page, int size) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query = session.createQuery("from AnnounceVO order by startDate desc");
		query.setFirstResult(page);
		query.setMaxResults(size);

		List<AnnounceVO> announces = query.list();

		return announces;
	}




	@Override
	public List<AnnounceVO> findByUser(Long userId, PageBy pageBy) throws BusinessResourceException, UserException {

		UserVO user = userDAO.findById(userId);
		if(user==null){
			throw new UserException("Aucun utilisateur trouvé avec cet id " +userId);
		}

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createQuery(AnnounceVO.SQL_FIND_BY_USER,AnnounceVO.class);
		query.setParameter("userId", userId);
		query.setFirstResult(pageBy.getPage());
		query.setMaxResults(pageBy.getSize());

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
			announce=session.get(AnnounceVO.class,announce.getId());
			addAnnounceToUser(announce);
			return announce;
		}
		return null;
	}



	@Override
	public AnnounceVO update(UpdateAnnounceDTO adto) throws Exception {

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
		session.update(announce);
		return announce;

	}


	@Override
	public List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);

		String where=composeQuery(announceSearchDTO, "a");
		Query query=session.createQuery("from AnnounceVO  as a "+ where);
		composeQueryParameters(announceSearchDTO,query);
		query.setFirstResult(pageBy.getPage());
		query.setMaxResults(pageBy.getSize());
		List announces = query.list();
		return announces;
	}



	@Override
	public boolean delete(Long id) throws BusinessResourceException {
		logger.info("Announce: delete");
		//deleteObject(id);
		return updateDelete(id);
	}

	@Override
	public void deleteObject(Object object) throws RecordNotFoundException {
		Long id = (Long)object;
		try{
			Session session=sessionFactory.getCurrentSession();

			AnnounceVO announce=findById(id);
			if(announce!=null){
				session.remove(announce);
				session.flush();
			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}
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


	private void setAnnounce(AnnounceVO announce,UserVO user,AnnounceDTO adto) throws Exception {

		BigDecimal price =adto.getPrice();
		BigDecimal preniumPrice =adto.getPreniumPrice();
		BigDecimal goldenPrice=adto.getGoldPrice();
		BigDecimal weigth =adto.getWeigth();
		Date startDate =adto.getStartDate();
		Date endDate =adto.getEndDate();

		if(startDate== null || endDate == null){
			throw new Exception("Une des dates est invalide");
		}

		if(DateUtils.isAfter(startDate,endDate)){
			throw new Exception("La date de depart ne peut pas etre superierure à celle de retour");
		}

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
		announce.setAnnounceType(adto.getAnnounceType());
		setProductCategory(announce,adto.getCategory());
		announce.setTransport(adto.getTransport());
	}

	@Override
	public AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException {
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
	public AnnounceVO findByUser(UserVO user) throws BusinessResourceException {
		return null;
	}

	@Override
	public AnnounceVO update(Integer id) throws BusinessResourceException {
		return null;
	}

	@Override
	public boolean updateDelete(Long id) throws BusinessResourceException{
		boolean result=false;

		try{

			Session session=sessionFactory.getCurrentSession();
			AnnounceVO announce=findById(id);
			if(announce!=null) {
				announce.setCancelled(true);
				session.merge(announce);
				announce = (AnnounceVO) session.get(AnnounceVO.class, id);
				result= (announce!=null) && (announce.isCancelled());
			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}
		return result;
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
		switch (adto.getAnnounceType().name()){
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
	public  String composeQuery(Object   obj, String alias) throws Exception {

		AnnounceSearchDTO announceSearch=(AnnounceSearchDTO)obj;

		StringBuilder hql = new StringBuilder(" where ");
		try {
			boolean andOrOr = announceSearch.isAnd();
			boolean addCondition = false;

			if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
				hql.append(alias+".transport=:transport ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias+".announceType=:announceType ");
			}

			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
				if (ObjectUtils.isCallable(announceSearch,"price")){
					BigDecimal price = announceSearch.getPrice();
				//AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
				if (price.compareTo(BigDecimal.ZERO)>=0){
					buildAndOr(hql, addCondition, andOrOr);
					hql.append(" ( "+alias+".goldPrice<=:goldPrice or "+alias+".price<=:price or " +alias+".preniumPrice<=:preniumPrice) ");
				}
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && StringUtils.equals(hql.toString(), " where ");
			if (ObjectUtils.isCallable(announceSearch,"startDate")){
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias+".startDate=:startDate ");
			}

			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (ObjectUtils.isCallable(announceSearch,"endDate")){
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias+".endDate=:endDate ");
			}

			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getDeparture())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias+".departure like:departure ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getArrival())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias+".arrival like:arrival ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getCategory())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias+".category.code=:category ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (ObjectUtils.isCallable(announceSearch,"userId")){
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias+".user.id=:userId ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getUser())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(" ( "+alias+".user.username like:user or "+alias+".user.email like:email) ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (!addCondition){
				hql = new StringBuilder();
			}
			hql.append(" order by " +alias+".startDate desc");
		}catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new Exception("Erreur dans la fonction "+AnnounceDAO.class.getName() + " composeQuery");

		}
		return hql.toString();
	}

	@Override
	public  void composeQueryParameters(Object obj, Query query) throws Exception {

		AnnounceSearchDTO announceSearch=(AnnounceSearchDTO)obj;

		try {

			if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
				TransportEnum transport = getTransport(announceSearch.getTransport());
				query.setParameter("transport", transport);
			}

			if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
				AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
				query.setParameter("announceType", announceType);
			}

				if (ObjectUtils.isCallable(announceSearch,"price")){

					BigDecimal price = announceSearch.getPrice();
				//AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
				if (price.compareTo(BigDecimal.ZERO)>=0){
					query.setParameter("goldPrice", price);
					query.setParameter("price", price);
					query.setParameter("preniumPrice", price);
				}
			}
			if (ObjectUtils.isCallable(announceSearch,"startDate")){
				Date startDate =announceSearch.getStartDate();// DateUtils.StringToDate(announceSearch.getStartDate());
				query.setParameter("startDate", startDate);

			}
			if (ObjectUtils.isCallable(announceSearch,"endDate")){
				Date endDate = announceSearch.getStartDate();//DateUtils.StringToDate(announceSearch.getEndDate());
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
			if (ObjectUtils.isCallable(announceSearch,"userId")){
				query.setParameter("userId", announceSearch.getUserId());
			}
			if (StringUtils.isNotEmpty(announceSearch.getUser())) {
				query.setParameter("user", "%"+announceSearch.getUser()+"%");
				query.setParameter("email", "%"+announceSearch.getUser()+"%");
			}

		}catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new Exception("Erreur dans la fonction "+AnnounceDAO.class.getName() + " composeQueryParameters");
		}
	}

}
