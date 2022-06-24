package cm.travelpost.tp.pricing.ent.service;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.common.utils.CodeGenerator;
import cm.travelpost.tp.common.exception.PricingException;
import cm.travelpost.tp.pricing.ent.dao.PricingDAO;
import cm.travelpost.tp.pricing.ent.vo.PricingSubscriptionVOId;
import cm.travelpost.tp.pricing.ent.vo.PricingVO;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;
import cm.travelpost.tp.ws.requests.pricing.CreatePricingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static cm.travelpost.tp.common.Constants.DEFAULT_TOKEN;
import static cm.travelpost.tp.common.Constants.PRICING_PREFIX;

@Service
@Transactional
public class PricingServiceImpl extends APricingSubscriptionServiceImpl  implements PricingService{

	private static Logger logger = LoggerFactory.getLogger(PricingServiceImpl.class);

	@Autowired
	PricingDAO dao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PricingVO create(CreatePricingDTO dto) throws Exception {

		PricingVO pricing = new PricingVO(CodeGenerator.generateCode(DEFAULT_TOKEN,PRICING_PREFIX), DEFAULT_TOKEN);
		pricing.setType(dto.getType());
		pricing.setPrice(dto.getAmount());
		PricingSubscriptionVOId id= (PricingSubscriptionVOId) dao.save(pricing);
		return (PricingVO) dao.findById(PricingVO.class, id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public PricingVO update(String code, String token, BigDecimal amount) throws Exception {

		PricingSubscriptionVOId id = new PricingSubscriptionVOId(code, token);
		PricingVO pricing =(PricingVO) dao.findById(PricingVO.class, id);

		if(pricing!=null){
			pricing.setPrice(amount);
			dao.update(pricing);
			return (PricingVO) dao.findById(PricingVO.class, id);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean delete(String code, String token) throws Exception {

		PricingSubscriptionVOId id = new PricingSubscriptionVOId(code, token);
		return dao.delete(id);
	}

	@Override
	public PricingVO object(@NotNull String code, @NotNull String token) throws Exception {
		return (PricingVO) dao.findById(PricingVO.class, new PricingSubscriptionVOId(code, token));
	}

	@Override
	public int count(PageBy pageBy) throws PricingException,Exception {
		return dao.count(PricingVO.class, pageBy);
	}

	@Override
	public List all(PageBy pageBy) throws Exception {
		try {
			return dao.all(PricingVO.class,pageBy);
		} catch (Exception e) {
			logger.error("Erreur durant la recuperation de la liste du pricing",e);
			throw new PricingException(e.getMessage());
		}
	}

	@Override
	public PricingVO byPrice(@NotNull BigDecimal amount) throws Exception {
		return dao.byPrice(amount);
	}

	@Override
	public PricingVO byType(SubscriptionPricingType type) throws Exception {
		return dao.byType(type);
	}
}
