## Including in your mod
This mod is designed for architectury. Due to being very early beta, I have no idea whether implementing this outside of an Architectury's common module would work. Feel free to try anyway.

```gradle
repositories {
    (...)
    maven {
        url = uri("https://maven.pkg.github.com/GDPulsar/Librarby")
        credentials {
            username = findProperty("gpr.user")
            password = findProperty("gpr.token")
        }
    }
}

dependencies {
    (...)
    // If including the library in your mod jar is not preferred, then remove the include word.
    include modImplementation("io.github.gdpulsar:classy-common:0.1.1")
}
```

## Usage
Check the wiki section for how to use Classy!