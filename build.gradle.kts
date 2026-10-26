plugins {
    kotlin("jvm") version "2.4.0"
    application
}

repositories { mavenCentral() }

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) }
}

application { mainClass.set("MainKt") }

tasks.withType<JavaExec>().configureEach {
    standardInput = System.`in`
    jvmArgs("-Dfile.encoding=UTF-8")
}
