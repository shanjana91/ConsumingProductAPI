package com.example.SwaggerConfig;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public Docket config() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiinfo())
				.select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build()
				.securitySchemes(Arrays.asList(apiKey())) //defines the authentication method available for the api
				.securityContexts(Arrays.asList(securitycontext())); //defines to which contexts the scheme should be applied
	}
	
	@Bean
	public ApiInfo apiinfo() {
		return new ApiInfoBuilder().title("REST client for Product API")
				.description("Consuming the product API that provides details about products")
				.build();
	}
	
	//security scheme : include JWT as authorization header
	private ApiKey apiKey() {  // a token that client provides when making API calls
	    return new ApiKey("JWT", "Authorization", "header");  //(name,keyname,passAs)
	}
	
	//provide global authorization scope
	private SecurityContext securitycontext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}
	
	private List<SecurityReference> defaultAuth(){
		AuthorizationScope scope=new AuthorizationScope("global", "accessEverything"); //(scope,description)
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
	    authorizationScopes[0] = scope; 
	    return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); //(reference,scopes)
	}
}
