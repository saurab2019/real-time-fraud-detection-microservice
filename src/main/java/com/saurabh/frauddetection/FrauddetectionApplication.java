package com.saurabh.frauddetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FrauddetectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrauddetectionApplication.class, args);
	}

}
