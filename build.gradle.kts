plugins {
    java
    `java-library`
    `maven-publish`
}

group = "dev.starless"
version = "2.0.1"

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
    compileOnly(libs.configurate)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withJavadocJar()
    withSourcesJar()
}