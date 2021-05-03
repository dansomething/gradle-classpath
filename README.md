# Gradle Classpath

Output the classpath string of dependencies for a [Gradle][gradle] project.

## Rationale

Unlike [Maven][maven], which provides [dependency:build-classpath][mvn-classpath], Gradle doesn't
offer a standard task for outputting the classpath of a project. The goal of this project is to
provide that missing functionality in a simple command-line utility which behaves much like Maven's.

This Gradle classpath utility is used by the [coc-groovy][coc-groovy] project.

## Installation

### Option 1: Release

This is the easiest way to get started.

Download the latest package from the [Releases page][releases] and then extract the download.

Make sure to add the location of the `gradle-classpath/bin` directory to your `$PATH`.

### Option 2: From source

```bash
$ git clone https://github.com/dansomething/gradle-classpath.git
$ cd gradle-classpath
$ ./gradlew installDist
```

Make sure to add the location of the `build/install/gradle-classpath/bin/` directory to your `$PATH`.

## Usage

In its simplest form `gradle-classpath` can be called like this to output the classpath.
```bash
$ cd {your project directory}
$ gradle-classpath
```

Or the classpath can be written to a file.
```bash
$ cd {your project directory}
$ gradle-classpath --output-file classpath.txt
```

Or display the `gradle-classpath` full usage details.
```bash
$ gradle-classpath --help
Usage: gradle-classpath [-hV] [-d=<projectPath>] [-g=<gradleUserHomeDir>]
                        [-G=<gradleHome>] [-o=<outputFile>] [-s=<pathSeparator>]
Outputs the Gradle classpath to a file or STDOUT.
  -d, --project-dir=<projectPath>
                  Path to the Gradle project. Defaults to the current directory.
  -g, --gradle-user-home=<gradleUserHomeDir>
                  Specifies the Gradle user home directory. Defaults to ~/.
                    gradle.
  -G, --gradle-home=<gradleHome>
                  Specifies the Gradle installation directory to use. Defaults
                    to a project-specific Gradle version.
  -h, --help      Show this help message and exit.
  -o, --output-file=<outputFile>
                  Write the classpath to this file. If undefined, the output is
                    sent to STDOUT.
  -s, --path-separator=<pathSeparator>
                  The character used between paths. Defaults to ':'
  -V, --version   Print version information and exit.
```

[coc-groovy]:    https://github.com/dansomething/coc-groovy
[gradle]:        https://gradle.org
[maven]:         https://maven.apache.org
[mvn-classpath]: https://maven.apache.org/plugins/maven-dependency-plugin/build-classpath-mojo.html
[releases]:      https://github.com/dansomething/gradle-classpath/releases
