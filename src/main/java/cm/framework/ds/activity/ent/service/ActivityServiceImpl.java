package cm.framework.ds.activity.ent.service;

import cm.framework.ds.activity.ent.dao.ActivityDAO;
import cm.framework.ds.activity.ent.vo.ActivityIdVO;
import cm.framework.ds.activity.ent.vo.ActivityVO;
import cm.framework.ds.common.ent.GenericCRUDEService;
import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.common.exception.GenericCRUDEException;
import cm.framework.ds.common.utils.CodeGenerator;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cm.travelpost.tp.common.Constants.DEFAULT_TOKEN;


@Service
@Transactional
public class ActivityServiceImpl  implements ActivityService,GenericCRUDEService<ActivityVO, ActivityIdVO> {
	public static final String ACTIVITY_PREFIX = "AV";
	@Autowired
	ActivityDAO dao;

	@Autowired
	UserDAO userDAO;


	@Override
	public ActivityVO create(ActivityVO activity) throws GenericCRUDEException {

		ActivityIdVO id = new ActivityIdVO(CodeGenerator.generateCode(DEFAULT_TOKEN,ACTIVITY_PREFIX), DEFAULT_TOKEN);
		activity.setId(id);
		dao.save(activity);
		return (ActivityVO) dao.get(ActivityVO.class,id);
	}

	@Override
	public ActivityVO read(ActivityIdVO id) throws GenericCRUDEException {
		  return (ActivityVO) dao.get(ActivityVO.class,id);
	}

	@Override
	public ActivityVO update(ActivityVO activity, ActivityIdVO id) throws GenericCRUDEException {
		return null;
	}

	@Override
	public boolean delete(ActivityIdVO id) throws GenericCRUDEException {

		ActivityVO activity =  (ActivityVO) dao.get(ActivityVO.class,id);

		activity.cancel();
		dao.update(activity);
		activity =(ActivityVO)dao.findById(ActivityVO.class, id);
		return activity.isCancelled();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<ActivityVO> findByUser(Long userId, PageBy pageBy) throws Exception {

		if(userDAO.findById(userId) ==null){
			throw new UserException("user not found with id :" + userId);
		}
		return dao.findBy(ActivityVO.FINDBYUSER,ActivityVO.class,userId, "userId",pageBy);
	}
}
