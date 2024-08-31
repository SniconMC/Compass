plugins {
    id("java")
}

group = "rip.snicon"
version = "0.1-dev"

repositories {
    mavenCentral()
    maven{
        url = uri("https://jitpack.io")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.minestom:minestom-snapshots:65f75bb059") // Minestom
    implementation("ch.qos.logback:logback-classic:1.5.7") // Logback
    implementation("net.kyori:adventure-text-minimessage:4.17.0") // MiniMessage
    implementation("com.github.SniconMC:Minestom-Utils:0.1.4")
    implementation("com.github.SniconMC:Minestom-Momentum:0.1.4")
}
