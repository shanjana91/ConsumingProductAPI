package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.UserRepository.UserRepository;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
@EnableJpaRepositories(basePackageClasses = UserRepository.class)

public class ConsumingProductApiApplication extends SpringBootServletInitializer implements CommandLineRunner {
	private static Logger log = LoggerFactory.getLogger(ConsumingProductApiApplication.class);
	
	//JAR
	public static void main(String[] args) {

		SpringApplication.run(ConsumingProductApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		log.info("Application started...");
	}
	
	//WAR
	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	        return builder.sources(ConsumingProductApiApplication.class);
	    }

}
