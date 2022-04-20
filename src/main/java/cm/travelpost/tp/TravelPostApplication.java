package cm.travelpost.tp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * https://o7planning.org/11665/spring-boot-hibernate-and-spring-transaction
 * https://www.springboottutorial.com/hibernate-jpa-tutorial-with-spring-boot-starter-jpa
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableBatchProcessing
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"cm.travelpost.tp","cm.framework.ds"})
public class TravelPostApplication extends SpringBootServletInitializer {

    private static Logger log = LoggerFactory.getLogger(TravelPostApplication.class);

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(TravelPostApplication.class);
        if(log.isDebugEnabled()){
            log.info("Application  starts....");
        }
        application.run(args);
    }

    // Pour deployer dans tomcat externe
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TravelPostApplication.class);
    }
}
