package cm.travelpost.tp.batch.jobs.cron.notification;

import cm.travelpost.tp.batch.config.cron.QuartzCronConfiguration;
import cm.travelpost.tp.batch.jobs.cron.QuartzSubmitJobs;
import org.quartz.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class NotificationCompletedJob extends QuartzSubmitJobs {

    private static Logger log = LoggerFactory.getLogger(NotificationCompletedJob.class);

    @Bean(name = "deleteOldCompletedNotifications")
    public JobDetailFactoryBean jobDeleteOldCompletedNotifications() {
        return QuartzCronConfiguration.createJobDetail(NotificationManagerJob.class, "Delete old completed notifications Job");
    }

    @Bean(name = "deleteOldCompletedNotificationsTrigger")
    public CronTriggerFactoryBean triggerDeleteOldCompletedNotifications(@Qualifier("deleteOldCompletedNotifications") JobDetail jobDetail) {
        log.info("Delete old completed notifications Job started...");

        return QuartzCronConfiguration.createCronTrigger(jobDetail, CRON_EVERY_SIX_HOURS, "Delete old completed notifications Trigger");
    }
}
