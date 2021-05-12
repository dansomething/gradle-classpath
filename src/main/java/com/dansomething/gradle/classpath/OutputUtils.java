package com.dansomething.gradle.classpath;

import static java.util.Objects.requireNonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Utilities for working with application output. */
class OutputUtils {
  static void print(final String message, final File outputFile, final boolean regenerateFile)
      throws ClasspathException {
    if (outputFile == null) {
      toStdout(message);
    } else {
      toFile(message, outputFile, regenerateFile);
    }
  }

  static void toStderr(final String message) {
    System.err.println(message);
  }

  static void toStdout(final String message) {
    System.out.println(message);
  }

  static boolean skipFileGeneration(final File outputFile, final boolean regenerateFile)
      throws ClasspathException {
    requireNonNull(outputFile);

    try {
      final Path path = Paths.get(outputFile.getPath());
      return outputFile.exists() && Files.size(path) > 0 && !regenerateFile;
    } catch (IOException e) {
      throw new ClasspathException(
          String.format("Failed to read file. '%s'. Error! %s", outputFile, e.getMessage()));
    }
  }

  static void toFile(final String message, final File outputFile, final boolean regenerateFile)
      throws ClasspathException {
    try {
      if (skipFileGeneration(outputFile, regenerateFile)) {
        return;
      }

      if (!outputFile.exists()) {
        final Path path = Paths.get(outputFile.getPath());
        final Path parent = path.getParent();
        if (parent != null) {
          Files.createDirectories(parent);
        }
        outputFile.createNewFile();
      }

      try (FileWriter fw = new FileWriter(outputFile);
          BufferedWriter bw = new BufferedWriter(fw)) {
        bw.write(message);
      }
    } catch (final IOException e) {
      throw new ClasspathException(
          String.format("Failed to write file. '%s'. Error! %s", outputFile, e.getMessage()));
    }
  }
}
