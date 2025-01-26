plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"

}

group = "rip.snicon.compass"
version = "0.1.1"
description = "The SniconMC lobby server"

repositories {
    mavenCentral()
    maven{
        url = uri("https://jitpack.io")
        name = "buf"
        url = uri("https://buf.build/gen/maven")
    }
}

dependencies {
    implementation("net.minestom:minestom-snapshots:65f75bb059") // Minestom
    implementation("net.kyori:adventure-text-minimessage:4.17.0") // MiniMessage
    implementation("net.kyori:adventure-text-serializer-gson:4.17.0")

    implementation("org.slf4j:slf4j-api:2.0.15") // SLF4J API
    implementation("ch.qos.logback:logback-classic:1.5.7") // Logback classic
    implementation("ch.qos.logback:logback-core:1.5.7") // Logback core

    implementation("org.mongodb:mongodb-driver-sync:4.10.0") // Database mongoDB
    implementation("redis.clients:jedis:5.0.0") // Cache Database redis
    implementation("com.google.guava:guava:32.1.2-jre") // Byte stuff

    // Check latest version at https://buf.build/minekube/gate/sdks
    implementation("build.buf.gen:minekube_gate_protocolbuffers_java:29.2.0.1.20241120101512.f1a10b5029ce")
    implementation("build.buf.gen:minekube_gate_grpc_java:1.69.0.1.20241120101512.f1a10b5029ce")
    implementation("io.grpc:grpc-netty:1.69.0") // For gRPC transport


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
