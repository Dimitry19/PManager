package cm.packagemanager.pmanager;

import cm.packagemanager.pmanager.user.ent.bo.AuthUserBO;
import cm.packagemanager.pmanager.user.ent.bo.UserBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PackageApplication {

	private static Logger logger = LoggerFactory.getLogger(PackageApplication.class);

	@Autowired
	UserBO userBO;

	@Autowired
	AuthUserBO authUserBO;

	public static void main(String[] args) {

		SpringApplication.run(PackageApplication.class, args);
		logger.debug("Simple log statement with inputs {}, {} and {}", 1,2,3);
	}

}
