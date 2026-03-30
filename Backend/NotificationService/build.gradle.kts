plugins {
    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"
    java
}

group = "org.siri_hate"
version = "1.0.0-SNAPSHOT"
description = "NotificationService"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val gsonVersion = "2.10.1"
val ognlVersion = "3.3.4"
val logstashEncoderVersion = "8.1"
val logbackVersion = "1.5.18"
val mailStarterVersion = "3.1.5"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-mail:$mailStarterVersion")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("ognl:ognl:$ognlVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("ch.qos.logback:logback-access:$logbackVersion")
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>("bootBuildImage") {
    imageName.set("starthub/notificationservice:${project.version}")
}
