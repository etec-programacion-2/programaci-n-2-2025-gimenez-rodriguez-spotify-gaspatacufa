plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
//    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-client-core:${ktor_version}")
    implementation("io.ktor:ktor-client-cio:${ktor_version}")
    implementation("ch.qos.logback:logback-classic:${logback_version}")
}

application {
    mainClass.set("org.example.ApplicationKt")
}

// 🔧 Forzar toolchain Java 21
kotlin {
    jvmToolchain(21)
}

