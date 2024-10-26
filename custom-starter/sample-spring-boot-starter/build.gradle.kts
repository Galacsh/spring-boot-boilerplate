plugins {
    id("java-library")
    id("maven-publish")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
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
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Using "api" instead of "implementation" to ensure the project can run correctly
    // even if the project using this custom starter does not explicitly include
    // "spring-boot-starter" in its build.gradle.kts dependencies.
    api("org.springframework.boot:spring-boot-starter")

    // @ConfigurationProperties → META-INF/spring-configuration-metadata.json
    // For auto completion & documentation for custom properties
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Process annotations such as @ConditionalOnBean, @EnableAutoConfiguration, ...
    // and generates META-INF/spring-autoconfigure-metadata.properties.
    // Something like:
    //      org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration=
    //      org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration$DataSourceInitializerConfiguration=
    //      org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration$DataSourceInitializerConfiguration.ConditionalOnBean=javax.sql.DataSource
    //      org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration$DataSourceInitializerConfiguration.ConditionalOnClass=org.springframework.jdbc.datasource.init.DatabasePopulator
    //      org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration.AutoConfigureAfter=org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
    //      org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration.ConditionalOnBean=org.springframework.batch.core.launch.JobLauncher
    //      org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration.ConditionalOnClass=javax.sql.DataSource,org.springframework.batch.core.launch.JobLauncher
    // So that Spring can optimize the autoconfiguration process.
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("starter") {
            from(components["java"])

            // Add the resolved version information to the POM
            versionMapping {
                usage("java-api") {
                    fromResolutionResult()
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name = "Conversational name. Not the artifactId"
                description = "Spring Boot Starter boilerplate"

                // No need to specify groupId and artifactId since it will be inferred.

                developers {
                    developer {
                        id = "galacsh"
                        name = "Galacsh"
                        email = "galacsh.dev@gmail.com"
                    }
                }

                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    }
                }
            }

        }
    }

    // where to publish
    repositories {
        mavenLocal()
    }
}