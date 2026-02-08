plugins {
    java
}

group = "com.slimetech"
version = "1.0.1"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    
    jar {
        archiveFileName.set("SlimeTECH-D${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}