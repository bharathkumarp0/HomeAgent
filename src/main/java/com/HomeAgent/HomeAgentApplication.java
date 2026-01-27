package com.HomeAgent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.HomeAgent.repository")
public class HomeAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeAgentApplication.class, args);

		System.out.println("application started sucessfully");



	}

}
