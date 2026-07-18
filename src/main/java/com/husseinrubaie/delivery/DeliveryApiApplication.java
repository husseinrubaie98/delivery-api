package com.husseinrubaie.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = {
				"com.husseinrubaie.delivery",
				"com.husseinrubaie.learning",
		}
)
// Using the above annotation is equivalent to using the 3 below:
//@EnableAutoConfiguration: Enables auto config support
//@ComponentScan: Recursively scans package for components (anything outside 'this' package will not be scanned)
//@Configuration: able to register extra beans using @Bean or import other configuration classes
public class DeliveryApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApiApplication.class, args);
	}

}
