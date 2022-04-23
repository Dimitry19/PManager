package cm.travelpost.tp;


import cm.travelpost.tp.common.mail.mailjet.MailJetSender;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;


//@SpringBootApplication
public class TravelPostApplicationDemo implements CommandLineRunner {



    private static final String secret ="tr@v3lP0st";



    public  static void main(String[] args) {

        SpringApplication.run(TravelPostApplicationDemo.class, args);
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


        System.out.println("21457bfe15c24588d5c7a67c8928c432:"+encryptor.encrypt("21457bfe15c24588d5c7a67c8928c432"));
        System.out.println("Orlynn1anais1986yann:"+encryptor.encrypt("Orlynn1anais1986yann"));
        System.out.println("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 :"+encryptor.encrypt("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"));


    }


    @Override
    public void run(String... args) throws Exception {
        secure();
    }

}
