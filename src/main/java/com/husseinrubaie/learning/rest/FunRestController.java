package com.husseinrubaie.learning.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {

    @Value("${info.author.firstname}")
    public String firstname;

    @Value("${info.author.lastname}")
    public String lastname;

    @GetMapping("/")
    public String sayHello() {
        return "<h1>Hello World!</h1><br>From <b> " + firstname + " " + lastname + "</b>";

    }

    @GetMapping("/workout")
    public String getDailyWorkout() {
        return "<h1>Run a hard 15k!!</h1>";
    }
}
