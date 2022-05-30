package cm.travelpost.tp.pricing.ent.service;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.common.Constants;
import cm.travelpost.tp.common.exception.PricingException;
import cm.travelpost.tp.common.utils.CodeGenerator;
import cm.travelpost.tp.pricing.ent.dao.PricingDAO;
import cm.travelpost.tp.pricing.ent.vo.PricingSubscriptionVOId;
import cm.travelpost.tp.pricing.ent.vo.PricingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PricingServiceImpl implements PricingService{

	private static Logger logger = LoggerFactory.getLogger(PricingServiceImpl.class);

	@Autowired
	PricingDAO dao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PricingVO create(BigDecimal amount) throws Exception {

		PricingVO pricing = new PricingVO(CodeGenerator.generateCode("P"), Constants.DEFAULT_TOKEN);
		pricing.setPrice(amount);
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
	public PricingVO pricing(@NotNull String code, @NotNull String token) throws Exception {
		return (PricingVO) dao.findById(PricingVO.class, new PricingSubscriptionVOId(code, token));
	}

	@Override
	public int count(PageBy pageBy) throws PricingException,Exception {
		return dao.count(PricingVO.class, pageBy);
	}

	@Override
	public List pricings(PageBy pageBy) throws PricingException {
		try {
			return dao.all(PricingVO.class,pageBy);
		} catch (Exception e) {
			logger.error("Erreur durant la recuperation de la liste des pricings",e);
			throw new PricingException(e.getMessage());
		}
	}

	@Override
	public PricingVO byPrice(@NotNull BigDecimal amount) throws Exception {
		return dao.byPrice(amount);
	}
}
