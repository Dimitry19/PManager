package cm.packagemanager.pmanager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;




@SpringBootApplication
@EnableAutoConfiguration(exclude = { //
		DataSourceAutoConfiguration.class, //
		DataSourceTransactionManagerAutoConfiguration.class, //
		HibernateJpaAutoConfiguration.class })
//@EnableJpaRepositories(basePackages = "cm.packagemanager.pmanager")
//@SpringBootApplication(scanBasePackages = "cm.packagemanager.pmanager")
public class PackageApplication extends SpringBootServletInitializer {

	private static Logger logger = LoggerFactory.getLogger(PackageApplication.class);


	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(PackageApplication.class);
		application.run(args);
	}


}
