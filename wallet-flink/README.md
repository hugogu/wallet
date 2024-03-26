# Wallet Flink Job

## How to build

```shell
gradle :wallet-flink:copyDependencies
docker build -t flink-spring wallet-flink
rm -rf wallet-flink/libs
```


## References
* [Apache Flink quickstart with Kotlin and Gradle](https://mgaiduk.substack.com/p/apache-flink-quickstart-with-kotlin)

## Spring Boot Jar

A spring boot jar contains quite a lot of dependencies that eventually get built into the user jar.

This can easily lead to [Metaspace OOM error in Flink](https://nightlies.apache.org/flink/flink-docs-master/docs/ops/debugging/debugging_classloading/#unloading-of-dynamically-loaded-classes-in-user-code). 

Dependencies should be placed in Flink's /lib folder whenever possible to keep the user jar as small as possible.

That is to say, introducing Spring Boot is not a good idea.

### Spring FatJar

The spring fat jar cannot be used in Flink as some of the dependencies are placed in the BOOT-INF/lib folder,
which is not accessible by Flink in its default behavior.

### Plain Jar

Using the following configuration in gradle, dependencies are included and accessible by Flink.

But the resource files, such as `application.yml`, can't be found by Spring framework, so it doesn't work. 

```kotlin
    tasks.jar {
        isZip64 = true
        manifest.attributes["Main-Class"] = "FraudDetection"
        val dependencies = configurations
            .runtimeClasspath
            .get()
            .map(::zipTree)
        from(dependencies)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        exclude("META-INF/*")
        exclude("org/apache/flink/*")
        exclude("org/scala-lang/*")
    }
```
### To explore

From an open source solution.
* [Flink Spring Boot Starter](https://github.com/intsmaze/flink-boot)
