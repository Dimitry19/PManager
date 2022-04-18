package cm.travelpost.tp.configuration.cfg;



import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.user.ent.service.UserService;
import cm.travelpost.tp.ws.requests.users.LoginDTO;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

//https://www.javainuse.com/spring/spring-boot-aop

@Aspect
@Component
public class CommonAspect {

    @Autowired
    UserService userService;

    private static Logger logger = LoggerFactory.getLogger(CommonAspect.class);

    @Before(value = "execution(* cm.travelpost.tp.user.ent.service.UserService.login(..)) && args(lr)")
    public void beforeAdvice(JoinPoint joinPoint, LoginDTO lr) throws Exception {

        if (!userService.checkLogin(lr)) {
            throw new BadCredentialsException(WebServiceResponseCode.ERROR_CREDENTIALS_LABEL);
        }
        logger.info("Before method:" + joinPoint.getSignature());

    }

    @After(value = "execution(* cm.travelpost.tp.user.ent.service.UserService.*(..)) && args(userId)")
    public void afterAdvice(JoinPoint joinPoint, Long userId) {
        logger.info("After method:" + joinPoint.getSignature());

    }

}
