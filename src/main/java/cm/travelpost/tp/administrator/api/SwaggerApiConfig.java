/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.administrator.api;

import cm.travelpost.tp.common.properties.CommonProperties;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@Configuration
public class SwaggerApiConfig  extends CommonProperties {

    @Value("${swagger.api.groupname.authentication}")
    private String apiGroupNameAuthentication;

    @Value("${swagger.api.groupname.announce}")
    private String apiGroupNameAnnounce;

    @Value("${swagger.api.groupname.review}")
    private String apiGroupNameReview;

    @Value("${swagger.api.groupname.user}")
    private String apiGroupNameUser;

    @Value("${swagger.api.groupname.message}")
    private String apiGroupNameMessage;

    @Value("${swagger.api.groupname.role}")
    private String apiGroupNameRole;

    @Value("${swagger.api.groupname.mail}")
    private String apiGroupNameMail;

    @Value("${swagger.api.groupname.reservation}")
    private String apiGroupNameReservation;

    @Value("${swagger.api.groupname.image}")
    private String apiGroupNameImage;

    @Value("${swagger.api.groupname.dashboard}")
    private String apiGroupNameDashboard;

    @Value("${swagger.api.groupname.websocket}")
    private String apiGroupNameWebsocket;

    @Value("${swagger.api.groupname.notification}")
    private String apiGroupNameNotification;

    @Value("${swagger.api.groupname.city}")
    private String apiGroupNameCity;

    @Value("${swagger.api.groupname.sms}")
    private String apiGroupNameSms;

    @Value("${swagger.api.groupname.totp}")
    private String apiGroupNameTotp;


    @Value("${swagger.api.contact}")
    private String apiContact;

    @Value("${swagger.api.info.title}")
    private String apiInfoTitle;

    @Value("${swagger.api.info.description}")
    private String apiInfoDescription;

    @Value("${swagger.api.info.licence}")
    private String apiInfoLicence;

    @Value("${swagger.api.info.version}")
    private String apiInfoVersion;


    @Value("${custom.api.auth.http.tokenName}")
    private String apiKeyPropertie;



   @Bean
    public Docket authenticationApi() {
        return createDocket(apiGroupNameAuthentication, contextRoot+"/ws/authentication.*");
    }
    @Bean
    public Docket announcesApi() {
        return createDocket(apiGroupNameAnnounce, contextRoot+"/ws/announce.*");
    }

    @Bean
    public Docket mailApi() {
        return createDocket(apiGroupNameMail, contextRoot+"/ws/mail.*");
    }

    @Bean
    public Docket reviewApi() {
        return createDocket(apiGroupNameReview, contextRoot+"/ws/review.*");
    }

    @Bean
    public Docket userApi() {
        return createDocket(apiGroupNameUser, contextRoot+"/ws/user.*");
    }


    @Bean
    public Docket messageApi() {
        return createDocket(apiGroupNameMessage, contextRoot+"/ws/message.*");
    }


    @Bean
    public Docket roleApi() {
        return createDocket(apiGroupNameRole, contextRoot+"/ws/role.*");
    }

    @Bean
    public Docket reservationApi() {
        return createDocket(apiGroupNameReservation, contextRoot+"/ws/reservation.*");
    }

    @Bean
    public Docket imageApi() {
        return createDocket(apiGroupNameImage, contextRoot+"/ws/image.*");
    }

    @Bean
    public Docket websocketApi() {
        return createDocket(apiGroupNameWebsocket, contextRoot+"/ws/socket/notification.*");
    }

    @Bean
    public Docket notificationApi() {
        return createDocket(apiGroupNameNotification, contextRoot+"/ws/notification.*");
    }

     @Bean
    public Docket dashBoardApi() {
        return createDocket(apiGroupNameDashboard, contextRoot+"/ws/dashboard.*");
    }

    @Bean
    public Docket cityApi() {
        return createDocket(apiGroupNameCity, contextRoot+"/ws/city.*");
    }

    @Bean
    public Docket otpApi() {
        return createDocket(apiGroupNameSms, contextRoot+"/ws/sms.*");
    }

    @Bean
    public Docket totpApi() {
        return createDocket(apiGroupNameTotp, contextRoot+"/ws/totp.*");
    }


    public Docket createDocket(String groupName,String paths) {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .forCodeGeneration(true)
                .groupName(groupName)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiToken()))
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(regex(paths))
                //.paths(PathSelectors.any())
                .build();
    }


    private ApiKey apiToken() {

        return new ApiKey("APIKey", apiKeyPropertie, SecurityScheme.In.HEADER.name());
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                //.forPaths(regex("/services/anyPath.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("APIKey", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(apiContact, travelPostDomain, travelPostEmail);

        return new ApiInfoBuilder().title(apiInfoTitle)
                .description(apiInfoDescription)
                .termsOfServiceUrl("")
                .contact(contact).license(apiInfoLicence)
                .licenseUrl("").version(apiInfoVersion).build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.MODEL)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .showCommonExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }
}
