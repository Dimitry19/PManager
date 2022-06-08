package cm.travelpost.tp.configuration.cfg;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class CustomWebMvcConfigurerAdapter implements WebMvcConfigurer {


    /**
     * Redirige les routes notFound et error sur la page index.html
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/notFound").setViewName("forward:/notFound.html");
        registry.addViewController("/error").setViewName("forward:/index.html");
    }

    /**
     * Ici on configure le container de tomcat afin qu'il redirige les erreurs not_found
     * et bad request vers les routes notFound et error
     *
     * @return
     */

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> {
            //container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notFound"));
            container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error"));
        };
    }
}
