package com.dansomething.gradle.classpath;

import static com.dansomething.gradle.classpath.GradleClasspath.getClasspath;
import static com.dansomething.gradle.classpath.OutputUtils.print;
import static com.dansomething.gradle.classpath.OutputUtils.toStderr;
import static com.dansomething.gradle.classpath.OutputUtils.toStdout;
import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * A simple application to print the classpath from a Gradle project to stdout.
 */
@Command(name = "gradle-classpath", mixinStandardHelpOptions = true,
    version = "gradle-classpath 0.1.0",
    description = "Outputs the Gradle classpath to a file or STDOUT.")
public class App implements Callable<Integer> {
  @Option(names = {"-G", "--gradle-home"},
      description = "Specifies the Gradle installation directory to use."
          + " Defaults to a project-specific Gradle version.")
  private String gradleHome;

  @Option(names = {"-g", "--gradle-user-home"},
      description = "Specifies the Gradle user home directory. Defaults to ~/.gradle.")
  private String gradleUserHomeDir;

  @Option(names = {"-o", "--output-file"},
      description = "Write the classpath to this file. If undefined, the output is sent to STDOUT.")
  private File outputFile;

  @Option(names = {"-d", "--project-dir"},
      description = "Path to the Gradle project. Defaults to the current directory.",
      defaultValue = ".")
  private String projectPath;

  @Option(names = {"-s", "--path-separator"},
      description = "The character used between paths. Defaults to ':'", defaultValue = ":")
  private String pathSeparator;

  /**
   * Generates the classpath from the Eclipse model and outputs it.
   *
   * @return The exit code.
   */
  @Override
  public Integer call() {
    try {
      final String classpath = getClasspath(
        gradleHome,
        gradleUserHomeDir,
        projectPath,
        pathSeparator
      );
      print(classpath, outputFile);
      if (outputFile != null) {
        toStdout(String.format("Wrote classpath file '%s'", outputFile.getPath()));
        toStdout("BUILD SUCCESS");
      }
      return 0;
    } catch (final ClasspathException e) {
      toStderr(e.getMessage());
      return 1;
    }
  }

  /**
   * The main entry point to this application.
   *
   * <p>
   * See the fields in {@link App} for the possible command-line arguments.
   * </P>
   *
   * @param args The command-line arguments provided.
   */
  public static void main(final String... args) {
    final int exitCode = new CommandLine(new App()).execute(args);
    System.exit(exitCode);
  }
}
