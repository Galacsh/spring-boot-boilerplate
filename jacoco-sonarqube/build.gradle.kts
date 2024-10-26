import java.util.*

plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.sonarqube)
}

group = "io.galacsh"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/* =============================================================================================
Tests are required to run before generating the report

For more configuration options, see:
    https://docs.gradle.org/current/userguide/jacoco_plugin.html#sec:jacoco_report_configuration
============================================================================================= */
tasks.jacocoTestReport {
    reports {
        html.required = false
        xml.required = true
    }

    dependsOn(tasks.test)
}

/* =============================================================================================
For more configuration options, see:
    https://docs.gradle.org/current/userguide/jacoco_plugin.html#sec:jacoco_report_configuration
============================================================================================= */
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "CLASS"
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

/* ============================================================================
Easy way to generate the report and verify the coverage.
Runs "jacocoTestReport" and "jacocoTestCoverageVerification" tasks in sequence.
============================================================================ */
tasks.register("jacoco") {
    group = JavaBasePlugin.VERIFICATION_GROUP
    description = "Generate Jacoco report and verify the coverage"

    val report = tasks.findByName("jacocoTestReport")!!
    val verification = tasks.findByName("jacocoTestCoverageVerification")!!

    dependsOn(verification, report)
    verification.mustRunAfter(report)
}

/* ==========================================================
Jacoco code coverage report & verification tasks are required
to run build task.
========================================================== */
tasks.build {
    dependsOn(tasks.named("jacoco"))
}

/* =======================================
SonarQube configuration.
Reads .env file for the secret properties.
======================================= */
sonar {
    properties {
        // get sonar token, host.url from .env file
        val envFile = project.file(".env")
        val props = Properties().apply {
            if (envFile.exists()) {
                envFile.inputStream().use { load(it) }
            } else {
                throw IllegalStateException("${envFile.absolutePath} file not found.")
            }
        }

        // and assign them to the sonar properties
        props.forEach { propKey, propValue ->
            val key = (propKey as String).trim('"', '\'')
            val value = (propValue as String).trim('"', '\'')
            property(key, value)
        }

        /* ==================================================================================
        This message:
            Resource not found: ... while reading test reports.
            Please, make sure your "sonar.junit.reportPaths" property is configured properly.
        currently occurs whether the property is properly set or not.
        ================================================================================== */
    }
}

/* ====================================
SonarQube task runs after Jacoco tasks.
==================================== */
tasks.sonar {
    dependsOn(tasks.named("jacoco"))
}