plugins {
    java
    `java-library`
    `maven-publish`

    alias(libs.plugins.lombok)
}

group = "dev.starless"
version = "1.25.7"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.StarlessDev"
            artifactId = "inventories-configurate"

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