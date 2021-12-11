package cm.packagemanager.pmanager.security;


import cm.packagemanager.pmanager.configuration.filters.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);


    @Autowired
    private AuthenticationEntryPoint authEntryPoint;


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        logger.info("into configure");
        http.csrf().disable();
        http.httpBasic().disable();

        // All requests send to the Web Server request must be authenticated
        //http.antMatcher("/sv/**").authorizeRequests().anyRequest().authenticated();

        // Use AuthenticationEntryPoint to authenticate user/password
        //http.httpBasic().authenticationEntryPoint(authEntryPoint);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER");

        String password = "password2";

        String encrytedPassword = "$2a$10$PTJsddL7mIzmov0UE4lBNuSpWUQocANJbSnkRfOfrgKDF2SVrUB8W";//this.passwordEncoder().encode(password);
        //System.out.println("Encoded password of password2=" + encrytedPassword);


        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> //
                mngConfig = auth.inMemoryAuthentication();

        // Defines 2 users, stored in memory.
        // ** Spring BOOT >= 2.x (Spring Security 5.x)
        // Spring auto add ROLE_
        //UserDetails u1 = User.withUsername("demo").password(encrytedPassword).roles("USER").build();
        //UserDetails u2 = User.withUsername("demo1").password(encrytedPassword).roles("USER").build();

        //mngConfig.withUser(u1);
        //mngConfig.withUser(u2);

        // If Spring BOOT < 2.x (Spring Security 4.x)):
        // Spring auto add ROLE_
        // mngConfig.withUser("tom").password("123").roles("USER");
        // mngConfig.withUser("jerry").password("123").roles("USER");
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean();
        AuthenticationFilter customURLFilter = new AuthenticationFilter();

        registrationBean.setFilter(customURLFilter);
        registrationBean.addUrlPatterns("/user/*");
        registrationBean.setOrder(2); //set precedence
        return registrationBean;
    }
}