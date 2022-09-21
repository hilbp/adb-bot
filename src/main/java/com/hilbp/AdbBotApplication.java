package com.hilbp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AdbBotApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(AdbBotApplication.class, args);
	}
	
	
}
