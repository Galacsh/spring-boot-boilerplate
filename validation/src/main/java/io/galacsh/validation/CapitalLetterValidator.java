package io.galacsh.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * An example of a custom constraint validator.
 * <p>
 * Automatically registered to the Spring context.
 * <p>
 * If annotation is used 3 times, 3 instances of this class will be created.
 *
 * @see CapitalLetter
 * @see org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration ValidationAutoConfiguration
 * @see org.springframework.validation.beanvalidation.LocalValidatorFactoryBean LocalValidatorFactoryBean
 */
public class CapitalLetterValidator implements ConstraintValidator<CapitalLetter, String> {

    @Override
    public void initialize(CapitalLetter constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        // check how many instances are created
        System.out.printf("Field name: %s (%s)%n", constraintAnnotation.name(), this.hashCode());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // check who is calling this method
        System.out.printf("Validating: %s (%s)%n", value, this.hashCode());

        return value != null
                && !value.isEmpty()
                && Character.isUpperCase(value.charAt(0));
    }
}
