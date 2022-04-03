package cm.packagemanager.pmanager;


import cm.packagemanager.pmanager.common.mail.mailjet.MailJetSender;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;


//@SpringBootApplication
public class TravelPostApplicationDemo implements CommandLineRunner {


    private Logger logger = LoggerFactory.getLogger(this.getClass());



    public  static void main(String[] args) throws MailjetSocketTimeoutException, MailjetException {
       // runMail();
        //Response res=testSend();
        //System.out.println(res.getStatusCode());
        SpringApplication.run(TravelPostApplicationDemo.class, args);
    }

/*
    @Component
    class Dummy implements CommandLineRunner {

        @Override
        public void run(String... string) throws Exception {

        }
    }
*/


    public  static void runMail() throws MailjetSocketTimeoutException, MailjetException {

    String template="<h3>Dear passenger 1, welcome to <a href='https://www.mailjet.com/'>Mailjet</a>!</h3><br />May the delivery force be with you!";
         MailJetSender mailJetSender = new MailJetSender();
        mailJetSender.send("unkutnation@gmail.com","Claude","unkutnation@gmail.com","Lion Dx",
                null,null,null,null,"Confirmation","Greetings from Travel Post.",template, true);

      /*  MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient("59ba0f382c4873d8c019dab9ec913d95", "21457bfe15c24588d5c7a67c8928c432",
                new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "unkutnation@gmail.com")
                                        .put("Name", "Claude"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "unkutnation@gmail.com")
                                                .put("Name", "Claude")))
                                .put(Emailv31.Message.SUBJECT, "Greetings from Mailjet.")
                                .put(Emailv31.Message.TEXTPART, "My first Mailjet email")
                                .put(Emailv31.Message.HTMLPART, template)
                                .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")
                        ));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());*/

    }


    @Override
    public void run(String... args) throws Exception {
        runMail();
    }
}
