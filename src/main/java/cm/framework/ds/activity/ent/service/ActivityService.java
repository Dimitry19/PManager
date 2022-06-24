package cm.framework.ds.activity.ent.service;

import cm.framework.ds.activity.ent.vo.ActivityVO;
import cm.framework.ds.common.ent.vo.PageBy;

import java.util.List;

public interface ActivityService {

	List<ActivityVO> findByUser(Long userId, PageBy pageBy) throws Exception;
}
