val backendServices = listOf(
    "ApiGateway",
    "UserService",
    "MainService",
    "NotificationService",
    "ChatService",
)

allprojects {
    group = "org.siri_hate"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

tasks.register("buildDockerImages") {
    group = "docker"
    description = "Build docker images for all backend services"
    dependsOn(backendServices.map { ":$it:bootBuildImage" })
}
