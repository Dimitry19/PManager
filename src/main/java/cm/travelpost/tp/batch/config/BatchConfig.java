package cm.travelpost.tp.batch.config;


import cm.travelpost.tp.batch.listerners.JobResultListener;
import cm.travelpost.tp.batch.listerners.StepResultListener;
import cm.travelpost.tp.batch.tasks.ConfirmUsersTask;
import cm.travelpost.tp.batch.tasks.CopyOldAnnouncesTask;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.service.UserService;
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
//@EnableBatchProcessing // enable batch
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