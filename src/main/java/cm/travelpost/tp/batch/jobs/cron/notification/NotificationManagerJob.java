package cm.travelpost.tp.batch.jobs.cron.notification;

import cm.travelpost.tp.notification.ent.dao.NotificationDAO;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@DisallowConcurrentExecution
public class NotificationManagerJob implements Job {

    private static Logger log = LoggerFactory.getLogger(NotificationManagerJob.class);

    @Autowired
    NotificationDAO notificationDAO;


    @Override
    public void execute(JobExecutionContext context) {
        log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        try {
            notificationDAO.deleteOldCompletedNotifications();
        } catch (Exception e) {
            log.error("Error Job ** {}- {}", context.getJobDetail().getKey().getName(), e);
            e.printStackTrace();
        }

        log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}
