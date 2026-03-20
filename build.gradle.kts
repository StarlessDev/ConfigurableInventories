plugins {
    java
    `java-library`
    `maven-publish`

    alias(libs.plugins.lombok)
}

group = "dev.starless"
version = "1.21.10"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.StarlessDev"
            artifactId = "inventories"

            from(components["java"])
        }
    }
}

dependencies {
    compileOnly(libs.paper)

    // Configuration library
    api(libs.configme)
    // Inventory UI library
    api(libs.invui)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withJavadocJar()
    withSourcesJar()
}