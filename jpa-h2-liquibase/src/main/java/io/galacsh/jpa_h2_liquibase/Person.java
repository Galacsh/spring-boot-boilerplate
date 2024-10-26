package io.galacsh.jpa_h2_liquibase;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "person")
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private int age;

    @Override
    public String toString() {
        return String.format("+ [%d] %s %s (%d)", id, firstName, lastName, age);
    }
}
