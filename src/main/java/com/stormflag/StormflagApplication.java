package com.stormflag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StormflagApplication {

	public static void main(String[] args) {
		SpringApplication.run(StormflagApplication.class, args);
	}

}
