package io.galacsh.validation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record User(
        @NotBlank
        @CapitalLetter(name = "Name")
        String name,

        @Min(value = 18, message = "Only adult is allowed", groups = Adult.class)
        Integer age,

        @NotBlank
        @CapitalLetter(name = "Nickname")
        String nickname
) {
    @Override
    public String toString() {
        return String.format("%s (%d)", name, age);
    }
}
