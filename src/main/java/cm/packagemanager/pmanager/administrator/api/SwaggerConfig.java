package cm.packagemanager.pmanager.administrator.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Predicate;

import static com.google.common.base.Predicates.or;
import static java.util.Collections.singletonList;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.schema.AlternateTypeRules.newRule;



/* https://springfox.github.io/springfox/docs/current/#springfox-spring-mvc-and-spring-boot*/
@Configuration
@EnableSwagger2
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

/*	@Bean
	public Docket pmanagerRestApi() {
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

	@Bean
	public Docket announcesApi() {
		return	createDocket(apiGroupNameAnnounce,"/pmanager/ws/announce.*");
	}

	@Bean
	public Docket reviewApi() {
		return	createDocket(apiGroupNameReview,"/pmanager/ws/review.*");
	}

	@Bean
	public Docket userApi() {
		return	createDocket(apiGroupNameUser,"/pmanager/ws/user.*");
	}


	@Bean
	public Docket messageApi() {
		return	createDocket(apiGroupNameMessage,"/pmanager/ws/message.*");
	}


	@Bean
	public Docket roleApi() {
		return	createDocket(apiGroupNameRole,"/pmanager/ws/role.*");
	}

	private ApiInfo metaData() {
		Contact contact = new Contact("Dimitri S.", "", "packagemanager@gmail.com");

		return new ApiInfoBuilder().title("Package Manager REST API")
				.description("Package Manager REST API reference for developers")
				.termsOfServiceUrl("")
				.contact(contact).license("Package Manager License")
				.licenseUrl("").version("1.0").build();
	}

	/*@Bean
	public Docket petApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/ws/announce/")
				.directModelSubstitute(LocalDate.class, String.class)
				.genericModelSubstitutes(ResponseEntity.class)
				.alternateTypeRules(
						newRule(typeResolver.resolve(DeferredResult.class,
								typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
								typeResolver.resolve(WildcardType.class)))
				.useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET,
						singletonList(new ResponseBuilder()
								.code("500")
								.description("500 message")
								.representation(MediaType.TEXT_XML)
								.apply(r ->
										r.model(m ->
												m.referenceModel(ref ->
														ref.key(k ->
																k.qualifiedModelName(q ->
																		q.namespace("some:namespace")
																				.name("ERROR"))))))
								.build()))
				.securitySchemes(singletonList(apiKey()))
				.securityContexts(singletonList(securityContext()))
				.enableUrlTemplating(true)
				.globalRequestParameters(
						singletonList(new springfox.documentation.builders.RequestParameterBuilder()
								.name("someGlobalParameter")
								.description("Description of someGlobalParameter")
								.in(ParameterType.QUERY)
								.required(true)
								.query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
								.build()))
				.tags(new Tag("Announce Service", "All apis relating to announce"));
				//.additionalModels(typeResolver.resolve(AnnounceDTO.class));
	}

	@Autowired
	private TypeResolver typeResolver;
*/
	private ApiKey apiKey() {
		return new ApiKey("API", "PM2020", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(regex("/pmanager/anyPath.*"))
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

	Docket createDocket(String groupname, String paths){
		return new Docket(DocumentationType.SWAGGER_2).groupName(groupname)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(regex(paths))
				.build()
				.securitySchemes(singletonList(apiKey()))
				.securityContexts(singletonList(securityContext()))
				.apiInfo(metaData());
	}

}