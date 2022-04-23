package cm.travelpost.tp.batch.jobs.cron.announce;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@DisallowConcurrentExecution
public class AnnounceNotificateJob extends AAnnounceJob  {

    private static Logger log = LoggerFactory.getLogger(AnnounceNotificateJob.class);



    @Override
    public void execute(JobExecutionContext context) {
        log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        try {
            announceDAO.announcesToNotificate(7);
        } catch (Exception e) {
            log.error("{}", context.getJobDetail().getKey().getName());
            e.printStackTrace();
        }

        log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}