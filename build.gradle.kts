plugins {
    java
    `java-library`
    `maven-publish`

    alias(libs.plugins.lombok)
}

group = "dev.starless"
version = "1.22.3"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.StarlessDev"
            artifactId = "inventories-folia"

            from(components["java"])
        }
    }
}

dependencies {
    compileOnly(libs.paper)

    // Configuration library
    api(libs.configme)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withJavadocJar()
    withSourcesJar()
}