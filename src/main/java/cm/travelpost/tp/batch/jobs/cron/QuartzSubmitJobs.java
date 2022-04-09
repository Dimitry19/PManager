package cm.travelpost.tp.batch.jobs.cron;

import org.springframework.context.annotation.Configuration;


@Configuration
public class QuartzSubmitJobs {
    protected static final String CRON_EVERY_FIVE_MINUTES = "0 0/5 * ? * * *";
    protected static final String CRON_EVERY_SIX_HOURS = "0 0 */6 ? * *";
    protected static final String CRON_EVERY_TWO_MINUTES = "0 0/2 * ? * * *";
    protected static final String CRON_EVERY_SATURDAY_AT_15 = "0 0 15 ? * SAT";



	/*@Bean(name = "userStats")
	public JobDetailFactoryBean jobMemberStats() {
		return QuartzCronConfiguration.createJobDetail(Demo.class, "Member Statistics Job");
	}

	@Bean(name = "memberStatsTrigger")
	public SimpleTriggerFactoryBean triggerMemberStats(@Qualifier("userStats") JobDetail jobDetail) {
		return QuartzCronConfiguration.createTrigger(jobDetail, 60000, "Member Statistics Trigger");
	}*/
}
