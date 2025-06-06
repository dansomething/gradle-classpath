package com.dansomething.gradle.classpath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExceptionUtilsTests {

  @Test
  @DisplayName("getRootCause returns the root cause of a nested exception")
  void getRootCause_returnsRootCause() {
    Throwable root = new IllegalArgumentException("root");
    Throwable mid = new RuntimeException("mid", root);
    Throwable top = new Exception("top", mid);
    assertEquals(root, ExceptionUtils.getRootCause(top));
  }

  @Test
  @DisplayName("getRootCause returns the same throwable if there is no cause")
  void getRootCause_returnsSelfIfNoCause() {
    Throwable t = new Exception("no cause");
    assertEquals(t, ExceptionUtils.getRootCause(t));
  }

  @Test
  @DisplayName("getRootCause throws NullPointerException if argument is null")
  void getRootCause_throwsOnNull() {
    try {
      ExceptionUtils.getRootCause(null);
    } catch (NullPointerException e) {
      return;
    }
    throw new AssertionError("Expected NullPointerException");
  }
}
