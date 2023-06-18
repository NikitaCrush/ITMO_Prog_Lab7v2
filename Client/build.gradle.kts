plugins {
    kotlin("jvm") version "1.8.0"
    id ("org.openjfx.javafxplugin") version "0.0.8"

    kotlin("plugin.serialization") version "1.5.10"

    application
}

group = "org.NikitaCrush"
version = "2.0"

dependencies {
    implementation(project(":common"))
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("io.insert-koin:koin-core:3.3.3")
    implementation("io.insert-koin:koin-core:3.3.3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("org.postgresql:postgresql:42.2.27")
    implementation ("org.openjfx:javafx-controls:16")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}


application {
    mainClass.set("MainKt")
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}