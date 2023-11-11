/*
 * ObjectUtil.java (c) 28 October 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.12.12
 */

package com.cp.build.lang;

public final class ObjectUtil {

  public static boolean equals(final Object obj0, final Object obj1) {
    return (isNull(obj0) ? isNull(obj1) : obj0.equals(obj1));
  }

  public static <T> T getDefaultValue(final T value, final T... defaultValues) {
    if (isNull(value)) {
      for (final T defaultValue : defaultValues) {
        if (isNotNull(defaultValue)) {
          return defaultValue;
        }
      }
    }

    return value;
  }

  public static int hashCode(final Object obj) {
    return (isNull(obj) ? 0 : obj.hashCode());
  }

  public static boolean isNull(final Object value) {
    return (value == null);
  }

  public static boolean isNotNull(final Object value) {
    return (value != null);
  }

  public static String toString(final Object value) {
    return (isNull(value) ? null : value.toString());
  }

}
