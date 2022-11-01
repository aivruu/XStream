plugins {
	id("com.github.johnrengelman.shadow") version("7.1.2")
	id("net.minecrell.plugin-yml.bukkit") version("0.5.2")
	id("net.minecrell.plugin-yml.bungee") version("0.5.2")
	`java-library`
	`maven-publish`
}

val directory = property("group") as String
val release = property("version") as String
val libsDirectory = property("libs") as String
val description = "Simple way to announce your streams globally!"

repositories {
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven("https://jitpack.io/")
	maven("https://repo.unnamed.team/repository/unnamed-releases/")
	mavenCentral()
}

dependencies {
	compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
	
	implementation(project(":api"))
	implementation(project(":bungee"))
	
	implementation("org.jetbrains:annotations:23.0.0")
	implementation("commons-lang:commons-lang:2.6")
	implementation("com.github.InitSync:XConfig:1.0.2")
	implementation("com.github.cryptomorin:XSeries:9.1.0")
	implementation("team.unnamed:gui-menu-api:3.3.2")
	implementation("team.unnamed:gui-menu-adapt-v1_8_R3:3.3.2")
}

bukkit {
	name = rootProject.name
	main = "$directory.bukkit.XStream"
	authors = listOf("InitSync")
	
	apiVersion = "1.13"
	version = release
	
	permissions {
		register("xstream.*") {
			children = listOf(
				 "xstream.help",
				 "xstream.reload",
				 "xstream.live"
			)
		}
		register("xstream.help")
		register("xstream.reload")
		register("xstream.live")
	}
	
	commands {
		register("xstream") {
			description = "-> Command to manage the plugin."
			aliases = listOf("xs")
		}
		register("live") {
			description = "-> Command to announce streams."
			aliases = listOf("stream", "youtube", "twitch")
		}
	}
}

bungee {
	name = rootProject.name
	main = "$directory.bungee.XStream"
	author = "InitSync"
	
	version = release
}

tasks {
	shadowJar {
		archiveFileName.set("XStream-$release.jar")
		destinationDirectory.set(file("$rootDir/bin/"))
		minimize()
		
		relocate("org.jetbrains.annotations", "$libsDirectory.annotations")
		relocate("org.apache.commons", "$libsDirectory.commons")
		relocate("net.xconfig", "$libsDirectory.xconfig")
		relocate("com.cryptomorin.xseries", "$libsDirectory.xseries")
	}
	
	withType<JavaCompile> {
		options.encoding = "UTF-8"
	}
	
	clean {
		delete("$rootDir/bin/")
	}
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "net.xstream"
			artifactId = "XStream"
			version = release
			
			from(components["java"])
		}
	}
}
