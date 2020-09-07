plugins {
//    id("org.beryx.jlink")
    id( "org.ysb33r.java.modulehelper")
//    id("com.github.johnrengelman.shadow") version "5.2.0"
}

val newRelicTelemetryVersion: String by project
val gsonVersion : String by project

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    disableAutoTargetJvm()
}

tasks {
    val taskScope = this
    val jar: Jar by taskScope
    jar.apply {
        manifest.attributes["Implementation-Version"] = project.version
        manifest.attributes["Implementation-Vendor"] = "New Relic, Inc"
    }
}

dependencies {
    "api"("com.newrelic.telemetry:telemetry-all:${newRelicTelemetryVersion}")
    implementation("com.google.code.gson:gson:${gsonVersion}")
//    api("org.slf4j:slf4j-api:${slf4jVersion}")
}

extraJavaModules {
//    module("slf4j-api-${slf4jVersion}.jar", "org.slf4j", slf4jVersion) {
//        exports("org.slf4j")
//    }
    module("gson-${gsonVersion}.jar", "com.google.code.gson", gsonVersion) {
        exports("com.google.gson")
    }
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.newrelic"
            artifactId = "jfr-mappers"
            version = version
            from(components["java"])
            pom {
                name.set(project.name)
                description.set("Library of mappers to turn JFR RecordedEvents into New Relic telemetry")
                url.set("https://github.com/newrelic/newrelic-jfr-core")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("newrelic")
                        name.set("New Relic")
                        email.set("opensource@newrelic.com")
                    }
                }
                scm {
                    url.set("git@github.com:newrelic/newrelic-jfr-core.git")
                    connection.set("scm:git:git@github.com:newrelic/newrelic-jfr-core.git")
                }
            }
        }
    }
}

signing {
    val signingKey : String? by project
    val signingKeyId: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    this.sign(publishing.publications["maven"])
}

// This is only here to find the correct relocations when composing builds.
// It only affetcs local development for people working on both newrelifc-jfr-core and newrelic-telemetry-sdk-java
// at the same time.
//if(project.gradle.includedBuilds.find { it.name == "newrelic-telemetry-sdk-java" } != null ) {
//    var coreShadow = File(project.gradle.includedBuild("newrelic-telemetry-sdk-java").projectDir,"telemetry_core/build/libs/telemetry-core-${newRelicTelemetryVersion}.jar")
//    dependencies {
//        "implementation"(files(coreShadow))
//    }
//}