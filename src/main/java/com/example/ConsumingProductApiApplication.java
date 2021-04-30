package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.slf4j.*;


@SpringBootApplication
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
