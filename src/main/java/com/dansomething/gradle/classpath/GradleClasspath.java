package com.dansomething.gradle.classpath;

import static com.dansomething.gradle.classpath.ExceptionUtils.getRootCause;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.eclipse.EclipseProject;

/** Generates a classpath string for a Gradle project. */
class GradleClasspath {
  static String getClasspath(
      final String gradleHome,
      final String gradleUserHomeDir,
      final String projectPath,
      final String pathSeparator)
      throws ClasspathException {
    requireNonNull(projectPath, "A project path is required.");
    requireNonNull(pathSeparator, "A path separator is required.");

    final GradleConnector connector = GradleConnector.newConnector();

    if (gradleHome != null) {
      final File gradleHomeFile = new File(gradleHome);
      if (!gradleHomeFile.exists()) {
        throw new ClasspathException(
            String.format("The specified gradle home directory doesn't exist? '%s'", gradleHome));
      }
      connector.useInstallation(gradleHomeFile);
    }

    if (gradleUserHomeDir != null) {
      final File gradleUserHomeDirFile = new File(gradleUserHomeDir);
      if (!gradleUserHomeDirFile.exists()) {
        throw new ClasspathException(
            String.format(
                "The specified gradle user home directory doesn't exist? '%s'", gradleUserHomeDir));
      }
      connector.useGradleUserHomeDir(gradleUserHomeDirFile);
    }

    final File projectPathFile = new File(projectPath);
    if (!projectPathFile.exists()) {
      throw new ClasspathException(
          String.format("The specified project directory doesn't exist? '%s'", projectPath));
    }

    final Path buildGradlePath = Paths.get(projectPath, "build.gradle");
    if (!buildGradlePath.toFile().exists()) {
      throw new ClasspathException(
          String.format(
              "The specified project directory doesn't contain a build.gradle file? '%s'",
              projectPath));
    }

    connector.forProjectDirectory(projectPathFile);

    try (final ProjectConnection connection = connector.connect()) {
      final EclipseProject project = connection.getModel(EclipseProject.class);

      return project.getClasspath().stream()
          .map(d -> d.getFile().getPath())
          .collect(Collectors.joining(pathSeparator));
    } catch (final Exception e) {
      throw new ClasspathException(String.format("Error! %s", getRootCause(e).getMessage()));
    }
  }
}
