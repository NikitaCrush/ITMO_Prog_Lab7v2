
plugins {
    kotlin("jvm") version "1.8.0"
    application
    kotlin("plugin.serialization") version "1.5.10"
}
group = "com.NikitaCrush"
version = "2.0"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.insert-koin:koin-core:3.3.3")
    implementation ("org.postgresql:postgresql:42.2.27")
    implementation ("org.jetbrains.exposed:exposed-core:0.35.1")
    implementation ("org.jetbrains.exposed:exposed-dao:0.35.1")
    implementation ("org.jetbrains.exposed:exposed-jdbc:0.35.1")
    implementation("org.bitbucket.b_c:jose4j:0.7.2")
    implementation("commons-codec:commons-codec:1.15")
    implementation ("ch.qos.logback:logback-classic:1.2.9")
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))

    implementation(project(":common"))

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