package io.galacsh.actuator;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        System.out.println("""
                ================================================================================
                See difference when running the application with the "prod" profile.
                
                👍 Checkout this URL for more information about the actuator endpoints:
                    https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html
                ================================================================================
                """);
    }
}
