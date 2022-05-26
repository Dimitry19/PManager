package cm.travelpost.tp.ws.controller.rest;

import cm.framework.ds.common.authentication.service.AuthenticationService;
import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.common.ent.vo.WSCommonResponseVO;
import cm.framework.ds.common.security.jwt.TokenProvider;
import cm.travelpost.tp.airline.ent.service.AirlineService;
import cm.travelpost.tp.announce.ent.service.AnnounceService;
import cm.travelpost.tp.announce.ent.service.ReservationService;
import cm.travelpost.tp.city.ent.service.CityService;
import cm.travelpost.tp.common.mail.ent.service.MailService;
import cm.travelpost.tp.common.mail.sendgrid.MailSenderSendGrid;
import cm.travelpost.tp.common.sms.ent.service.SmsService;
import cm.travelpost.tp.common.sms.ent.service.TextMagicService;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.FileUtils;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.image.ent.vo.ImageVO;
import cm.travelpost.tp.message.ent.service.MessageService;
import cm.travelpost.tp.notification.ent.service.NotificationService;
import cm.travelpost.tp.user.ent.service.RoleService;
import cm.travelpost.tp.user.ent.service.UserService;
import cm.travelpost.tp.ws.controller.RedirectType;
import cm.travelpost.tp.ws.responses.PaginateResponse;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;

/**
 * L'annotation @CrossOrigin(origins = "http://localhost:8080", maxAge = 3600) permet de favoriser une communication distante entre le client et le serveur,
 * c'est-à-dire lorsque le client et le serveur sont déployés dans deux serveurs distincts, ce qui permet d'éviter des problèmes réseaux.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Component
public class CommonController  extends WSConstants {


    private final Log log = LogFactory.getLog(CommonController.class);

    public static final String HEADER_TOTAL = "x-total-count";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN_VALUE = "*";


    @Value("${redirect.page}")
    protected String redirectPage;

    @Value("${redirect.page.error}")
    protected String redirectPageError;

    @Value("${redirect.confirm.ok.page}")
    protected String redirectConfirmPage;

    @Value("${redirect.confirm.error.page}")
    protected String redirectConfirmErrorPage;


    @Autowired
    protected AirlineService airlineService;

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
    protected TokenProvider tokenProvider;

    @Autowired
    protected AuthenticationService authenticationService;


    /** SMS Services **/
    @Autowired
    protected SmsService smsService;

    @Autowired
    protected TextMagicService tmSmsService;

    /** SMS Services **/


    @Autowired
    protected SimpMessagingTemplate socket;


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


    protected  ResponseEntity<byte []> manageImage(String filename,byte[] data)  {

         String contentType = servletContext.getMimeType(filename);
         return ResponseEntity
                .ok()
                .contentType(org.springframework.http.MediaType.valueOf(contentType))
                .body(data);

    }
    protected boolean imageCheck(ImageVO image){
        return (image!=null && image.getPicByte()!=null);

    }



    protected ResponseEntity<PaginateResponse> getPaginateResponseResponseEntity(HttpHeaders headers, PaginateResponse paginateResponse, int count, List results) {

        if (count == 0) {
            headers.add(HEADER_TOTAL, Long.toString(count));
            paginateResponse.setRetCode(WebServiceResponseCode.OK_CODE);
            paginateResponse.setRetDescription(WebServiceResponseCode.PAGINATE_EMPTY_RESPONSE_LABEL);
        } else {

            if (CollectionsUtils.isNotEmpty(results)) {
                paginateResponse.setCount(count);
            }
            paginateResponse.setResults(results);
            paginateResponse.setRetCode(WebServiceResponseCode.OK_CODE);
            paginateResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.PAGINATE_RESPONSE_LABEL,count));
            headers.add(HEADER_TOTAL, Long.toString(results.size()));
        }
        return new ResponseEntity<>(paginateResponse, HttpStatus.OK);
    }

    protected ResponseEntity<PaginateResponse> getPaginateResponseSearchResponseEntity(HttpHeaders headers, PaginateResponse paginateResponse, int count, List results, PageBy pageBy ) {

        if (count == 0) {
            headers.add(HEADER_TOTAL, Long.toString(count));
            paginateResponse.setRetCode(WebServiceResponseCode.OK_CODE);
            paginateResponse.setRetDescription(WebServiceResponseCode.PAGINATE_EMPTY_RESPONSE_LABEL);
        } else {

            if (CollectionsUtils.isNotEmpty(results)) {
                paginateResponse.setCount(count);
            }
            paginateResponse.setResults(results);
            paginateResponse.setRetCode(WebServiceResponseCode.OK_CODE);

            if(pageBy == null ||  pageBy.getPage()==Integer.valueOf(DEFAULT_PAGE)){
                paginateResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.PAGINATE_RESPONSE_LABEL,count));
            }
            headers.add(HEADER_TOTAL, Long.toString(results.size()));
        }
        return new ResponseEntity<>(paginateResponse, HttpStatus.OK);
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
            paginateResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.PAGINATE_RESPONSE_LABEL, CollectionsUtils.size(results)));
            headers.add(HEADER_TOTAL, Long.toString(results.size()));
        }
        return new ResponseEntity<>(paginateResponse, HttpStatus.OK);
    }

    @NotNull
    protected   ResponseEntity<Response> getResponseMailResponseEntity(Response pmResponse, Exception e, String message) {
        pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
        if(e instanceof MessagingException || e instanceof SMTPSendFailedException || e instanceof MailSendException){
            pmResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_MAIL_SERVICE_UNAVAILABLE_LABEL,message));
        }else{
            pmResponse.setMessage(WebServiceResponseCode.ERROR_USER_REGISTER_LABEL);
        }
        pmResponse.setRetDescription(null);
        return new ResponseEntity<>(pmResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @NotNull
    public static ResponseEntity<Response> getResponseDeleteResponseEntity(@RequestParam @Valid Long id, Response pmResponse, boolean delete, String label)  {
        if (id != null) {
            if (delete) {
                pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, label));

                return new ResponseEntity<>(pmResponse, HttpStatus.OK);
            } else {
                pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                pmResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, label));

            }
        }
        return new ResponseEntity<>(pmResponse, HttpStatus.NOT_FOUND);
    }


    protected  ResponseEntity<Object> getResponseLoginErrorResponseEntity(String errorDescription){
        WSCommonResponseVO commonResponse = new WSCommonResponseVO();
        commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
        commonResponse.setMessage(errorDescription);
        return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);
    }

    public String getRedirectPage(RedirectType type) {
        StringBuilder redirectSb = new StringBuilder(contextRoot);
        switch (type){
            case INDEX:
                redirectSb.append(redirectPage);
                break;
            case CONFIRMATION:
                redirectSb.append(redirectConfirmPage);
                break;
            case CONFIRMATION_ERROR:
                redirectSb.append(redirectConfirmErrorPage);
                break;
            case ERROR:
                redirectSb.append(redirectPageError);
                break;
            default:
                    break;
        }
        return redirectSb.toString();
    }
}
