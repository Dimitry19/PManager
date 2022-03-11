package cm.packagemanager.pmanager;


import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

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

        @Override
        public void run(String... string) throws Exception {

        }
    }

    private static Response testSend() {

        Email from = new Email("dimipasc@hotmail.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("packagemanager2020@gmail.com");
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
