plugins {
    java
}

group = "com.slimetech"
version = "1.0.1"

// Java 17 (required for 1.17.1+)
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    // Backup repositories
    maven {
        url = uri("https://libraries.minecraft.net/")
    }
    maven {
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
}

dependencies {
    // PaperMC 1.17.1 API (when copper was introduced)
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
        
        // Paper-specific compiler args
        options.compilerArgs.add("-parameters")
    }
    
    jar {
        archiveFileName.set("SlimeTECH-D$version.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    
    // Helpful for debugging
    task("printDependencies") {
        doLast {
            configurations.compileClasspath.get().forEach { println(it.name) }
        }
    }
}