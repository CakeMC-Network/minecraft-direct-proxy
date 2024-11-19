import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.*

plugins {
    id("idea")

    id("java")
    id("java-library")

    id("maven-publish")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "net.cakemc.mc.proxy"
version = "0.0.0-develop"


val repoProperties = Properties()
val repoFile = file("/credentials.properties")
if (repoFile.exists())
    repoProperties.load(repoFile.inputStream())
val repoUsername: String = (repoProperties["username"] ?: System.getenv("REPOSITORY_USERNAME")).toString()
val repoPassword: String = (repoProperties["password"] ?: System.getenv("REPOSITORY_PASSWORD")).toString()

repositories {
    mavenCentral()

    maven {
        name = "cakemc-nexus"
        url = URI.create("http://cakemc.net:8081/releases")
        credentials {
            username = repoUsername
            password = repoPassword
        }
        isAllowInsecureProtocol = true
    }
}


dependencies {
    implementation("io.netty:netty-all:4.1.112.Final")

    implementation(
        group = "net.cakemc.mc.server",
        name = "module-lib",
        version = "0.0.0-develop",
        classifier = "all"
    )
    implementation(
        group = "net.cakemc.mc.server",
        name = "module-protocol",
        version = "0.0.0-develop",
        classifier = "all"
    )

    implementation(
        group = "net.cakemc.util",
        name = "screen-system",
        version = "0.0.0-develop",
        classifier = "all"
    )
}

val jdkVersion = JavaVersion.VERSION_21
val jdkVersionString = jdkVersion.toString()

java {
    toolchain.languageVersion = JavaLanguageVersion.of(jdkVersionString)
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = jdkVersionString
    targetCompatibility = jdkVersionString
    options.encoding = StandardCharsets.UTF_8.toString()
}

tasks.withType<AbstractArchiveTask> {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}

tasks.withType<ShadowJar> {
    configurations = listOf(project.configurations.shadow.get())
    isZip64 = true
}

configurations.shadow { isTransitive = false }


publishing {
    publications.create<MavenPublication>(rootProject.name) {
        artifact(tasks.shadowJar)
    }
    repositories {
        maven {
            name = "cakemc"
            url = URI.create("http://cakemc.net:8081/releases")
            credentials {
                username = repoUsername
                password = repoPassword
            }
            isAllowInsecureProtocol = true
        }
    }
}
