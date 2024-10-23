package io.galacsh.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

/**
 * An example of a custom constraint annotation.
 * <p>
 * To make this annotation work, you'll need to:
 * <ul>
 *     <li>
 *         Connect this annotation with a validator using {@link jakarta.validation.Constraint @Constraint}.
 *     <li>
 *         Annotate the target element.
 *     </li>
 *     <li>
 *         Don't forget the {@link org.springframework.validation.annotation.Validated @Validated} annotation.
 *     </li>
 * </ul>
 * Also, you have to specify these:
 * <ul>
 *     <li>
 *         message - see {@link #message()}
 *     </li>
 *     <li>
 *         groups - see {@link #groups()}
 *     </li>
 *     <li>
 *         payload - see {@link #payload()}
 *     </li>
 * </ul>
 *
 * @see CapitalLetterValidator
 * @see org.springframework.validation.annotation.Validated @Validated
 * @see jakarta.validation.Constraint @Constraint
 * @see jakarta.validation.ConstraintValidator ConstraintValidator
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CapitalLetterValidator.class)
public @interface CapitalLetter {

    /**
     * Field name which will be shown in error message.
     */
    String name();

    /**
     * Message template.
     * <p>
     * The message template will be interpolated automatically by
     * {@link org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator ParameterMessageInterpolator}.
     * <p>
     * Note that Hibernate provides this interpolator,
     * but it is not Jakarta Validation specification compliant.
     * <p>
     * ParameterMessageInterpolator:
     * <pre><code>
     * // when name = "Username"
     * "{name} must ..." → "Username must ..."
     * </code></pre>
     *
     * @see org.springframework.boot.validation.MessageInterpolatorFactory MessageInterpolatorFactory
     * @see org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator ParameterMessageInterpolator
     */
    String message() default "{name} must start with a capital letter";

    /**
     * Groups are used to validate only a subset of constraints.
     * <p>
     * Before using groups, you'll need to know:
     * <ul>
     *     <li>
     *         There is a {@link jakarta.validation.groups.Default Default} group.
     *     </li>
     *     <li>
     *         If you don't specify any group when annotating an element,
     *         it means that your annotation is in the {@link jakarta.validation.groups.Default Default} group.
     *     </li>
     *     <li>
     *         You cannot set groups in annotation definition due to
     *         {@link jakarta.validation.ConstraintDefinitionException ConstraintDefinitionException}.
     *     </li>
     * </ul>
     * <p>
     * Usage example:
     * <pre>
     * {@code @Validated({  Adult.class, Default.class }) User user;}
     * </pre>
     * <p>
     * This will make validations run on elements having annotations like:
     * <pre><code>
     *     {@code @NotBlank} String name;
     *     {@code @Min(value = 18, groups = { Adult.class })} int age;
     * </code></pre>
     * But not on elements having annotations like:
     * <pre><code>
     *     {@code @NotBlank(groups = { Guest.class })} String guestUid;
     * </code></pre>
     * <p>
     * If you think {@code @Validated({ Adult.class, Default.class })} is too verbose,
     * you can make {@code Adult} extend {@link jakarta.validation.groups.Default Default} group.
     *
     * @see Adult Adult Group (example)
     * @see jakarta.validation.groups.Default Default Group
     * @see org.springframework.validation.annotation.Validated @Validated
     * @return what groups to validate
     */
    Class<?>[] groups() default {};

    /**
     * Payload is additional information that can be attached to a constraint violation.
     * <p>
     * For example, you can specify the severity of the error.
     * {@code payload = { Severity.Error.class }} or
     * {@code payload = { Severity.Warning.class }}
     *
     * @return additional information
     */
    Class<?>[] payload() default {};
}
