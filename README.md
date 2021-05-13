![Build Status][build-badge]

# Gradle Classpath

Output the classpath string of dependencies for a [Gradle][gradle] project.

## Rationale

Unlike [Maven][maven], which provides [dependency:build-classpath][mvn-classpath], Gradle doesn't
offer a standard task for outputting the classpath of a project. The goal of this project is to
provide that missing functionality in a simple command-line utility which behaves much like Maven's.

This Gradle classpath utility is used by the [coc-groovy][coc-groovy] project.

## Prerequisites

- [Java][java] (version 1.8.0 or later)

## Installation

### Option 1: Release

This is the easiest way to get started.

Download the latest package from the [Releases page][releases] and then extract the download.

Be sure to add the location of the `gradle-classpath/bin` directory to your `$PATH`.

### Option 2: From source

See [Building](#building)

Be sure to add the location of the `build/install/gradle-classpath/bin/` directory to your `$PATH`.

## Usage

In its simplest form `gradle-classpath` can be called like this to output the classpath of a Gradle project.
```bash
$ cd {your Gradle project directory}
$ gradle-classpath
```

Or the classpath can be written to a file.
```bash
$ cd {your Gradle project directory}
$ gradle-classpath --output-file classpath.txt
```

Or display the `gradle-classpath` full usage details.
```bash
$ gradle-classpath --help
Usage: gradle-classpath [-hrV] [-d=<projectPath>] [-g=<gradleUserHomeDir>]
                        [-G=<gradleHome>] [-o=<outputFile>] [-s=<pathSeparator>]
Outputs the Gradle classpath to a file or STDOUT.
  -d, --project-dir=<projectPath>
                          Path to the Gradle project. Defaults to the current
                            directory.
  -g, --gradle-user-home=<gradleUserHomeDir>
                          Specifies the Gradle user home directory. Defaults to
                            ~/.gradle.
  -G, --gradle-home=<gradleHome>
                          Specifies the Gradle installation directory to use.
                            Defaults to a project-specific Gradle version.
  -h, --help              Show this help message and exit.
  -o, --output-file=<outputFile>
                          Write the classpath to this file. If undefined, the
                            output is sent to STDOUT.
  -r, --regenerate-file   If 'true' it always regenerates the classpath file.
                            If 'false' it is not regenerated if it exists.
                            Defaults to 'false'
  -s, --path-separator=<pathSeparator>
                          The character used between paths. Defaults to ':'
  -V, --version           Print version information and exit.
```

## Contributing

### Building

1. Clone this project and run the [Gradle install task][gradle-install].

    ```bash
    $ git clone https://github.com/dansomething/gradle-classpath.git
    $ cd gradle-classpath
    $ ./gradlew installDist
    ```

1. Look in `build/install/gradle-classpath` for the output.

### Debugging

1. Debug gradle-classpath on itself.

    ```bash
    $ ./gradlew run --debug-jvm
    ```

1. Set breakpoints.
1. Attach a Java debugger client to port 5005.

### Running

* Run gradle-classpath on itself.

    ```bash
    $ ./gradlew run
    ```

* Run gradle-classpath on itself and write the results to a file.

    ```bash
    ./gradlew run --args='-o classpath.txt'
    ```

[build-badge]:    https://github.com/dansomething/gradle-classpath/actions/workflows/gradle.yml/badge.svg
[coc-groovy]:     https://github.com/dansomething/coc-groovy
[gradle]:         https://gradle.org
[gradle-install]: https://docs.gradle.org/current/userguide/application_plugin.html#sec:application_tasks
[java]:           https://www.oracle.com/technetwork/java/javase/downloads/index.html
[maven]:          https://maven.apache.org
[mvn-classpath]:  https://maven.apache.org/plugins/maven-dependency-plugin/build-classpath-mojo.html
[releases]:       https://github.com/dansomething/gradle-classpath/releases
