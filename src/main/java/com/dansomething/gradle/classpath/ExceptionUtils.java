package com.dansomething.gradle.classpath;

import static java.util.Objects.requireNonNull;

/**
 * Utilities for working with exceptions.
 */
public class ExceptionUtils {
  static Throwable getRootCause(final Throwable throwable) {
    requireNonNull(throwable);
    Throwable rootCause = throwable;
    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
      rootCause = rootCause.getCause();
    }
    return rootCause;
  }
}
