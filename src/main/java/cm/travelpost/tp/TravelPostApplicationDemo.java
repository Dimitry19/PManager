package cm.travelpost.tp;


import cm.travelpost.tp.common.mail.mailjet.MailJetSender;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


@SpringBootTest
public class TravelPostApplicationDemo implements CommandLineRunner {

    private static final String secret ="tr@v3lP0st";



    public  static void main(String[] args) throws MessagingException {

       //SpringApplication.run(TravelPostApplicationDemo.class, args);
        secure();
        //sendMailSendGridSmtp();
        //setFilters("fffdf","dddd");
    }




    public  static void runMail() throws MailjetSocketTimeoutException, MailjetException {

    String template="<h3>Dear passenger 1, welcome to <a href='https://www.mailjet.com/'>Mailjet</a>!</h3><br />May the delivery force be with you!";
         MailJetSender mailJetSender = new MailJetSender();
        mailJetSender.send("unkutnation@gmail.com","Claude","unkutnation@gmail.com","Lion Dx",
                null,null,null,null,"Confirmation","Greetings from Travel Post.",template, true);


    }


    public  static void  secure(){


        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        //SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        encryptor.setIvGenerator(new RandomIvGenerator());
        encryptor.setPassword(secret);
        encryptor.setAlgorithm("PBEWithHmacSHA512AndAES_256");
        encryptor.setPoolSize(4);
       // encryptor.setKeyObtentionIterations(1000);
       // encryptor.setProviderName("SunJCE");
        //encryptor.setStringOutputType("base64");
        //encryptor.setConfig(config);


        System.out.println("sa :"+encryptor.encrypt("bdc_d092ef590a4e40a58c1f4b2d9181517d"));
        //System.out.println(":"+encryptor.encrypt("apikey"));
        System.out.println("Decrypt :"+encryptor.decrypt(encryptor.encrypt("bdc_d092ef590a4e40a58c1f4b2d9181517d")));

    }



    protected static void  setFilters(String ...filter){

        String[] filters = null;
        List<String> listFilters = Arrays.asList(filter);

        int size  = CollectionsUtils.size(listFilters);
        filters = new String[size];
        String[] finalFilters = filters;
        listFilters.stream().forEach(f->{
            finalFilters[listFilters.indexOf(f)]=f;
        });

        for (String f: finalFilters ){
            System.out.println("Decrypt :"+f);
        }
    }

    public  static void  sendMailSendGridSmtp() throws MessagingException {

        JavaMailSenderImpl jms= loadProperties();

        MimeMessage msg = jms.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo("dimipasc@yahoo.fr");

        helper.setSubject("Testing from Spring Boot");

        //  default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText("<h1>Check attachment for image!</h1>", true);

        // hard coded a file path
        //FileSystemResource file = new FileSystemResource(new File("path/android.png"));

        helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
        msg.setSubject("test");
        msg.setHeader("From", "dimipasc@hotmail.com");
        msg.setText("Sendgrid mail sender");
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress("dimipasc@yahoo.fr"));
        jms.send(msg);



    }


    @Override
    public void run(String... args) throws Exception {
        //secure();
        sendMailSendGridSmtp();
    }


    protected static JavaMailSenderImpl loadProperties() {

        JavaMailSenderImpl jms = new JavaMailSenderImpl();
        Properties mailProp = jms.getJavaMailProperties();
        //session = Session.getDefaultInstance(mailProp, new MailUtils.SMTPAuthenticator(ADMIN_USERNAME,ADMIN_PASS));

        jms.setHost("smtp.sendgrid.net");
        jms.setPort(25);
        jms.setUsername("apikey");
        jms.setPassword("");

        mailProp.put("mail.transport.protocol",  "smtp");
        mailProp.put("mail.smtp.auth", true);
        mailProp.put("mail.smtp.starttls.enable", true);
        mailProp.put("mail.smtp.starttls.required", true);
        mailProp.put("mail.debug", true);
        mailProp.put("mail.smtp.ssl.enable",false);
        jms.setJavaMailProperties(mailProp);

      return jms;

    }

}
