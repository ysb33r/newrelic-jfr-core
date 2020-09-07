
val gsonVersion: String by project
val log4jVersion: String by project
val newRelicTelemetry: String by project
val slf4jVersion: String by project

plugins {
    id("org.beryx.jlink")
    id("org.ysb33r.java.modulehelper")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    disableAutoTargetJvm()
}

extraJavaModules {
//    module("slf4j-api-${slf4jVersion}.jar", "org.slf4j", slf4jVersion) {
//        exports("org.slf4j")
//        exports("org.slf4j.event")
//    }
//    module("gson-${gsonVersion}.jar", "com.google.code.gson", gsonVersion) {
//        exports("com.google.gson")
//    }
//    module("telemetry-all-0.8.0-SNAPSHOT.jar", "com.newrelic.telemetry", "0.8.0-SNAPSHOT") {
//        exports("com.newrelic.telemetry")
//    }
}


dependencies {
    implementation(project(":jfr-mappers"))
    implementation("org.slf4j:slf4j-api:${slf4jVersion}")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:${log4jVersion}")
    implementation("org.apache.logging.log4j:log4j-core:${log4jVersion}")
//    implementation("com.newrelic.telemetry:telemetry-all:${Versions.newRelicTelemetry}")
//    implementation("com.google.code.gson:gson:${Versions.gson}")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.newrelic"
            artifactId = "jfr-daemon"
            version = version
            from(components["java"])
            pom {
                name.set(project.name)
                description.set("JFR Daemon")
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
    val signingKey: String? by project
    val signingKeyId: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    this.sign(publishing.publications["maven"])
}

application {
    mainClass.set("com.newrelic.jfr.daemon.JFRDaemon")
    mainModule.set("com.newrelic.jfr.daemon")
}

jlink {
}

