import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
}

repositories {
    mavenCentral()
    maven("https://maven.dimensional.fun/releases")
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")
}

dependencies {
    /* kotlin */
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.1")

    /* audio */
    implementation("com.github.walkyst:lavaplayer-fork:original-SNAPSHOT")
    implementation("com.github.Topis-Lavalink-Plugins:Topis-Source-Managers:master-SNAPSHOT")

    /* koin */
    implementation("io.insert-koin:koin-core:3.1.6")

    /* config */
    implementation("com.sksamuel.hoplite:hoplite-core:2.1.5")
    implementation("com.sksamuel.hoplite:hoplite-toml:2.1.5")

    /* jda */
    implementation("net.dv8tion:JDA:5.0.0-alpha.12")
    implementation("com.github.minndevelopment:jda-ktx:master-SNAPSHOT")

    /* logging */
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
}

tasks.withType<KotlinCompile> {
    kotlinOptions { jvmTarget = "16" }
}
