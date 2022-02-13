/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.administrator.api;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
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
public class SwaggerApiConfig {

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

    @Value("${swagger.api.groupname.communication}")
    private String apiGroupNameCommunication;

    @Value("${swagger.api.groupname.websocket}")
    private String apiGroupNameWebsocket;

    @Value("${swagger.api.groupname.notification}")
    private String apiGroupNameNotification;

   @Bean
    public Docket announcesApi() {
        return createDocket(apiGroupNameAnnounce, "/pmanager/ws/announce.*");
    }

    @Bean
    public Docket mailApi() {
        return createDocket(apiGroupNameMail, "/pmanager/ws/mail.*");
    }

    @Bean
    public Docket reviewApi() {
        return createDocket(apiGroupNameReview, "/pmanager/ws/review.*");
    }

    @Bean
    public Docket userApi() {
        return createDocket(apiGroupNameUser, "/pmanager/ws/user.*");
    }


    @Bean
    public Docket messageApi() {
        return createDocket(apiGroupNameMessage, "/pmanager/ws/message.*");
    }


    @Bean
    public Docket roleApi() {
        return createDocket(apiGroupNameRole, "/pmanager/ws/role.*");
    }

    @Bean
    public Docket reservationApi() {
        return createDocket(apiGroupNameReservation, "/pmanager/ws/reservation.*");
    }

    @Bean
    public Docket imageApi() {
        return createDocket(apiGroupNameImage, "/pmanager/ws/image.*");
    }

    @Bean
    public Docket websocketApi() {
        return createDocket(apiGroupNameWebsocket, "/pmanager/ws/socket/notification.*");
    }

    @Bean
    public Docket notificationApi() {
        return createDocket(apiGroupNameNotification, "/pmanager/ws/notification.*");
    }

     @Bean
    public Docket dashBoardCommunicationApi() {
        return createDocket(apiGroupNameCommunication, "/pmanager/ws/dashboard/communication.*");
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

        return new ApiKey("APIKey", "AUTH_API_KEY", SecurityScheme.In.HEADER.name());
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                //.forPaths(regex("/pmanager/anyPath.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("APIKey", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Dimitri S. / Ludovic N.", "", "packagemanager@gmail.com");

        return new ApiInfoBuilder().title("Tr@vel Post REST API")
                .description("Tr@vel Post REST API reference for developers")
                .termsOfServiceUrl("")
                .contact(contact).license("Tr@vel Post License")
                .licenseUrl("").version("1.0").build();
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
