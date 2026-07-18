package com.husseinrubaie.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Scans only com.husseinrubaie.delivery (the platform). The training/reference code
// in com.husseinrubaie.learning is left untouched but kept out of the default scan;
// it is activated on demand by the "learning" profile (see config.LearningModuleConfig),
// which avoids clashes such as two controllers mapping GET "/".
// Using @SpringBootApplication is equivalent to using the 3 below:
//@EnableAutoConfiguration: Enables auto config support
//@ComponentScan: Recursively scans package for components (anything outside 'this' package will not be scanned)
//@Configuration: able to register extra beans using @Bean or import other configuration classes
public class DeliveryApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApiApplication.class, args);
	}

}
