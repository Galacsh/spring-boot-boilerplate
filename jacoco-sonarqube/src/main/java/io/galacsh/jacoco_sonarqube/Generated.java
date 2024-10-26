package io.galacsh.jacoco_sonarqube;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Mark things to skip coverage checks.
 * <p>
 * Since Jacoco performs analysis of bytecode and hence
 * this annotation must have retention policy
 * {@link RetentionPolicy#CLASS} or {@link RetentionPolicy#RUNTIME}.
 */
@Retention(RetentionPolicy.CLASS)
public @interface Generated {
}
