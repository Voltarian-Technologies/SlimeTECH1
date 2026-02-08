plugins {
    java
}

group = "com.slimetech"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    
    jar {
        archiveFileName.set("SlimeTECH.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}