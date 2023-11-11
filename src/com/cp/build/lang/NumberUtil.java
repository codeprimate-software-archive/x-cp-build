/*
 * NumberUtil.java (c) 29 November 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.11.29
 */

package com.cp.build.lang;

public final class NumberUtil {

  public static Integer valueAsInteger(final String value) {
    return (StringUtil.isEmpty(value) ? null : Integer.valueOf(value));
  }

}
