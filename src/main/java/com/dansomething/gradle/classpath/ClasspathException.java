package com.dansomething.gradle.classpath;

/**
 * Represents an exception when accessing the classpath of a project.
 */
class ClasspathException extends Exception {
  private static final long serialVersionUID = 1L;

  ClasspathException(final String message) {
    super(message);
  }
}
