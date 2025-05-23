plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.asciidoctor)
}

group = "io.galacsh"
version = "1.0.0"

/** configure dependencies for asciidoctor */
val asciidoctorOnly: Configuration by configurations.creating

/** where to store Spring REST Docs snippets */
val asciidocSnippets = file("build/generated-snippets")

/** where the asciidoctor html will be generated */
val asciidoctorOutputDir = file("build/docs/asciidoc")

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
    implementation("org.springframework.boot:spring-boot-starter-web")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // generate asciidoc snippets during tests
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    // make asciidoc to resolve snippets generated by "spring-restdocs-mockmvc"
    asciidoctorOnly("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

tasks.withType<Test> {
    outputs.dir(asciidocSnippets)
    useJUnitPlatform()
}

tasks.asciidoctor {
    // "spring-restdocs-asciidoctor" is for here
    configurations(asciidoctorOnly.name)
    // generate snippets before asciidoctor
    dependsOn(tasks.test)
    // generate html with snippets
    inputs.dir(asciidocSnippets)
}

/**
 * Use this task to copy the generated asciidoc html to a specific location
 */
tasks.register("saveDocs") {
    dependsOn(tasks.asciidoctor)
    doLast {
        copy {
            from(asciidoctorOutputDir)
            into("docs") // ← Change if you want
        }
        println("Generated asciidoc html in src/docs/output")
    }
}

tasks.bootJar {
    dependsOn(tasks.asciidoctor)
    from(asciidoctorOutputDir) {
        // copy into the JAR/static/docs
        into("static/docs")
    }
}