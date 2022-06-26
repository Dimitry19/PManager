package cm.framework.ds.activity.ent.service;

import cm.framework.ds.activity.ent.dao.ActivityDAO;
import cm.framework.ds.activity.ent.vo.ActivityIdVO;
import cm.framework.ds.activity.ent.vo.ActivityVO;
import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.common.exception.GenericCRUDEException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.user.ent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ActivityServiceImpl  implements ActivityService<ActivityVO, ActivityIdVO> {

	@Autowired
	ActivityDAO dao;

	@Autowired
	UserService userService;


	@Override
	public ActivityVO create(ActivityVO activity) throws GenericCRUDEException {

		dao.save(activity);
		return (ActivityVO) dao.get(ActivityVO.class,activity.getId());
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

		if(userService.findById(userId) ==null){
			throw new UserException("user not found with id :" + userId);
		}
		return dao.findBy(ActivityVO.FINDBYUSER,ActivityVO.class,userId, "userId",pageBy);
	}
}
