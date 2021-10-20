plugins {
    kotlin("jvm") version "1.5.31"
}

group = "xyz.acrylicstyle"
version = "1.0.8d"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf(
            "-Xjsr305=strict"
        )
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "utf-8"
    }

    withType<ProcessResources> {
        filteringCharset = "UTF-8"
        from(sourceSets.main.get().resources.srcDirs) {
            include("**")

            val tokenReplacementMap = mapOf(
                "version" to project.version,
                "name" to project.rootProject.name
            )

            filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to tokenReplacementMap)
        }

        from(projectDir) {
            include("LICENSE")
        }
    }

    withType<Jar> {
        doFirst {
            manifest {
                manifest.attributes["Main-Class"] = "xyz.acrylicstyle.unusedCommentRemover.Main"
            }
        }
        from(configurations.getByName("implementation").apply { isCanBeResolved = true }.map { if (it.isDirectory) it else zipTree(it) })
    }
}
