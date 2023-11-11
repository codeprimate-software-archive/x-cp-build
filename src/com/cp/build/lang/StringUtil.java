/*
 * StringUtil.java (c) 28 October 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.12.12
 */

package com.cp.build.lang;

public final class StringUtil {

  public static int indexOf(final String value, final String indexValue) {
    return indexOf(value, indexValue, 0);
  }

  public static int indexOf(final String value, final String indexValue, final int fromIndex) {
    return (ObjectUtil.isNotNull(value) ? value.indexOf(indexValue, fromIndex) : -1);
  }

  public static boolean isEmpty(final String value) {
    return (ObjectUtil.isNull(value) || "".equals(value.trim()));
  }

  public static boolean isNotEmpty(final String value) {
    return (ObjectUtil.isNotNull(value) && !"".equals(value.trim()));
  }

  public static int lastIndexOf(final String value, final String indexValue) {
    return lastIndexOf(value, indexValue, 0);
  }

  public static int lastIndexOf(final String value, final String indexValue, final int fromIndex) {
    return (ObjectUtil.isNotNull(value) ? value.lastIndexOf(indexValue, fromIndex) : -1);
  }

  public static String trim(final String value) {
    return (ObjectUtil.isNull(value) ? null : value.trim());
  }

}
