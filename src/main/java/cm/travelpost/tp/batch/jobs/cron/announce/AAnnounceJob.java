package cm.travelpost.tp.batch.jobs.cron.announce;

import cm.travelpost.tp.announce.ent.dao.AnnounceDAO;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;


@ManagedBean
public  class AAnnounceJob implements Job {

    @Autowired
    protected AnnounceDAO announceDAO;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
