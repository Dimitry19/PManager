package cm.packagemanager.pmanager;

import cm.packagemanager.pmanager.user.ent.bo.UserBO;
import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@SpringBootApplication(scanBasePackages = "cm.packagemanager.pmanager")
public class PackageApplicationDemo {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public static void main(String[] args) {

        Response res=testSend();
        System.out.println(res.getStatusCode());
        //SpringApplication.run(PackageApplicationDemo.class, args);
    }

    @Component
    class Dummy implements CommandLineRunner {


        UserBO userBO;


        @Override
        public void run(String... string) throws Exception {

        }
    }

    private static Response testSend() {

        Email from = new Email("packagemanager2020@gmail.com");
        String subject = "Sending with Twilio SendGrid is Fun";
        Email to = new Email("dimipasc@yahoo.fr");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            return sg.api(request);

        } catch (IOException ex) {

        }
        return  null;

    }

}
