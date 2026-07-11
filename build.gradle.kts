plugins {
    java
    `java-library`
    `maven-publish`
}

group = "dev.starless"
version = "1.3.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.StarlessDev"
            artifactId = "ConfigurableInventories"

            from(components["java"])
        }
    }
}

dependencies {
    compileOnly(libs.paper)
    api(libs.configurate)
    api(libs.adventure.serializers) {
        exclude(group = "net.kyori", module = "adventure-api")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withJavadocJar()
    withSourcesJar()
}