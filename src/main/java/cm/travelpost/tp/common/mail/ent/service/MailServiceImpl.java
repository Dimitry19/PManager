package cm.travelpost.tp.common.mail.ent.service;

import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.mail.CommonMailSenderService;
import cm.travelpost.tp.common.mail.MailType;
import cm.travelpost.tp.common.mail.TravelPostMailSender;
import cm.travelpost.tp.common.mail.ent.dao.ContactUSDAO;
import cm.travelpost.tp.common.mail.ent.vo.ContactUSVO;
import cm.travelpost.tp.common.mail.sendgrid.MailSenderSendGrid;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.HTMLEntities;
import cm.travelpost.tp.common.utils.MailUtils;
import cm.travelpost.tp.user.ent.service.UserService;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.mail.ContactUSDTO;
import cm.travelpost.tp.ws.requests.mail.MailDTO;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.sendgrid.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MailServiceImpl extends CommonMailSenderService implements MailService {

    public static final String USER_WS = "/ws/user";

    @Autowired
    private TravelPostMailSender personalMailSender;

    @Autowired
    MailSenderSendGrid mailSenderSendGrid;

    @Autowired
    IGoogleMailSenderService googleMailSenderService;

    @Autowired
    ContactUSDAO contactUSDAO;

    @Autowired
	UserService userService;


    @Override
    public void sendEmail(UserVO user) throws MailException {

    }

    @Override
    public boolean contactUS(ContactUSDTO contactus) throws Exception {

        ContactUSVO contactUS = new ContactUSVO();
        contactUS.setPseudo(contactus.getPseudo());
        contactUS.setSender(contactus.getSender());
        contactUS.setContent(contactus.getContent());
        contactUS.setSubject(contactus.getSubject());
        contactUS.setReceiver(personalMailSender.getDefaultContactUs());

        Long id=(Long) contactUSDAO.save(contactUS);

        boolean sent = personalMailSender.contactUs(contactUS.getSender(), contactUS.getPseudo(), contactUS.getReceiver(),
                personalMailSender.getTravelPostPseudo(), null, null,
                null, null, contactUS.getSubject(), contactUS.getContent()+template(contactus.getSender(),true), null,true);

       // googleMailSenderService.contactUs(contactUS);
        return sent || id!=null;
    }




    public boolean sendConfirmationMail(HttpServletRequest request, UserVO user) throws UserException, IOException, MailjetSocketTimeoutException, MailjetException, MessagingException {

        String appUrl = HTMLEntities.buildUrl(request, USER_WS);
        StringBuilder sblink = new StringBuilder();
        StringBuilder sblinkNoTemplate = new StringBuilder();

        sblink.append("<a href=");
        sblink.append(appUrl);
        sblinkNoTemplate.append(appUrl);
        sblink.append("/confirm?token=");
        sblinkNoTemplate.append("/confirm?token=");
        sblink.append(user.getConfirmationToken());
        sblinkNoTemplate.append(user.getConfirmationToken());
        sblink.append(">Activer votre compte</a>");

        String body = MailType.CONFIRM_TEMPLATE_BODY + sblink.toString();
        String bodyNoTemplate = MailType.CONFIRM_TEMPLATE_BODY + sblinkNoTemplate.toString();
        List<String> labels = new ArrayList();
        List<String> emails = new ArrayList();

        labels.add(MailType.BODY_KEY);

        emails.add(user.getEmail());

        Map<String,String> emailTo= new HashMap();
        emailTo.put(user.getUsername(),user.getEmail());

        String title=MessageFormat.format(MailType.CONFIRM_TEMPLATE_TITLE,travelPostPseudo);

         googleMailSenderService.sendMail(title,emails,null,null,null,user.getUsername(),bodyNoTemplate,false);
         personalMailSender.send(MailType.CONFIRM_TEMPLATE, title, MailUtils.replace(user, labels, body, null),emailTo, null,null,null,null,false,null);

        return mailSenderSendGrid.manageResponse(mailSenderSendGrid.sendMailMessage(MailType.CONFIRM_TEMPLATE, title, MailUtils.replace(user, labels, body, null),emails, null, null, null, user.getUsername(), null, true));
    }

    public Response sendMail(MailDTO mr, boolean active) throws Exception {

        UserVO user = userService.findByEmail(mr.getFrom());


        if (user != null) {

            List<String> labels = new ArrayList<String>();

            labels.add(MailType.BODY_KEY);

            Map<String,String> emailTo= new HashMap();
            emailTo.put(user.getUsername(),user.getEmail());

            Map<String,String> emailCc= fillDestinations(mr.getCc());
            Map<String,String> emailBCc= fillDestinations(mr.getBcc());


            personalMailSender.send(MailType.CONFIRM_TEMPLATE, MailType.CONFIRM_TEMPLATE_TITLE, MailUtils.replace(user, labels,  mr.getBody(), null),
                    emailTo, emailCc,emailBCc,null,null,true, null);

            return mailSenderSendGrid.sendMailMessage(MailType.SEND_MAIL_TEMPLATE, mr.getSubject(), MailUtils.replace(user, labels, mr.getBody(), null),
                    mr.getTo(), mr.getCc(), mr.getBcc(), mr.getFrom(), user.getUsername(), null, true);
        }
        return null;
    }




    private   Map<String,String> fillDestinations(List<String> emails){

        Map<String,String> mails = new HashMap();
        if(CollectionsUtils.isNotEmpty(emails)){
            emails.stream().forEach(e->{
                mails.put("Key",e);

            });
        }
        return mails;
    }

}
