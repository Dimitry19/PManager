package cm.packagemanager.pmanager.batch.jobs.cron.announce;

import cm.packagemanager.pmanager.batch.config.cron.QuartzCronConfiguration;
import cm.packagemanager.pmanager.batch.jobs.cron.QuartzSubmitJobs;
import cm.packagemanager.pmanager.batch.jobs.cron.user.UserManagerJob;
import org.quartz.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class AnnounceSubmitJobs extends QuartzSubmitJobs {

	private static Logger log = LoggerFactory.getLogger(AnnounceSubmitJobs.class);

	@Bean(name = "changeStatusAnnounce")
	public JobDetailFactoryBean jobChangeStatusAnnounce() {
		return QuartzCronConfiguration.createJobDetail(AnnounceManagerJob.class, "Change Status Announce Job");
	}

	@Bean(name = "changeStatusAnnounceTrigger")
	public CronTriggerFactoryBean triggerChangeStatusAnnounce(@Qualifier("changeStatusAnnounce") JobDetail jobDetail) {
		log.info("Change Status Announce Job started...");
		return QuartzCronConfiguration.createCronTrigger(jobDetail, CRON_EVERY_SATURDAY_AT_15, "Change Status AnnounceTrigger");
	}
}
