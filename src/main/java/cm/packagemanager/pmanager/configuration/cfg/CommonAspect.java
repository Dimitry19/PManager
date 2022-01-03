package cm.packagemanager.pmanager.configuration.cfg;


import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.service.UserService;
import cm.packagemanager.pmanager.ws.requests.users.LoginDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//https://www.javainuse.com/spring/spring-boot-aop

@Aspect
@Component
public class CommonAspect {

    @Autowired
    UserService userService;

    private static Logger logger = LoggerFactory.getLogger(CommonAspect.class);

    @Before(value = "execution(* cm.packagemanager.pmanager.user.ent.service.UserService.login(..)) && args(lr)")
    public void beforeAdvice(JoinPoint joinPoint, LoginDTO lr) throws Exception {

        if (!userService.checkLogin(lr)) {
            throw new UserException("Le compte n'est pas actif ou mot de passe non valide");
        }
        logger.info("Before method:" + joinPoint.getSignature());

    }

    @After(value = "execution(* cm.packagemanager.pmanager.user.ent.service.UserService.*(..)) && args(userId)")
    public void afterAdvice(JoinPoint joinPoint, Long userId) {
        logger.info("After method:" + joinPoint.getSignature());

    }

}
