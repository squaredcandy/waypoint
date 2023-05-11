pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "waypoint"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    ":example",
    ":waypoint-core",
)
