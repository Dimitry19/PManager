package cm.framework.ds.activity.ent.service;

import cm.framework.ds.activity.ent.vo.ActivityIdVO;
import cm.framework.ds.activity.ent.vo.ActivityVO;
import cm.framework.ds.common.ent.GenericCRUDEService;
import cm.framework.ds.common.ent.vo.CommonIdVO;
import cm.framework.ds.common.ent.vo.CommonVO;
import cm.framework.ds.common.ent.vo.PageBy;

import java.util.List;

public interface ActivityService<A extends CommonVO, AId extends CommonIdVO> extends GenericCRUDEService<ActivityVO, ActivityIdVO> {

	List<ActivityVO> findByUser(Long userId, PageBy pageBy) throws Exception;
}
