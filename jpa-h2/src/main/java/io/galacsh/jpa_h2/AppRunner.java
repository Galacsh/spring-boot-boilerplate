package io.galacsh.jpa_h2;

import io.galacsh.jpa_h2.entity.SampleEntity;
import io.galacsh.jpa_h2.entity.SampleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final SampleEntityRepository sampleRepo;

    @Override
    public void run(ApplicationArguments args) {
        sampleRepo.save(SampleEntity.builder()
                .title("First")
                .summary("Hello first")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        sampleRepo.save(SampleEntity.builder()
                .title("Second")
                .summary("Hello second")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        sampleRepo.findAll().stream()
                .map(SampleEntity::getTitle)
                .forEach(title -> System.out.println("→ Title: " + title));
    }
}
