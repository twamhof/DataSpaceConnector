val infoModelVersion: String by project
val jacksonVersion: String by project
val jerseyVersion: String by project

plugins {
    `java-library`
}

dependencies {
    implementation(project(":core"))
    implementation(project(":extensions:protocol:web"))
    implementation(project(":extensions:control-http"))
    
    implementation(project(":extensions:ids:ids-core"))
    implementation(project(":extensions:ids:ids-api-catalog"))
    implementation(project(":extensions:ids:ids-catalog-memory"))
    implementation(project(":extensions:ids:ids-api-transfer"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")

}
