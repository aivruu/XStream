**XStream** is a plugin that allows to your Streamers announce their streams/lives globally to all the server, constantly from time to time of asynchronous way to avoid loss performance. Very customizable with multiple functions and checks to avoid all bypass type or errors.

# 🛠️ | Import
If you're using a dependency manager such as Maven or Gradle. Or just import the library to BuildPath of your project.

To get the jar, either download it from [GitHub](https://github.com/InitSync/XStream/releases) or [Spigot](https://www.spigotmc.org/resources/xconfig.105977/). Or just [Build it locally](https://github.com/InitSync/XStream#--build)

Gradle (Kotlin DSL)
```Gradle
repositories {
  maven("https://jitpack.io")
  mavenCentral()
}

dependencies {
  compileOnly("com.github.InitSync:XStream:1.0.0")
}
```

Maven
```Xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.InitSync</groupId>
    <artifactId>XStream</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

# ➕ | Contribute
Do you want contribute with the library?

* [Make a Pull Request](https://github.com/InitSync/XStream/compare)
* [Issues](https://github.com/InitSync/XStream/issues/new)

# ✅ | Build
If you want build the project locally, download it, you must be had Gradle and Java 8+ for this.

Now for build the project
```
git clone https://github.com/InitSync/XStream
cd XConfig
./gradlew clean shadowJar
```

The file will be at ```bin/XStream-[release].jar```.

# 🎫 | License
This project is licensed under the GNU General Public License v3.0 license, for more details see the file [License](LICENSE)
