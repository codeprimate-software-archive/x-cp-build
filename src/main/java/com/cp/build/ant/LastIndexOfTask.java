/*
 * LastIndexOfTask.java (c) 29 November 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.11.29
 * @see com.cp.build.ant.AbstractTask
 */

package com.cp.build.ant;

import com.cp.build.lang.Assert;
import com.cp.build.lang.ObjectUtil;
import com.cp.build.lang.StringUtil;
import org.apache.tools.ant.BuildException;

public class LastIndexOfTask extends AbstractTask {

  private String property;
  private String token;
  private String value;

  public String getProperty() {
    Assert.state(StringUtil.isNotEmpty(property), "The property was not properly specified!");
    return property;
  }

  public void setProperty(final String property) {
    Assert.notEmpty(property, "The property cannot be null or empty!");
    this.property = property;
  }

  public String getToken() {
    Assert.state(StringUtil.isNotEmpty(token), "The token was not properly specified!");
    return token;
  }

  public void setToken(final String token) {
    Assert.notEmpty(token, "The token cannot be null or empty!");
    this.token = token;
  }

  public String getValue() {
    Assert.state(ObjectUtil.isNotNull(value), "The value was not properly specified!");
    return value;
  }

  public void setValue(final String value) {
    Assert.notNull(value, "The String value cannot be null!");
    this.value = value;
  }

  protected int lastIndexOf() {
    return replaceProperties(getValue()).lastIndexOf(getToken());
  }

  @Override
  public void execute() throws BuildException {
    super.execute();
    setPropertyValue(getProperty(), lastIndexOf());
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{property = ");
    buffer.append(getProperty());
    buffer.append(", token = ").append(getToken());
    buffer.append(", value = ").append(getValue());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
