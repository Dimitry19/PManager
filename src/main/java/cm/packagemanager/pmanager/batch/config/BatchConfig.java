package cm.packagemanager.pmanager.batch.config;


import cm.packagemanager.pmanager.batch.listerners.JobResultListener;
import cm.packagemanager.pmanager.batch.listerners.StepResultListener;
import cm.packagemanager.pmanager.batch.tasks.ConfirmUsersTask;
import cm.packagemanager.pmanager.batch.tasks.CopyOldAnnouncesTask;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.service.UserService;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*https://howtodoinjava.com/spring-batch/java-config-multiple-steps/*/

@Configuration("stepByStep")
//@EnableBatchProcessing // enable batch support for our application
public class BatchConfig {

    private static Logger log = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserService userService;


    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Step stepOne() {
        return steps.get("stepOne")
                .tasklet(new ConfirmUsersTask())
                .listener(new StepResultListener())
                .build();
    }

    @Bean
    public Step stepTwo() {
        return steps.get("stepTwo")
                .tasklet(new CopyOldAnnouncesTask())
                .build();
    }

    @Bean(name = "copyOldAnnounces")
    public Job copyOldAnnounces() {
        return jobs.get("copyOldAnnounces")
                .incrementer(new RunIdIncrementer())
                .listener(new JobResultListener())
                .start(stepOne())
                .next(stepTwo())
                .build();
    }

    @Bean(name = "confirmUsers")
    public Job confirmUsers() {
        return jobs.get("confirmUsers")
                .flow(stepOne())
                .build()
                .build();
    }
}