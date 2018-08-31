import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.2.61"
}

group = "org.darkjaguar"
version = "0.1"

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
    compile(group = "com.discord4j", name = "Discord4J", version = "2.10.1")
    compile(group = "com.sedmelluq", name = "lavaplayer", version = "1.3.7")
    compile("com.beust:jcommander:1.71")

    testCompile(kotlin("test"))
    testCompile("org.junit.jupiter:junit-jupiter-api:5.2.0")
    testCompile("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.2.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
