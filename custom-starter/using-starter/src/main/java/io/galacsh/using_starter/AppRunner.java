package io.galacsh.using_starter;

import io.galacsh.starter.Sample;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final Sample sample;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println(sample);
    }
}
