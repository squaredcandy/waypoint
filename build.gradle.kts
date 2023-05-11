import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle)
    }
}

subprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_metrics"
            )
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_metrics"
            )
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs +
                    "-opt-in=kotlin.RequiresOptIn" +
                    "-opt-in=androidx.compose.animation.ExperimentalAnimationApi" +
                    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi" +
                    "-opt-in=androidx.compose.material.ExperimentalMaterialApi" +
                    "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api" +
                    "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs +
                "-opt-in=kotlin.RequiresOptIn" +
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi" +
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi" +
                "-opt-in=androidx.compose.material.ExperimentalMaterialApi" +
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api" +
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
    }
}
