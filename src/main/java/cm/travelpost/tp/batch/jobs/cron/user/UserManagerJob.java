package cm.travelpost.tp.batch.jobs.cron.user;

import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@DisallowConcurrentExecution
public class UserManagerJob implements Job {

    private static Logger log = LoggerFactory.getLogger(UserManagerJob.class);

    @Autowired
	UserDAO userDAO;


    @Override
    public void execute(JobExecutionContext context) {
        log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        try {
            for (UserVO userVO : userDAO.getAllUsersToConfirm()) {
                //System.out.println("USERNAME:" + userVO.getUsername());
                userVO.setActive(1);
                userDAO.updateUser(userVO);
               // System.out.println("UPDATED USER:" + userVO.getUsername());

            }
        } catch (Exception e) {
            log.error("Error Job ** {}- {}", context.getJobDetail().getKey().getName(), e);
            e.printStackTrace();
        }

        log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}
