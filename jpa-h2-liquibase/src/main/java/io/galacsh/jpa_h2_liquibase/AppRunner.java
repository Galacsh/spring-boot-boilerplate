package io.galacsh.jpa_h2_liquibase;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final PersonRepository repository;

    @Override
    public void run(ApplicationArguments args) {
        repository.findAll().forEach(System.out::println);
    }
}
