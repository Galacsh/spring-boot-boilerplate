package io.galacsh.jpa_h2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Sample entity class for JPA.
 *
 * @see <a href="https://openjpa.apache.org/builds/1.2.3/apache-openjpa/docs/jpa_overview_pc.html">OpenJPA - Chapter 4. Entity</a>
 */

@Entity
@Table(name = "post")
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SampleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}