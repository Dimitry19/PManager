package cm.travelpost.tp.pricing.ent.service;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.common.enums.OperationEnum;
import cm.travelpost.tp.common.exception.SubscriptionException;
import cm.travelpost.tp.common.utils.CodeGenerator;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.pricing.ent.dao.PricingDAO;
import cm.travelpost.tp.pricing.ent.dao.SubscriptionDAO;
import cm.travelpost.tp.pricing.ent.vo.PricingSubscriptionVOId;
import cm.travelpost.tp.pricing.ent.vo.PricingVO;
import cm.travelpost.tp.pricing.ent.vo.SubscriptionVO;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.pricing.CreateSubscriptionDTO;
import cm.travelpost.tp.ws.requests.pricing.ManageSubscriptionUserDTO;
import cm.travelpost.tp.ws.requests.pricing.UpdateSubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cm.travelpost.tp.common.Constants.DEFAULT_TOKEN;
import static cm.travelpost.tp.common.Constants.SUBSCRIPTION_PREFIX;

@Service
@Transactional
public class SubscriptionServiceImpl extends APricingSubscriptionServiceImpl implements SubscriptionService{

	protected final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

	@Autowired
	SubscriptionDAO dao;

	@Autowired
	PricingDAO pricingDAO;

	@Autowired
	UserDAO userDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SubscriptionVO create(CreateSubscriptionDTO dto) throws Exception {

		SubscriptionVO subscription = new SubscriptionVO(CodeGenerator.generateCode(SUBSCRIPTION_PREFIX), DEFAULT_TOKEN);
		checkPricingByType(subscription, dto.getType(), dto.getDescription(), dto.getConvertedStartDate(), dto.getConvertedEndDate());
		PricingSubscriptionVOId id= (PricingSubscriptionVOId) dao.save(subscription);
		return (SubscriptionVO) dao.findById(SubscriptionVO.class, id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SubscriptionVO update(String code, String token, UpdateSubscriptionDTO dto) throws Exception {

		SubscriptionVO subscription= getSubscription(code,token);
		checkPricingByType(subscription, dto.getType(), dto.getDescription(), dto.getConvertedStartDate(), dto.getConvertedEndDate());
		dao.update(subscription);
		PricingSubscriptionVOId id= new PricingSubscriptionVOId(code,token);
		return (SubscriptionVO) dao.findById(SubscriptionVO.class, id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean delete(String code, String token) throws Exception {

		PricingSubscriptionVOId id = new PricingSubscriptionVOId(code, token);
		return dao.delete(id);
	}

	@Override
	public SubscriptionVO object(String code, String token) {
		return (SubscriptionVO) dao.findById(SubscriptionVO.class, new PricingSubscriptionVOId(code, token));
	}

	@Override
	public int count(PageBy pageBy) throws Exception {

		  return dao.count(SubscriptionVO.class, pageBy);
	}

	@Override
	public List all(PageBy pageBy) throws Exception {
		try {
			return dao.all(SubscriptionVO.class,pageBy);
		} catch (Exception e) {
			logger.error("Erreur durant la recuperation de la liste des abonnements",e);
			throw new SubscriptionException(e.getMessage());
		}
	}

	@Override
	public SubscriptionVO byType(SubscriptionPricingType type) throws Exception {
		  return dao.byType(type);
	}

	@Override
	public boolean addOrRemoveToUser(ManageSubscriptionUserDTO dto) throws Exception {

		Set<UserVO> users = new HashSet<>();
		SubscriptionVO subscription= getSubscription(dto.getCode(),dto.getToken());
		if(CollectionsUtils.isNotEmpty(dto.getUsers())){
			users = (Set<UserVO>) dto.getUsers()
					.stream()
					.filter(id->getUser(String.valueOf(id))!=null)
					.map(id->getUser(String.valueOf(id)))
					.collect(Collectors.toSet());
		}
		if (dto.getOperation()== OperationEnum.ADD){
			users.stream().forEach(u-> {u.setSubscription(subscription);userDAO.updateUser(u); });
			subscription.getUsers().addAll(users);
		}else {
			users.stream().forEach(u-> { u.setSubscription(null); userDAO.updateUser(u);});
			subscription.getUsers().removeAll(users);
		}
		return true;
	}

	@Override
	public int countUsers(@NotNull String code, @NotNull String token, PageBy pageBy) throws Exception {
		SubscriptionVO subscription = getSubscription( code, token );
		return CollectionsUtils.size(userDAO.usersBySubscription(subscription.getId().getCode(),subscription.getId().getToken(), pageBy));
	}
	@Override
	public List<UserVO> retrieveUsers(@NotNull String code, @NotNull String token, PageBy pageBy) throws Exception {
		SubscriptionVO subscription = getSubscription( code, token );
		return userDAO.usersBySubscription(subscription.getId().getCode(),subscription.getId().getToken(), pageBy);
	}

	private PricingVO getPricingByType(SubscriptionPricingType type) throws Exception {

		return pricingDAO.byType(type);
	}

	private void checkPricingByType(SubscriptionVO subscription, SubscriptionPricingType type, String description, Date convertedStartDate, Date convertedEndDate) throws Exception {
		PricingVO pricing= getPricingByType(type);
		if(pricing == null){
			logger.error("Aucun pricing de type {} trouvé", type);
			throw new SubscriptionException("Aucun pricing de type ["+ type +"] trouvé");
		}
		subscription.setPricing(pricing);
		subscription.setDescription(description);
		subscription.setStartDate(convertedStartDate);
		subscription.setEndDate(convertedEndDate);
		subscription.setType(type);
		pricing.addSubscription(subscription);
	}

	private SubscriptionVO getSubscription(String code, String token ) throws SubscriptionException {

		SubscriptionVO subscription= (SubscriptionVO) dao.findById(SubscriptionVO.class, new PricingSubscriptionVOId(code, token));
		if(subscription == null){
			logger.error("Aucun abonnement trouvé avec le code {}",code);
			throw new SubscriptionException("Aucun abonnement trouvé avec le code "+code);
		}
		return subscription;

	}
	private UserVO getUser(String username){
		try {
			return userDAO.findByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private SubscriptionPricingType getSubscriptionPricingType(String type) {

		for (SubscriptionPricingType a : SubscriptionPricingType.values()) {
			if (StringUtils.equals(type, a.toString())) {
				return a;
			}
		}
		return null;
	}
}
