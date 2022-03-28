package cm.packagemanager.pmanager;


import cm.packagemanager.pmanager.common.mail.ent.service.IGoogleMailSenderServiceImpl;
import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;

//@SpringBootApplication
public class PackageApplicationDemo   implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IGoogleMailSenderServiceImpl iGoogleMailSenderService;

    public static void main(String[] args) {

        //Response res=testSend();
        //System.out.println(res.getStatusCode());
        SpringApplication.run(PackageApplicationDemo.class, args);
    }

/*
    @Component
    class Dummy implements CommandLineRunner {

        @Override
        public void run(String... string) throws Exception {

        }
    }
*/

    @Override
    public void run(String... args) {

        System.out.println("Sending Email...");

        iGoogleMailSenderService.sendMail();
        //sendEmailWithAttachment();

        System.out.println("Done");

    }

    public void sendEmail() throws MailException {




        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);

        mailSender.setUsername("packagemanager2020@gmail.com");
        mailSender.setPassword("pmanager2020");

        //from email id and password
        //System.out.println("Username is : " + String.valueOf(resourceList.get(0)).split("@")[0]);
        //System.out.println("Password is : " + String.valueOf(resourceList.get(1)));

        Properties mailProp = mailSender.getJavaMailProperties();
        mailProp.put("mail.transport.protocol", "smtp");
        mailProp.put("mail.smtp.auth", "true");
        mailProp.put("mail.smtp.starttls.enable", "true");
        mailProp.put("mail.smtp.starttls.required", "true");
        mailProp.put("mail.debug", "true");
        mailProp.put("mail.smtp.ssl.enable", "true");

        /*
         * This JavaMailSender Interface is used to send Mail in Spring Boot. This
         * JavaMailSender extends the MailSender Interface which contains send()
         * function. SimpleMailMessage Object is required because send() function uses
         * object of SimpleMailMessage as a Parameter
         */

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("dimipasc@yahoo.fr");
        mail.setSubject("Testing Mail API");
        mail.setText("Hurray ! You have done that dude...");

        /*
         * This send() contains an Object of SimpleMailMessage as an Parameter
         */
        mailSender.send(mail);
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
