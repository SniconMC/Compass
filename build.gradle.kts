plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "rip.snicon.compass"
version = "0.1"
description = "The SniconMC hub lobby server"

repositories {
    mavenCentral()
    maven{
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.mysql:mysql-connector-j:9.1.0")
    implementation("net.minestom:minestom-snapshots:65f75bb059") // Minestom
    implementation("net.kyori:adventure-text-minimessage:4.17.0") // MiniMessage
    implementation("com.github.SniconMC:Utils:0.1.7.1")
    implementation("com.github.SniconMC:Momentum:0.1.7")
    implementation("com.github.SniconMC:Sidebar:0.1.3")
    implementation("com.github.SniconMC:Container:0.1.6")
    implementation("com.github.SniconMC:Oblivion:0.1.6")
    implementation("com.github.SniconMC:Gandalf:0.1.2.8")

    implementation("org.slf4j:slf4j-api:2.0.15") // SLF4J API
    implementation("ch.qos.logback:logback-classic:1.5.7") // Logback classic
    implementation("ch.qos.logback:logback-core:1.5.7") // Logback core
    implementation("com.google.guava:guava:32.1.2-jre") // Byte stuff
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Minestom has a minimum Java version of 21
    }
}


tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    jar {
        manifest {
            attributes["Main-Class"] = "rip.snicon.compass.Main" // Change this to your main class
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("") // Prevent the -all suffix on the shadowjar file.
    }
}
