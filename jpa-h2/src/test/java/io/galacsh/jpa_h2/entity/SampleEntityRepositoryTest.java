package io.galacsh.jpa_h2.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class SampleEntityRepositoryTest {

    @Autowired
    SampleEntityRepository repository;

    @Test
    void when_save_post_then_can_get_id() {
        var createdAt = Instant.now();
        var sample = SampleEntity.builder()
                .title("Hello")
                .summary("World")
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .build();

        var saved = repository.save(sample);
        assertNotNull(saved.getId());
    }
}