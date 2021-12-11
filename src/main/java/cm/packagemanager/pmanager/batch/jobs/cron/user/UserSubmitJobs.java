package cm.packagemanager.pmanager.batch.jobs.cron.user;

import cm.packagemanager.pmanager.batch.config.cron.QuartzCronConfiguration;
import cm.packagemanager.pmanager.batch.jobs.cron.QuartzSubmitJobs;
import org.quartz.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class UserSubmitJobs extends QuartzSubmitJobs {

    private static Logger log = LoggerFactory.getLogger(UserSubmitJobs.class);

    @Bean(name = "confirmUsers")
    public JobDetailFactoryBean jobConfirmUsers() {
        return QuartzCronConfiguration.createJobDetail(UserManagerJob.class, "User comfirmation Job");
    }

    @Bean(name = "confirmUsersTrigger")
    public CronTriggerFactoryBean triggerConfirmUsers(@Qualifier("confirmUsers") JobDetail jobDetail) {
        log.info("User comfirmation Job started...");

        return QuartzCronConfiguration.createCronTrigger(jobDetail, CRON_EVERY_SIX_HOURS, "User comfirmation Trigger");
    }
}
