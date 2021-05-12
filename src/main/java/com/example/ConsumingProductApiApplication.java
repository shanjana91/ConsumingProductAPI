package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import com.example.UserRepository.UserRepository;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import org.slf4j.*;


@SpringBootApplication
@EnableEncryptableProperties
@EnableJpaRepositories(basePackageClasses = UserRepository.class)

public class ConsumingProductApiApplication implements CommandLineRunner{
	private static Logger log=LoggerFactory.getLogger(ConsumingProductApiApplication.class);
	public static void main(String[] args) {
		
		
		SpringApplication.run(ConsumingProductApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		log.info("Application started...");
	}
	
}
