rootProject.name = "newrelic-jfr-core"

include("jfr-mappers")
include("jfr-daemon")

if(file("../newrelic-telemetry-sdk-java").exists()){
    includeBuild("../newrelic-telemetry-sdk-java")
}
