package com.att.microservices.springframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableAutoConfiguration  // Sprint Boot Automatic Configuration
@ComponentScan(basePackages = "com.att.microservices")
@SpringCloudApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages =  {"com.att.microservices.springframework", "com.att.microservices.springframework.services"})
@EnableCircuitBreaker
public class SpringBootMongodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMongodbApplication.class, args);
	}
}
