package cm.framework.ds.activity.writer;

import cm.framework.ds.activity.ent.service.ActivityService;
import cm.framework.ds.activity.ent.vo.ActivityIdVO;
import cm.framework.ds.activity.ent.vo.ActivityVO;
import cm.framework.ds.activity.enums.ActivityOperation;
import cm.framework.ds.common.utils.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cm.travelpost.tp.common.Constants.DEFAULT_TOKEN;

@Component
public class ActivityWriter {

    public static final String ACTIVITY_PREFIX = "AV";

    @Autowired
    ActivityService service;

    public  ActivityVO logActivity(String activityDescription, ActivityOperation operation, Long userId, Object o){

        switch (operation){
            case CREATE:
                ActivityVO activity= new ActivityVO();
                ActivityIdVO id = new ActivityIdVO(CodeGenerator.generateCode(DEFAULT_TOKEN,ACTIVITY_PREFIX), DEFAULT_TOKEN);
                activity.setId(id);
                activity.setActivity(activityDescription);
                activity.setOperation(operation);
                activity.setUserId(userId);
                return  (ActivityVO) service.create(activity);
            case READ:
                ActivityIdVO activityId = (ActivityIdVO)o;
                return  (ActivityVO) service.read(activityId);
            case UPDATE:
            break;
            case DELETE:
                ActivityIdVO actId = (ActivityIdVO)o;
                service.read(actId);
            break;
        }
        return null;
    }

}
