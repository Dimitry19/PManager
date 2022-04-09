package cm.travelpost.tp.batch.jobs.cron.announce;

import cm.travelpost.tp.announce.ent.dao.AnnounceDAO;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class AnnounceManagerJob implements Job {

    private static Logger log = LoggerFactory.getLogger(AnnounceManagerJob.class);

    @Autowired
	AnnounceDAO announceDAO;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        try {
            announceDAO.announcesStatus();
        } catch (Exception e) {
            log.error("{}", context.getJobDetail().getKey().getName());
            e.printStackTrace();
        }

        log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}