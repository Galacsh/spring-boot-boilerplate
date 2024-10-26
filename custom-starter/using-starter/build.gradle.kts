plugins {
    java
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

extra["springBootVersion"] = "2.5.4"

dependencies {
    // Since sample-spring-boot-starter contains spring-boot-starter as dependency(api),
    // it will work without adding spring-boot-starter dependency here.
    //     implementation("org.springframework.boot:spring-boot-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // custom starter dependency from "subproject"
    implementation(project(":custom-starter:sample-spring-boot-starter"))

    /* ======================================================================
    if you want to use custom starter dependency from local maven repository,
    use the following code:
        implementation("io.galacsh:sample-spring-boot-starter:1.0.0")
    ====================================================================== */
}

tasks.withType<Test> {
    useJUnitPlatform()
}
