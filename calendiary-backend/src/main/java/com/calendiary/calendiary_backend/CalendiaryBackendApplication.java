package com.calendiary.calendiary_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CalendiaryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendiaryBackendApplication.class, args);
	}

}
