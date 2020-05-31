package cm.packagemanager.pmanager;

import cm.packagemanager.pmanager.user.ent.bo.AuthUserBO;
import cm.packagemanager.pmanager.user.ent.bo.UserBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class PackageApplication {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserBO userBO;

	@Autowired
	AuthUserBO authUserBO;

	public static void main(String[] args) {

		SpringApplication.run(PackageApplication.class, args);
	}



}
