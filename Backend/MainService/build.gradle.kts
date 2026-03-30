plugins {
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.18.0"
    java
}

group = "org.siri_hate"
version = "1.0.0-SNAPSHOT"
description = "MainService"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val awsSdkVersion = "2.37.3"
val mapstructVersion = "1.5.5.Final"
val jjwtVersion = "0.11.5"
val logstashEncoderVersion = "8.1"
val logbackVersion = "1.5.20"
val lombokVersion = "1.18.42"

dependencies {
    implementation("software.amazon.awssdk:s3:${awsSdkVersion}")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.google.code.gson:gson:2.10.1")
    runtimeOnly("org.postgresql:postgresql")
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("ch.qos.logback:logback-access:$logbackVersion")
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.41")
    implementation("org.openapitools:jackson-databind-nullable:0.2.8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("${projectDir}/openapi.yaml")
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)
    apiPackage.set("org.siri_hate.main_service.api")
    modelPackage.set("org.siri_hate.main_service.dto")
    modelNameSuffix.set("DTO")
    importMappings.set(mapOf("Pageable" to "org.springframework.data.domain.Pageable"))
    configOptions.set(mapOf(
            "library" to "spring-boot", "interfaceOnly" to "true", "skipDefaultInterface" to "true",
            "useTags" to "true", "useBeanValidation" to "true", "useJakartaEe" to "true",
            "openApiNullable" to "false", "dateLibrary" to "java8", "useSpringController" to "true",
        )
    )
}

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated/src/main/java"))
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}

tasks.clean {
    delete(layout.buildDirectory.dir("generated"))
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>("bootBuildImage") {
    imageName.set("starthub/mainservice:${project.version}")
}