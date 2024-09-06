plugins {
    id("java")
}

group = "rip.snicon.compass"
version = "0.1"
description = "The SniconMC lobby server"

repositories {
    mavenCentral()
    maven{
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("net.minestom:minestom-snapshots:65f75bb059") // Minestom
    implementation("net.kyori:adventure-text-minimessage:4.17.0") // MiniMessage
    implementation("com.github.SniconMC:Utils:0.1.6.4")
    implementation("com.github.SniconMC:Momentum:0.1.7")
    implementation("com.github.SniconMC:Container:0.1.4")
    implementation("com.github.SniconMC:Sidebar:0.1.3")
    implementation("com.github.SniconMC:Oblivion:0.1.6")

    implementation("org.slf4j:slf4j-api:2.0.15") // SLF4J API
    implementation("ch.qos.logback:logback-classic:1.5.7") // Logback classic
    implementation("ch.qos.logback:logback-core:1.5.7") // Logback core

}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}
