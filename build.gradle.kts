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
    repositories {
        maven {
            name = "mineclub"
            url = uri("https://repo.starless.dev/releases")
            credentials(PasswordCredentials::class)
        }
    }

    publications {
        create<MavenPublication>("maven") {
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