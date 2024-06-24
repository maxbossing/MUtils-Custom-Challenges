import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"

    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

group = "de.maxbossing"
version = 2

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")

    // Older version cuz API uses old
    paperLibrary("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.6.2")

    paperLibrary("de.maxbossing","mxpaper", "3.0.0")

    compileOnly("de.miraculixx", "challenge-api", "1.2.1")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
    runServer {
        minecraftVersion("1.21")
    }
}

paper {
    name = "MXChallenges"
    description = "Challenges that aren't in MUtils, but should!"

    apiVersion = "1.21"

    author = "Max Bossing <info@maxbossing.de>"
    website = "https://maxbossing.de"

    // I see no problem with folia
    foliaSupported = true

    // Start at server start
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP

    // Main Class
    main = "de.maxbossing.mxchallenges.main.MXChallenges"

    // Loads libraries from paper-libraries.json
    loader = "de.maxbossing.mxchallenges.main.MXChallengesLoader"

    // Creates a paper-libraries.json file in JAR resources to read and load libraries at runtime
    generateLibrariesJson = true

    // Needed Plugin Dependencies at lifecycle start
    bootstrapDependencies {
        register("MUtils-Challenge") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
    }

    // Needed Plugin dependencies at runtime
    serverDependencies {
        register("MUtils-Challenge") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
    }
}