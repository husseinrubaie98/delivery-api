package com.husseinrubaie.delivery.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    // define a private field for the dependency
    private Coach myCoach; // This is a singleton: only 1 instance is created across the app

    @Autowired
    /*
        - Field Injection: Unlike contructor and setter injection, field injection is not recommended by
        the spring.io team.
        - It is a legacy way of injecting, and makes unit test harder.
        - No longer recommended.
     */
    private Coach fieldInjectedCoach;

    @Autowired
    // define a constructor for dependency injection
    // Constructor Injection: use for required dependencies
    public DemoController(@Qualifier("baseballCoach") Coach theCoach) {
        /*
            - Auto wiring will create an instance of a class that extends the "Coach" interface.
              This could be either CricketCoach, or BaseballCoach. But since we have both, then
              Spring will choose the "Primary" class (cricket). Having 2 "Primary" will cause the app to crash.
            - To override the "Primary" behavior, we use the "Qualifier" annotation.
            - The Qualifier should take the "camelCaseFormat" of the class name (BaseballCoach -> baseballCoach)
            - We can use @Component("specialName") then use @Qualifier("specialName"): customizable class name.
         */
        myCoach = theCoach;
    }

    @Autowired
    // Setter Injection: use for optional dependencies
    // Can use any name other than "setObject" since we're using the @AutoWired annotation
    // At runtime, an instance of "Coach" be injected created and injected into this method
    public void setterInjectedCoach(Coach theCoach) {
        myCoach = theCoach;
    }

    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return myCoach.getDailyWorkout() + fieldInjectedCoach.getDailyWorkout();
    }
}
