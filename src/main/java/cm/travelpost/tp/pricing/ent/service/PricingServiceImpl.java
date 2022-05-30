package cm.travelpost.tp.pricing.ent.service;

import cm.travelpost.tp.common.Constants;
import cm.travelpost.tp.common.utils.CodeGenerator;
import cm.travelpost.tp.pricing.ent.dao.PricingDAO;
import cm.travelpost.tp.pricing.ent.vo.PricingSubscriptionVOId;
import cm.travelpost.tp.pricing.ent.vo.PricingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingServiceImpl implements PricingService{
	private static Logger logger = LoggerFactory.getLogger(PricingServiceImpl.class);
	@Autowired
	PricingDAO dao;

	@Override
	public PricingVO create(BigDecimal amount) throws Exception {

		PricingVO pricing = new PricingVO(CodeGenerator.generateCode("P"), Constants.DEFAULT_TOKEN);
		pricing.setPrice(amount);
		PricingSubscriptionVOId id= (PricingSubscriptionVOId) dao.save(pricing);
		return (PricingVO) dao.findById(PricingVO.class, id);
	}

	@Override
	public PricingVO update(String code, String token, BigDecimal amount) throws Exception {

		PricingSubscriptionVOId id = new PricingSubscriptionVOId(code, token);
		PricingVO pricing =(PricingVO) dao.findById(PricingVO.class, id);

		if(pricing!=null){
			pricing.setPrice(amount);
			dao.update(amount);
			return (PricingVO) dao.findById(PricingVO.class, id);
		}
		return null;
	}

	@Override
	public boolean delete(String code, String token) throws Exception {

		PricingSubscriptionVOId id = new PricingSubscriptionVOId(code, token);
		return dao.delete(id);
	}
}
