package cm.packagemanager.pmanager.configuration.cfg;


import cm.packagemanager.pmanager.PackageApplication;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//https://www.javainuse.com/spring/spring-boot-aop

@Aspect
@Component
public class CommonAspect {

	private static Logger logger = LoggerFactory.getLogger(CommonAspect.class);

		@Before(value = "execution(* cm.packagemanager.pmanager.user.service.UserService.*(..)) && args(userId)")
		public void beforeAdvice(JoinPoint joinPoint, Long  userId ) {
			logger.info("Before method:" + joinPoint.getSignature());

			logger.info("Creating Employee with   id - " + userId);
		}

		@After(value = "execution(* cm.packagemanager.pmanager.user.service.UserService.*(..)) && args(userId)")
		public void afterAdvice(JoinPoint joinPoint, Long  userId) {
			logger.info("After method:" + joinPoint.getSignature());

			logger.info("Successfully created Employee with  id - " + userId);
		}

}
