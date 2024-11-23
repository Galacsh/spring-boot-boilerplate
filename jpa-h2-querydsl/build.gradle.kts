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

// Make the annotation processors available during compilation
// without including them in the final build artifact
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("com.h2database:h2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    /* === QueryDSL (needs to be jakarta) === */
    val querydslVersion = dependencyManagement.importedProperties["querydsl.version"]
    implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
    /* === fixes Querydsl "NoClassDefFoundError: jakarta/persistence/Entity" === */
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Delete IntelliJ IDEA generated files
tasks.clean {
    doLast {
        file("src/main/generated").deleteRecursively()
        file("out").deleteRecursively()
    }
}
