package cm.packagemanager.pmanager.batch.jobs.cron;

import cm.packagemanager.pmanager.batch.config.cron.QuartzCronConfiguration;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Slf4j
@Component
@DisallowConcurrentExecution
public class UserManagerJob implements Job {

	private static Logger log = LoggerFactory.getLogger(QuartzCronConfiguration.class);

	@Autowired
	UserDAO userDAO;


	@Override
	public void execute(JobExecutionContext context) {
		log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

		for (UserVO userVO:userDAO.getAllUsersToConfirm()){
			System.out.println("USERNAME:" +userVO.getUsername());
			userVO.setActive(1);
			userDAO.updateUser(userVO);
			System.out.println("UPDATED USER:" +userVO.getUsername());

		}

		log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
	}
}