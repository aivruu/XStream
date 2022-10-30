plugins {
	id("java")
}

repositories {
	maven("https://oss.sonatype.org/content/groups/public/")
	mavenCentral()
}

dependencies {
	compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
	
	implementation(project(":api"))
	
	implementation("org.jetbrains:annotations:23.0.0")
	implementation("commons-lang:commons-lang:2.6")
}