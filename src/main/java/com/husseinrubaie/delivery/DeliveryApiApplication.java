package com.husseinrubaie.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeliveryApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApiApplication.class, args);
		String weather = "Beirut: Patchy light drizzle, 16°C, winds at 14 km/h";
		String batrounWeather = "Batroun: Patchy rain nearby, 15°C, winds at 25 km/h";
		System.out.print(weather);
	}
}