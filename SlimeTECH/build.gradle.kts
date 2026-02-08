plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.slimetech"
version = "1.0.1-DEV" // DEV suffix in version

// STRICT Java 17 enforcement
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

// Force all tasks to use Java 17
tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-source", "17", "-target", "17", "-parameters"))
    options.release.set(17)
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(17))
    })
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    // Backup repos for 1.17.1 dependencies
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://libraries.minecraft.net/")
    }
}

dependencies {
    // PaperMC 1.17.1 API (must have)
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    
    // Include dependencies that Paper 1.17.1 needs
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("com.mojang:datafixerupper:4.0.26")
    compileOnly("com.google.code.gson:gson:2.8.8")
}

tasks {
    // Configure shadowJar for dependencies (optional)
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("SlimeTECH-D${project.version}.jar")
        relocate("com.google.gson", "com.slimetech.libs.gson")
        minimize()
    }
    
    // Regular jar (no dependencies)
    jar {
        archiveFileName.set("SlimeTECH-D${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        
        // Manifest for Java 17
        manifest {
            attributes(
                "Implementation-Title" to "SlimeTECH",
                "Implementation-Version" to version,
                "Built-By" to System.getProperty("user.name"),
                "Built-JDK" to System.getProperty("java.version"),
                "Build-Timestamp" to java.time.Instant.now().toString()
            )
        }
    }
    
    // Build task that depends on jar
    build {
        dependsOn(jar)
    }
    
    // Clean task
    clean {
        delete("build")
    }
}

// Gradle properties for Java 17
System.setProperty("org.gradle.java.home", "/usr/lib/jvm/java-17-openjdk-amd64")