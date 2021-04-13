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
import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
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
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public class AnnounceDAOImpl extends CommonFilter  implements AnnounceDAO {

	private static Logger logger = LoggerFactory.getLogger(AnnounceDAOImpl.class);

	@Autowired
	protected AirlineDAO airlineDAO;

	@Autowired
	protected UserDAO userDAO;

	@Autowired
	protected CategoryDAO categoryDAO;

	@Autowired
	protected Constants constants;

	@Autowired
	protected QueryUtils queryUtils;



	@Override
	@Transactional(readOnly = true)
	public int count(AnnounceSearchDTO announceSearch, PageBy pageBy) throws Exception {

		logger.info(" Announce - count");
		if (announceSearch != null) {
			Session session = this.sessionFactory.getCurrentSession();
			session.enableFilter(FilterConstants.CANCELLED);
			String where = composeQuery(announceSearch, "a");
			Query query = session.createQuery("from AnnounceVO  as a " + where);
			composeQueryParameters(announceSearch, query);
			return CollectionsUtils.isNotEmpty(query.list()) ? query.list().size() : 0;
		} else {
			return count(AnnounceVO.class, pageBy);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<AnnounceVO> announces(PageBy pageBy) throws Exception {

		return allAndOrderBy(AnnounceVO.class, "startDate", true, pageBy);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnnounceVO> announces(int page, int size) throws Exception {

		PageBy pageBy = new PageBy(page, size);
		return allAndOrderBy(AnnounceVO.class, "startDate", true, pageBy);
	}


	@Override
	@Transactional(readOnly = true)
	public List<AnnounceVO> announcesByUser(Long userId, PageBy pageBy) throws Exception {

		UserVO user = userDAO.findById(userId);
		if (user == null) {
			throw new UserException("Aucun utilisateur trouvé avec cet id " + userId);
		}
		return findByUserNameQuery(AnnounceVO.SQL_FIND_BY_USER, AnnounceVO.class, userId, pageBy);

	}

	@Override
	@Transactional(readOnly = true)
	public List<AnnounceVO> announcesByType(AnnounceType type, PageBy pageBy) throws Exception {

		return findBy(AnnounceVO.FINDBYTYPE, AnnounceVO.class, type, "type", pageBy);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
	public AnnounceVO announce(Long id) throws Exception {

		AnnounceVO announce = (AnnounceVO) findById(AnnounceVO.class, id);
		if (announce == null) return null;
		double rating = calcolateAverage(announce.getUser());
		announce.getUserInfo().setRating(rating);
		countReservation(announce);
		return announce;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnnounceVO create(AnnounceDTO adto) throws Exception {

		if (adto != null) {

			UserVO user = userDAO.findById(adto.getUserId());

			if (user == null) {
				throw new Exception("Aucun utilisateur trouvé avec cet id " + adto.getUserId());
			}

			AnnounceVO announce = new AnnounceVO();
			setAnnounce(announce, user, adto);

			announce.setStatus(StatusEnum.VALID);
			announce.setAnnounceId(new AnnounceIdVO(constants.DEFAULT_TOKEN));
			announce.setMessages(null);
			announce.setCancelled(false);
			save(announce);
			addAnnounceToUser(announce);
			return announce;
		}
		return null;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnnounceVO update(UpdateAnnounceDTO adto) throws Exception {

		UserVO user = userDAO.findById(adto.getUserId());

		if (user == null) {
			throw new RecordNotFoundException("Aucun utilisateur trouvé");
		}

		AnnounceVO announce = announce(adto.getId());
		if (announce == null) {
			throw new RecordNotFoundException("Aucune annonce  trouvée");
		}
		if (CollectionsUtils.isEmpty(adto.getCategories())) {
			adto.getCategories().add(constants.DEFAULT_CATEGORIE);
		}
		double rating = calcolateAverage(announce.getUser());
		announce.getUserInfo().setRating(rating);
		setAnnounce(announce, user, adto);
		update(announce);
		countReservation(announce);
		return announce;

	}


	@Override
	@Transactional(readOnly = true)
	public List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);

		String where = composeQuery(announceSearchDTO, "a");
		Query query = session.createQuery("from AnnounceVO  as a " + where);
		composeQueryParameters(announceSearchDTO, query);
		pageBy(query, pageBy);
		return query.list();
	}

	@Override
	@Transactional(propagation = Propagation. REQUIRED)
	public void announcesStatus() throws Exception {
		List<AnnounceVO> announces = Optional.ofNullable(allAndOrderBy(AnnounceVO.class, "startDate", true, null)).orElseGet(Collections::emptyList);
		if (CollectionsUtils.isNotEmpty(announces)) {
			announces.stream().filter(ann->!ann.isCancelled() && DateUtils.isBefore(ann.getEndDate(),new Date()))
				.forEach(a->{
					a.setStatus(StatusEnum.COMPLETED);
					update(a);
				});
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delete(Long id) throws BusinessResourceException {
		logger.info("Announce: delete");

		//delete(AnnounceVO.class,id,true);
		return updateDelete(id);
	}


	public UserVO addAnnounceToUser(AnnounceVO announce) throws BusinessResourceException {

		UserVO user = announce.getUser();

		if (user != null) {
			user.addAnnounce(announce);
			return userDAO.save(user);
		} else {
			return null;
		}
	}


	private void setAnnounce(AnnounceVO announce, UserVO user, AnnounceDTO adto) throws Exception {

		BigDecimal price = adto.getPrice();
		BigDecimal preniumPrice = adto.getPreniumPrice();
		BigDecimal goldenPrice = adto.getGoldPrice();
		BigDecimal weigth = adto.getWeight();
		Date startDate = DateUtils.milliSecondToDate(adto.getStartDate());
		Date endDate = DateUtils.milliSecondToDate(adto.getEndDate());

		if (startDate == null || endDate == null) {
			throw new Exception("Une des dates est invalide");
		}

		if (DateUtils.isAfter(startDate, endDate)) {
			throw new Exception("La date de depart ne peut pas etre superierure à celle de retour");
		}

		announce.setPrice(price);
		announce.setPreniumPrice(preniumPrice);
		announce.setGoldPrice(goldenPrice);
		announce.setWeight(weigth);
		announce.setRemainWeight(weigth);
		announce.setUser(user);
		announce.setStartDate(startDate);
		announce.setEndDate(endDate);
		announce.setArrival(adto.getArrival());
		announce.setDeparture(adto.getDeparture());
		announce.setDescription(adto.getDescription());
		announce.setAnnounceType(adto.getAnnounceType());
		handleCategories(announce,adto.getCategories());
		announce.setTransport(adto.getTransport());
	}

	public List<ReservationVO> findReservations(Long id) throws Exception {

		try {
			return findBy(ReservationVO.FINDBYANNOUNCE, ReservationVO.class, id, "announceId", null);
		} catch (Exception e) {
			logger.error("Erreur durant la recuperation des reservations liées à l'annonce id={}", id);
			throw e;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean deleteReservations(Long id) throws Exception {

		try {

			List<ReservationVO> reservations = findReservations(id);
			for (ReservationVO reservation : reservations) {
				reservation.setCancelled(true);
				update(reservation);
			}
		} catch (Exception e) {
			logger.error("Erreur durant la recuperation des reservations liées à l'annonce id={}", id);
			throw e;
		}
		return true;
	}

	@Override
	public AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException {
		return null;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<AnnounceVO> announcesByUser(UserVO user) throws Exception {
		return findByUserNameQuery(AnnounceVO.SQL_FIND_BY_USER, AnnounceVO.class, user.getId(), null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnnounceVO update(Integer id) throws BusinessResourceException {
		return null;
	}

	@Override
	public boolean updateDelete(Long id) throws BusinessResourceException {
		boolean result = false;

		try {

			Session session = sessionFactory.getCurrentSession();
			AnnounceVO announce = announce(id);
			if (announce != null) {
				announce.updateDeleteChildrens();
				announce.setCancelled(true);
				merge(announce);
				announce = session.get(AnnounceVO.class, id);
				result = (announce != null) && (announce.isCancelled()) && deleteReservations(id);

			}
		} catch (Exception e) {
			throw new BusinessResourceException(e.getMessage());
		}
		return result;
	}

	@Transactional
	void countReservation(AnnounceVO announce) throws Exception {
		int count=countByNameQuery(ReservationVO.FINDBYANNOUNCE,ReservationVO.class,announce.getId(),"announceId", null);
		announce.setCountReservation(count);
	}

	private TransportEnum getTransport(String transport) {
		for (TransportEnum t : TransportEnum.values()) {
			if (StringUtils.equals(transport, t.toString())) {
				return t;
			}
		}
		return null;
	}


	private AnnounceType getAnnounceType(String announceType) {
		for (AnnounceType a : AnnounceType.values()) {
			if (StringUtils.equals(announceType, a.toString())) {
				return a;
			}
		}
		return null;
	}


	@Override
	public String composeQuery(Object obj, String alias) throws Exception {

		AnnounceSearchDTO announceSearch = (AnnounceSearchDTO) obj;

		StringBuilder hql = new StringBuilder(" where ");
		try {
			boolean andOrOr = announceSearch.isAnd();
			boolean addCondition = false;

			if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
				hql.append(alias + ".transport=:transport ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias + ".announceType=:announceType ");
			}

			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (ObjectUtils.isCallable(announceSearch, "price")) {
				BigDecimal price = announceSearch.getPrice();
				//AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
				if (price.compareTo(BigDecimal.ZERO) >= 0) {
					buildAndOr(hql, addCondition, andOrOr);
					hql.append(" ( " + alias + ".goldPrice<=:goldPrice or " + alias + ".price<=:price or " + alias + ".preniumPrice<=:preniumPrice) ");
				}
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (ObjectUtils.isCallable(announceSearch, "startDate") && announceSearch.getStartDate() > 0) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias + ".startDate=:startDate ");
			}

			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (ObjectUtils.isCallable(announceSearch, "endDate") && announceSearch.getEndDate() > 0) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias + ".endDate=:endDate ");
			}

			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getDeparture())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias + ".departure like:departure ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getArrival())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias + ".arrival like:arrival ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getCategory())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias + ".category.code=:category ");
			}
			/*addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (ObjectUtils.isCallable(announceSearch,"userId")){
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias+".user.id=:userId ");
			}
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (StringUtils.isNotEmpty(announceSearch.getUser())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(" ( "+alias+".user.username like:user or "+alias+".user.email like:email) ");
			}*/
			addCondition = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(), " where ");
			if (!addCondition) {
				hql = new StringBuilder();
			}
			hql.append(" order by " + alias + ".startDate desc");
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new Exception("Erreur dans la fonction " + AnnounceDAO.class.getName() + " composeQuery");

		}
		return hql.toString();
	}

	@Override
	public void composeQueryParameters(Object obj, Query query) throws Exception {

		AnnounceSearchDTO announceSearch = (AnnounceSearchDTO) obj;

		try {

			if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
				TransportEnum transport = getTransport(announceSearch.getTransport());
				query.setParameter("transport", transport);
			}

			if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
				AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
				query.setParameter("announceType", announceType);
			}

			if (ObjectUtils.isCallable(announceSearch, "price")) {

				BigDecimal price = announceSearch.getPrice();
				//AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
				if (price.compareTo(BigDecimal.ZERO) >= 0) {
					query.setParameter("goldPrice", price);
					query.setParameter("price", price);
					query.setParameter("preniumPrice", price);
				}
			}
			if (ObjectUtils.isCallable(announceSearch, "startDate") && announceSearch.getStartDate() > 0) {
				Date startDate = DateUtils.milliSecondToDate(announceSearch.getStartDate());
				query.setParameter("startDate", startDate);

			}
			if (ObjectUtils.isCallable(announceSearch, "endDate") && announceSearch.getEndDate() > 0) {
				Date endDate = DateUtils.milliSecondToDate(announceSearch.getEndDate());
				query.setParameter("endDate", endDate);
			}

			if (StringUtils.isNotEmpty(announceSearch.getDeparture())) {
				query.setParameter("departure", "%" + announceSearch.getDeparture().trim().toUpperCase() + "%");
			}

			if (StringUtils.isNotEmpty(announceSearch.getArrival())) {
				query.setParameter("arrival", "%" + announceSearch.getArrival().trim().toUpperCase() + "%");
			}

			if (StringUtils.isNotEmpty(announceSearch.getCategory())) {
				query.setParameter("category", announceSearch.getCategory());
			}
			/*if (ObjectUtils.isCallable(announceSearch,"userId")){
				query.setParameter("userId", announceSearch.getUserId());
			}
			if (StringUtils.isNotEmpty(announceSearch.getUser())) {
				query.setParameter("user", "%"+announceSearch.getUser()+"%");
				query.setParameter("email", "%"+announceSearch.getUser()+"%");
			}*/

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new Exception("Erreur dans la fonction " + AnnounceDAO.class.getName() + " composeQueryParameters");
		}
	}


	private void handleCategories(AnnounceVO announce, List<String> rcategories) throws Exception {

		if (CollectionsUtils.isNotEmpty(rcategories)) {
			rcategories.stream().filter(cat->StringUtils.isNotEmpty(cat)).forEach(x -> {
				CategoryVO category = null;
				try {
					category = categoryDAO.findByCode(x);
					if (category == null) throw new Exception("Valoriser la categorie");
					announce.addCategory(category);
				} catch (Exception e) {
					logger.error("Erreur durant la recuperation des categories",e);
				}
			});
		}
	}
}