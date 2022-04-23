package cm.travelpost.tp.administrator.api;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static springfox.documentation.builders.PathSelectors.regex;


/* https://springfox.github.io/springfox/docs/current/#springfox-spring-mvc-and-spring-boot*/
/*@Configuration
@EnableSwagger2*/
public class SwaggerConfig {


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

/*	@Bean
	public Docket servicesRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build().securitySchemes(singletonList(apiKey()))
				.securityContexts(singletonList(securityContext()))
				.enableUrlTemplating(true);
	}*/


    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName(apiGroupNameAnnounce)
                .apiInfo(metaData()).select().paths(PathSelectors.any()).build();
    }

   // @Bean
    public Docket announcesApi() {
        return createDocket(apiGroupNameAnnounce, "/services/ws/announce.*");
    }

    // @Bean
    public Docket mailApi() {
        return createDocket(apiGroupNameMail, "/services/ws/mail.*");
    }

    // @Bean
    public Docket reviewApi() {
        return createDocket(apiGroupNameReview, "/services/ws/review.*");
    }

    // @Bean
    public Docket userApi() {
        return createDocket(apiGroupNameUser, "/services/ws/user.*");
    }


    // @Bean
    public Docket messageApi() {
        return createDocket(apiGroupNameMessage, "/services/ws/message.*");
    }


    // @Bean
    public Docket roleApi() {
        return createDocket(apiGroupNameRole, "/services/ws/role.*");
    }

    // @Bean
    public Docket reservationApi() {
        return createDocket(apiGroupNameReservation, "/services/ws/reservation.*");
    }

    // @Bean
    public Docket imageApi() {
        return createDocket(apiGroupNameImage, "/services/ws/image.*");
    }

    //  @Bean
    public Docket websocketApi() {
        return createDocket(apiGroupNameWebsocket, "/services/ws/socket/notification.*");
    }

    // @Bean
    public Docket notificationApi() {
        return createDocket(apiGroupNameNotification, "/services/ws/notification.*");
    }

    // @Bean
    public Docket dashBoardCommunicationApi() {
        return createDocket(apiGroupNameCommunication, "/services/ws/dashboard/communication.*");
    }


    private ApiInfo metaData() {
        Contact contact = new Contact("Dimitri S. / Ludovic N.", "", "travelpostservices@gmail.com");

        return new ApiInfoBuilder().title("Tr@vel Post REST API")
                .description("Tr@vel Post REST API reference for developers")
                .termsOfServiceUrl("")
                .contact(contact).license("Tr@vel Post License")
                .licenseUrl("").version("1.0").build();
    }

    private ApiKey apiKey() {
        return new ApiKey("API", "AUTH_API_KEY", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(regex("/services/anyPath.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(
                new SecurityReference("mykey", authorizationScopes));
    }

    @Bean
    SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId("test-app-client-id")
                .clientSecret("test-app-client-secret")
                .realm("test-app-realm")
                .appName("test-app")
                .scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .enableCsrfSupport(false)
                .build();
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

    Docket createDocket(String groupname, String paths) {
        return new Docket(DocumentationType.SWAGGER_2)
                 .forCodeGeneration(true)
                //.globalRequestParameters(globalParameterList() )
                //.globalOperationParameters(globalParameterList())
                .groupName(groupname)
                .select()
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(regex(paths))
                .build()
                .securitySchemes(singletonList(apiKey()))
                .securityContexts(singletonList(securityContext()))
                .apiInfo(metaData());
    }

    private List<Parameter> globalParameterList() {
        return Collections.singletonList( new ParameterBuilder()
                .name("AUTH_API_KEY") // name of the header
                .modelRef(new ModelRef("string")) // data-type of the header
                .required(true) // required/optional
                .parameterType("header") // for query-param, this value can be 'query'
                .description("Basic Auth Token")
                .build());
    }



}