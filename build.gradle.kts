plugins {
    java
    `java-library`
    `maven-publish`

    alias(libs.plugins.lombok)
}

group = "dev.starless"
version = "1.21.7"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
}

publishing {
    repositories {
        maven {
            name = "mineclub"
            url = uri("https://repo.starless.dev/releases")
            credentials(PasswordCredentials::class)
        }
    }

    publications {
        create<MavenPublication>("maven") {
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