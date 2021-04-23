package com.dansomething.gradle.classpath;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.eclipse.EclipseProject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * A simple application to print the classpath from a Gradle project to stdout.
 */
@Command(
  name = "gradle-classpath",
  mixinStandardHelpOptions = true,
  version = "gradle-classpath 0.1.0",
  description = "Prints the Gradle classpath to a file or STDOUT."
)
public class App implements Callable<Integer> {
  @Option(
    names = { "-G", "--gradle-home" },
    description = "Specifies the Gradle installation directory to use."
      + " Defaults to a project-specific Gradle version."
  )
  private String gradleHome;

  @Option(
    names = { "-g", "--gradle-user-home" },
    description = "Specifies the Gradle user home directory. Defaults to ~/.gradle."
  )
  private String gradleUserHomeDir;

  @Option(
    names = { "-o", "--output-file" },
    description = "Write the classpath to this file. If undefined, the output is sent to STDOUT."
  )
  private File outputFile;

  @Option(
    names = { "-d", "--project-dir" },
    description = "Path to the Gradle project. Defaults to the current directory.",
    defaultValue = "."
  )
  private String projectPath;

  @Option(
    names = { "-s", "--path-separator" },
    description = "The character used between paths. Defaults to ':'",
    defaultValue = ":"
  )
  private String pathSeparator;

  /**
   * Generates the classpath from the Eclipse model and outputs it.
   *
   * @return The exit code.
   */
  @Override
  public Integer call() {
    GradleConnector connector = GradleConnector.newConnector();

    if (gradleHome != null) {
      connector.useInstallation(new File(gradleHome));
    }
    if (gradleUserHomeDir != null) {
      connector.useGradleUserHomeDir(new File(gradleUserHomeDir));
    }

    File projectDir = new File(projectPath);
    if (!projectDir.exists()) {
      System.err.println(String.format(
        "The specified project directory doesn't exist? --project-dir '%s'",
        projectDir
      ));
      return 1;
    }

    connector.forProjectDirectory(projectDir);
    ProjectConnection connection = connector.connect();

    try {
      EclipseProject project = connection.getModel(EclipseProject.class);

      String classpath = project.getClasspath().stream()
        .map(d -> d.getFile().getPath())
        .collect(Collectors.joining(pathSeparator));

      return output(classpath);
    } catch (Exception e) {
      System.err.println(String.format("Error: %s", getRootCause(e).getMessage()));
      return 1;
    } finally {
      connection.close();
    }
  }

  private int output(final String classpath) {
    if (outputFile == null) {
      System.out.println(classpath);
    } else {
      try {
        if (!outputFile.exists()) {
          Path path = Paths.get(outputFile.getPath());
          Path parent = path.getParent();
          if (parent != null) {
            Files.createDirectories(parent);
          }
          outputFile.createNewFile();
        }
        try (FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(fw)
        ) {
          bw.write(classpath);
        }
      } catch (IOException e) {
        System.err.println(String.format(
          "Failed to write classpath. --output-file '%s'. Error: %s",
          outputFile,
          e.getMessage()
        ));
        return 1;
      }
    }

    return 0;
  }

  private static Throwable getRootCause(final Throwable throwable) {
    Objects.requireNonNull(throwable);
    Throwable rootCause = throwable;
    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
      rootCause = rootCause.getCause();
    }
    return rootCause;
  }

  /**
   * The main entry point.
   *
   * <p>
   * See the fields in {@link App} for the possible command-line arguments.
   * </P>
   *
   * @param args The command-line arguments provided.
   */
  public static void main(final String... args) {
    int exitCode = new CommandLine(new App()).execute(args);
    System.exit(exitCode);
  }
}
