/*
 * Assert.java (c) 28 October 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.10.28
 */

package com.cp.build.lang;

public final class Assert {

  public static void notEmpty(final String value, final String message) {
    if (StringUtil.isEmpty(value)) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notNull(final Object value, final String message) {
    if (ObjectUtil.isNull(value)) {
      throw new NullPointerException(message);
    }
  }

  public static void state(final boolean condition, final String message) {
    if (!condition) {
      throw new IllegalStateException(message);
    }
  }

}
