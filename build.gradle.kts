plugins {
    id("java-library")
    id("groovy")
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "com.github.blindpirate"
version = "0.0.7"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    api(gradleApi())
    api(localGroovy())
    api("biz.aQute.bnd:biz.aQute.bndlib:6.4.1")

    testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
    testImplementation("net.bytebuddy:byte-buddy:1.14.9")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.vintage:junit-vintage-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

gradlePlugin {
    website = "https://github.com/blindpirate/gradle-legacy-osgi-plugin"
    vcsUrl = "https://github.com/blindpirate/gradle-legacy-osgi-plugin"

    plugins {
        create("osgiPlugin") {
            id = "com.github.blindpirate.osgi"
            implementationClass = "com.github.blindpirate.osgi.plugins.osgi.OsgiPlugin"
            displayName = "A legacy osgi plugin in Gradle 5"
            description = "A legacy osgi plugin in Gradle 5"
            tags = listOf("legacy", "osgi")
        }
    }
}

tasks.publishPlugins {
    notCompatibleWithConfigurationCache("https://github.com/gradle/gradle/issues/21283")

    dependsOn(tasks.check)
}

tasks.test {
    useJUnitPlatform()
}
