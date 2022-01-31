package cm.packagemanager.pmanager.ws.controller.rest;

import cm.packagemanager.pmanager.announce.ent.service.AnnounceService;
import cm.packagemanager.pmanager.announce.ent.service.ReservationService;
import cm.packagemanager.pmanager.announce.ent.service.ServiceAnnounce;
import cm.packagemanager.pmanager.common.ent.vo.ImageVO;
import cm.packagemanager.pmanager.common.mail.MailSender;
import cm.packagemanager.pmanager.common.mail.ent.service.MailService;
import cm.packagemanager.pmanager.common.session.SessionManager;
import cm.packagemanager.pmanager.common.utils.FileUtils;
import cm.packagemanager.pmanager.message.ent.service.MessageService;
import cm.packagemanager.pmanager.notification.firebase.ent.service.NotificationService;
import cm.packagemanager.pmanager.notification.firebase.ent.service.PushNotificationService;
import cm.packagemanager.pmanager.user.ent.service.RoleService;
import cm.packagemanager.pmanager.user.ent.service.UserService;
import cm.packagemanager.pmanager.websocket.client.listeners.SessionListener;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.apache.catalina.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.WebSession;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * L'annotation @CrossOrigin(origins = "http://localhost:8080", maxAge = 3600) permet de favoriser une communication distante entre le client et le serveur,
 * c'est-à-dire lorsque le client et le serveur sont déployés dans deux serveurs distincts, ce qui permet d'éviter des problèmes réseaux.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Component
public class CommonController  {


    protected final Log logger = LogFactory.getLog(CommonController.class);
    public static final String DEFAULT_SIZE = "12";
    public static final String DEFAULT_PAGE = "0";
    public static final String HEADER_TOTAL = "x-total-count";


    @Autowired
    public UserService userService;

    @Autowired
    public NotificationService notificationService;

    @Autowired
    public MailSender mailSender;

    @Autowired
    public RoleService roleService;

    @Autowired
    public ReservationService reservationService;

    @Autowired
    public AnnounceService announceService;
    @Autowired
    public ServiceAnnounce announceTester;

    @Autowired
    public MailService mailService;

    @Autowired
    public MessageService messageService;


    @Autowired
    public PushNotificationService pushNotificationService;


    @Autowired
    public ServletContext servletContext;

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
}
