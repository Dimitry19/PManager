package cm.packagemanager.pmanager.batch.config;

import cm.packagemanager.pmanager.batch.jobs.CustomQuartzJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

//@Configuration
public class QuartzNoCronConfiguration {

    private static Logger log = LoggerFactory.getLogger(QuartzNoCronConfiguration.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLocator jobLocator;


    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public JobDetail confirmUsersJobDetail() {
        //Set Job data map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "confirmUsers");
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(CustomQuartzJob.class)
                .withIdentity("confirmUsers")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public JobDetail copyOldAnnouncesJobDetail() {
        //Set Job data map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "copyOldAnnounces");
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(CustomQuartzJob.class)
                .withIdentity("copyOldAnnounces")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean("confirmUsersTrigger")
    public Trigger confirmUsersTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule();
        //.withIntervalInSeconds(10)
        //.repeatForever();

        return TriggerBuilder
                .newTrigger()
                .forJob(confirmUsersJobDetail())
                .withIdentity("confirmUsersTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public Trigger copyOldAnnouncesTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule();
        //.withIntervalInSeconds(20)
        //.repeatForever();

        return TriggerBuilder
                .newTrigger()
                .forJob(copyOldAnnouncesJobDetail())
                .withIdentity("copyOldAnnouncesTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {

        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(confirmUsersTrigger(), copyOldAnnouncesTrigger());
        scheduler.setQuartzProperties(quartzProperties());
        scheduler.setJobDetails(confirmUsersJobDetail(), copyOldAnnouncesJobDetail());
        return scheduler;
    }

    @Bean
    public Properties quartzProperties() throws IOException {

        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

}
