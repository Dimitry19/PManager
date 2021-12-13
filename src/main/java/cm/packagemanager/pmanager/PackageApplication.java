package cm.packagemanager.pmanager;


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
import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import static cm.packagemanager.pmanager.websocket.constants.WebSocketConstants.*;


/*
 * https://o7planning.org/11665/spring-boot-hibernate-and-spring-transaction
 * https://www.springboottutorial.com/hibernate-jpa-tutorial-with-spring-boot-starter-jpa
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableBatchProcessing
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "cm.packagemanager.pmanager")
public class PackageApplication extends SpringBootServletInitializer {

    private static Logger logger = LoggerFactory.getLogger(PackageApplication.class);
    
    @Autowired
	private SimpMessageSendingOperations messagingTemplate;

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(PackageApplication.class);
        application.run(args);
    }

    // Pour deployer dans tomcat externe
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PackageApplication.class);
    }
    
   @Bean
	public CommandLineRunner websocketDemo() {
		return (args) -> {
			while (true) {
				try {
					Thread.sleep(3*1000); // Each 3 sec.
					Notification notification = new Notification("Test Notification", "Bonjour Ludo", null, null);
                    notification.setTopic(SUSCRIBE_QUEUE_ANNOUNCE_SEND);
          			messagingTemplate.convertAndSend(notification.getTopic(), notification.getMessage());
					 
					notification.setTopic(SUSCRIBE_QUEUE_COMMENT_SEND);
          			messagingTemplate.convertAndSend(notification.getTopic(), notification.getMessage());
					
					notification.setTopic(SUSCRIBE_QUEUE_USER_SEND);
          			messagingTemplate.convertAndSend(notification.getTopic(), notification.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

}
