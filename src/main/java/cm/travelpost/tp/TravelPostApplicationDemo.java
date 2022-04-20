package cm.travelpost.tp;


import cm.travelpost.tp.common.mail.mailjet.MailJetSender;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;


//@SpringBootApplication
public class TravelPostApplicationDemo implements CommandLineRunner {






    public  static void main(String[] args) {

        SpringApplication.run(TravelPostApplicationDemo.class, args);
    }




    public  static void runMail() throws MailjetSocketTimeoutException, MailjetException {

    String template="<h3>Dear passenger 1, welcome to <a href='https://www.mailjet.com/'>Mailjet</a>!</h3><br />May the delivery force be with you!";
         MailJetSender mailJetSender = new MailJetSender();
        mailJetSender.send("unkutnation@gmail.com","Claude","unkutnation@gmail.com","Lion Dx",
                null,null,null,null,"Confirmation","Greetings from Travel Post.",template, true);


    }


    @Override
    public void run(String... args) throws Exception {
        runMail();
    }
}
