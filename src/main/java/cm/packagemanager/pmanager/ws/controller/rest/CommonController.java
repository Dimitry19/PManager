package cm.packagemanager.pmanager.ws.controller.rest;

import cm.packagemanager.pmanager.airline.ent.service.AirlineService;
import cm.packagemanager.pmanager.announce.ent.service.AnnounceService;
import cm.packagemanager.pmanager.announce.ent.service.ReservationService;
import cm.packagemanager.pmanager.city.ent.service.CityService;
import cm.packagemanager.pmanager.common.mail.ent.service.MailService;
import cm.packagemanager.pmanager.common.mail.sendgrid.MailSenderSendGrid;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.common.utils.FileUtils;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.image.ent.vo.ImageVO;
import cm.packagemanager.pmanager.message.ent.service.MessageService;
import cm.packagemanager.pmanager.notification.ent.service.NotificationService;
import cm.packagemanager.pmanager.user.ent.service.RoleService;
import cm.packagemanager.pmanager.user.ent.service.UserService;
import cm.packagemanager.pmanager.ws.controller.RedirectType;
import cm.packagemanager.pmanager.ws.responses.PaginateResponse;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import com.sun.mail.smtp.SMTPSendFailedException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * L'annotation @CrossOrigin(origins = "http://localhost:8080", maxAge = 3600) permet de favoriser une communication distante entre le client et le serveur,
 * c'est-à-dire lorsque le client et le serveur sont déployés dans deux serveurs distincts, ce qui permet d'éviter des problèmes réseaux.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Component
public class CommonController  extends WSConstants {


    protected final Log logger = LogFactory.getLog(CommonController.class);

    public static final String HEADER_TOTAL = "x-total-count";


    @Value("${redirect.page}")
    protected String redirectPage;

    @Value("${redirect.page.error}")
    protected String redirectPageError;

    @Value("${redirect.confirm.ok.page}")
    protected String redirectConfirmPage;

    @Value("${redirect.confirm.error.page}")
    protected String redirectConfirmErrorPage;




    @Autowired
    protected AirlineService airplaneService;

    @Autowired
    protected CityService cityService;


    @Autowired
    protected UserService userService;

    @Autowired
    protected NotificationService notificationService;

    @Autowired
    protected MailSenderSendGrid mailSenderSendGrid;

    @Autowired
    protected RoleService roleService;

    @Autowired
    protected ReservationService reservationService;

    @Autowired
    protected AnnounceService announceService;


    @Autowired
    protected MailService mailService;

    @Autowired
    protected MessageService messageService;


    @Autowired
    protected ServletContext servletContext;

    @Qualifier("jaegerTracer")
    @Autowired
    protected Tracer gTracer;

    @Value("${pagination.size}")
    public Integer size;

    @Autowired
    FileUtils fileUtils;


    protected Span packageManagerSpan;


    @PostConstruct
    public void init() {
       if (!GlobalTracer.isRegistered()){
            GlobalTracer.register(gTracer);
       }

    }

    protected void createOpentracingSpan(String spanName) {
        packageManagerSpan = GlobalTracer.get().buildSpan(spanName).start();
    }

    protected void finishOpentracingSpan() {
        if (packageManagerSpan != null) {
            packageManagerSpan.finish();
        }
    }


    protected  ResponseEntity<byte []> manageImage(String filename,byte[] data) throws IOException {

         String contentType = servletContext.getMimeType(filename);
         return ResponseEntity
                .ok()
                .contentType(org.springframework.http.MediaType.valueOf(contentType))
                .body(data);

    }
    protected boolean imageCheck(ImageVO image){
        return (image!=null && image.getPicByte()!=null);

    }



    protected ResponseEntity<PaginateResponse> getPaginateResponseResponseEntity(HttpHeaders headers, PaginateResponse paginateResponse, int count,List results) {

        if(CollectionsUtils.isNotEmpty(results)){
            switch (count) {
                case 0:
                    headers.add(HEADER_TOTAL, Long.toString(count));
                    paginateResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    paginateResponse.setRetDescription(WebServiceResponseCode.PAGINATE_EMPTY_RESPONSE_LABEL);
                    break;
                default:

                    if (CollectionsUtils.isNotEmpty(results)) {
                        paginateResponse.setCount(count);
                    }
                    paginateResponse.setResults(results);
                    paginateResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    paginateResponse.setRetDescription(WebServiceResponseCode.PAGINATE_RESPONSE_LABEL);
                    headers.add(HEADER_TOTAL, Long.toString(results.size()));
                    break;
            }
            return new ResponseEntity<>(paginateResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(paginateResponse, HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity<PaginateResponse> getPaginateResponseResponseEntity(HttpHeaders headers, PaginateResponse paginateResponse,List results) {

        if (CollectionsUtils.isEmpty(results)) {
            headers.add(HEADER_TOTAL, Long.toString(0));
            paginateResponse.setRetCode(WebServiceResponseCode.OK_CODE);
            paginateResponse.setRetDescription(WebServiceResponseCode.PAGINATE_EMPTY_RESPONSE_LABEL);
        } else {
            paginateResponse.setCount(results.size());
            paginateResponse.setResults(results);
            paginateResponse.setRetCode(WebServiceResponseCode.OK_CODE);
            paginateResponse.setRetDescription(WebServiceResponseCode.PAGINATE_RESPONSE_LABEL);
            headers.add(HEADER_TOTAL, Long.toString(results.size()));
        }
        return new ResponseEntity<>(paginateResponse, HttpStatus.OK);
    }

    @NotNull
    protected   ResponseEntity<Response> getResponseMailResponseEntity(Response pmResponse, Exception e, String message) {
        pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
        if(e instanceof MessagingException || e instanceof SMTPSendFailedException || e instanceof MailSendException){
            pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_MAIL_SERVICE_UNAVAILABLE_LABEL,message));
        }else{
            pmResponse.setRetDescription(WebServiceResponseCode.ERROR_USER_REGISTER_LABEL);
        }
        return new ResponseEntity<Response>(pmResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    public String getRedirectPage(RedirectType type) {

        StringBuilder redirectSb = new StringBuilder(contextRoot);
        switch (type){
            case INDEX:
                redirectSb.append(redirectPage);
            case CONFIRMATION:
                redirectSb.append(redirectConfirmPage);
            case CONFIRMATION_ERROR:
                redirectSb.append(redirectConfirmErrorPage);
            case ERROR:
                redirectSb.append(redirectPageError);
        }
        return redirectSb.toString();
    }
}
