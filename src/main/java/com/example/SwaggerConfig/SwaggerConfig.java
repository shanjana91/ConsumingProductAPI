package com.example.SwaggerConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public Docket config() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiinfo())
				.select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}
	
	@Bean
	public ApiInfo apiinfo() {
		return new ApiInfoBuilder().title("REST client for Product API")
				.description("Consuming the product API that provides details about products")
				.build();
	}
}
