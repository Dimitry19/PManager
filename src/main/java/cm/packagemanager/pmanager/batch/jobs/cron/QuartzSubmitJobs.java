package cm.packagemanager.pmanager.batch.jobs.cron;

import cm.packagemanager.pmanager.batch.config.cron.QuartzCronConfiguration;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzSubmitJobs {
	private static final String CRON_EVERY_FIVE_MINUTES = "0 0/5 * ? * * *";
	private static final String CRON_EVERY_TWO_MINUTES = "0 0/1 * ? * * *";

	/*@Bean(name = "userStats")
	public JobDetailFactoryBean jobMemberStats() {
		return QuartzConfiguration.createJobDetail(Demo.class, "Member Statistics Job");
	}

	@Bean(name = "memberStatsTrigger")
	public SimpleTriggerFactoryBean triggerMemberStats(@Qualifier("userStats") JobDetail jobDetail) {
		return QuartzConfiguration.createTrigger(jobDetail, 60000, "Member Statistics Trigger");
	}*/

	@Bean(name = "memberClassStats")
	public JobDetailFactoryBean jobMemberClassStats() {
		return QuartzCronConfiguration.createJobDetail(UserManagerJob.class, "Class Statistics Job");
	}

	@Bean(name = "memberClassStatsTrigger")
	public CronTriggerFactoryBean triggerMemberClassStats(@Qualifier("memberClassStats") JobDetail jobDetail) {
		return QuartzCronConfiguration.createCronTrigger(jobDetail, CRON_EVERY_TWO_MINUTES, "Class Statistics Trigger");
	}
}
