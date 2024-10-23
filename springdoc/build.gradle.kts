plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)

    // generate OpenAPI specification
    alias(libs.plugins.springdoc.to.openapi)
    // generate SDK from OpenAPI specification
    alias(libs.plugins.openapi.to.sdk)
}

group = "io.galacsh"
version = "1.0.1"

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
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // enable SpringDoc only in development and test environment
    testAndDevelopmentOnly(libs.springdoc.webmvc.ui)
    // enable Swagger annotations
    implementation(libs.swagger.annotations)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/*
==============================================================================
SpringDoc's "openapi-gradle-plugin" configuration.
This plugin generates OpenAPI specification.

Run:
    ./gradlew :{project-name}:generateOpenApiDocs

See more options:
    https://github.com/springdoc/springdoc-openapi-gradle-plugin#customization
==============================================================================
 */
openApi {
    outputDir.set(file("$projectDir/docs"))
    customBootRun {
        args = listOf("--logging.level.root=ERROR")
    }
}

/*
==============================================================================
OpenAPI Generator's plugin configuration.
This plugin generates client SDK(Typescript fetch) from OpenAPI specification.

Run:
    ./gradlew :{project-name}:openApiGenerate

See more options:
    https://openapi-generator.tech/docs/generators/typescript-fetch
==============================================================================
 */
openApiGenerate {
    generatorName.set("typescript-fetch")
    inputSpec.set("$projectDir/docs/openapi.json")
    outputDir.set("${projectDir}/client-sdk")
    configOptions.set(
        mapOf(
            "npmName" to project.name,
            "npmVersion" to project.version.toString(),
            "supportsES6" to "true",
            "useSingleRequestParameter" to "true",
            "validationAttributes" to "true",
        )
    )
}


/*
===============================================================================
Make sure to generate client SDK after OpenAPI specification is generated.

OpenAPI specification file is generated after the task "forkedSpringBootStop",
which is executed after the task "generateOpenApiDocs".

So we need to:
    1. Trigger "generateOpenApiDocs" first.
    2. Wait for "forkedSpringBootStop" to finish.
===============================================================================
 */
tasks.openApiGenerate {
    dependsOn(tasks.generateOpenApiDocs)
    shouldRunAfter(tasks.forkedSpringBootStop)

    doLast {
        val note = """

            NOTE:
                Make sure to edit `.openapi-generator-ignore` file
                to prevent files from being overwritten by the generator.
 
        """.trimIndent().prependIndent("    ")
        println(note)
    }
}

// when building the jar, generate OpenAPI specification first
tasks.bootJar {
    dependsOn(tasks.openApiGenerate)
}