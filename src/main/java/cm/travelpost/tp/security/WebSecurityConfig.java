package cm.travelpost.tp.security;


import cm.travelpost.tp.configuration.filters.AuthenticationFilter;
import cm.travelpost.tp.configuration.filters.SessionFilter;
import cm.travelpost.tp.websocket.constants.WebSocketConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    private static final String[] AUTH_LIST = {
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources",
            "/configuration/security",
            "/swagger-ui.html",
            "/ws/**",
            "/webjars/**",
            WebSocketConstants.END_POINT
    };


    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http.csrf().disable();
        http.httpBasic().disable().requiresChannel()
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure();*/

       logger.info("into configure");
//        http.csrf().disable();
//        http.httpBasic().disable();

        http
        .authorizeRequests()
        .antMatchers(AUTH_LIST)
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .cors()
                .and()
                .headers()
                .frameOptions()
                //.sameOrigin()
                .disable()
                .and()
                .csrf()
                .disable()
                .requiresChannel()
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure();
                //.addFilterBefore(sessionFilter,UsernamePasswordAuthenticationFilter.class);



            /* http
                .cors()
                .and()
                .headers()
                .frameOptions().disable()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/stomp").permitAll() // On autorise l'appel handshake entre le client et le serveur
                .anyRequest()
                .authenticated();*/
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {


    }

    @Bean
    public FilterRegistrationBean  authenticationFilterBean() {
        FilterRegistrationBean  registrationBean = new FilterRegistrationBean();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();

        registrationBean.setFilter(authenticationFilter);
        registrationBean.addUrlPatterns("/user/*");
        registrationBean.setOrder(2); //set precedence
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean  sessionFilterBean() {
        FilterRegistrationBean  registrationBean = new FilterRegistrationBean();
        SessionFilter sessionFilter = new SessionFilter();

        registrationBean.setFilter(sessionFilter);
        registrationBean.addUrlPatterns("/user/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

}