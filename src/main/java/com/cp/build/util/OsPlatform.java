/*
 * OsPlatform.java (c) 12 December 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.12.12
 */

package com.cp.build.util;

import com.cp.build.lang.StringUtil;

public enum OsPlatform {
  WINDOWS(1, "windows", "Microsoft Windows"),
  UNIX(2, "unix", "UNIX");

  private Integer id;

  private String code;
  private String description;

  OsPlatform(final Integer id, final String code, final String description) {
    this.id = id;
    this.code = code;
    this.description = description;
  }

  public static OsPlatform getByCode(final String code) {
    for (final OsPlatform value : values()) {
      if (StringUtil.indexOf(code, value.getCode()) != -1) {
        return value;
      }
    }

    return null;
  }

  public static OsPlatform getById(final Integer id) {
    for (final OsPlatform value : values()) {
      if (value.getId().equals(id)) {
        return value;
      }
    }

    return null;
  }

  public Integer getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return getDescription();
  }

}
