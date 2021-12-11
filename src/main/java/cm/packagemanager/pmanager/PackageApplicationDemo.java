package cm.packagemanager.pmanager;

import cm.packagemanager.pmanager.user.ent.bo.UserBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

//@SpringBootApplication(scanBasePackages = "cm.packagemanager.pmanager")
public class PackageApplicationDemo {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public static void main(String[] args) {

        SpringApplication.run(PackageApplicationDemo.class, args);
    }

    @Component
    class Dummy implements CommandLineRunner {


        UserBO userBO;


        @Override
        public void run(String... string) throws Exception {

        }
    }


}
