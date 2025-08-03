/*
 * SubstringTask.java (c) 29 November 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.11.29
 * @see com.cp.build.ant.AbstractTask
 */

package com.cp.build.ant;

import com.cp.build.lang.Assert;
import com.cp.build.lang.NumberUtil;
import com.cp.build.lang.ObjectUtil;
import com.cp.build.lang.StringUtil;
import org.apache.tools.ant.BuildException;

public class SubstringTask extends AbstractTask {

  private static final Integer DEFAULT_BEGIN_INDEX = 0;
  private static final Integer DEFAULT_END_INDEX = -1;

  private String beginIndex;
  private String endIndex;
  private String property;
  private String value;

  public String getBeginIndex() {
    return beginIndex;
  }

  public void setBeginIndex(final String beginIndex) {
    this.beginIndex = beginIndex;
  }

  public String getEndIndex() {
    return endIndex;
  }

  public void setEndIndex(final String endIndex) {
    this.endIndex = endIndex;
  }

  public String getProperty() {
    Assert.state(StringUtil.isNotEmpty(property), "The property was not properly specified!");
    return property;
  }

  public void setProperty(final String property) {
    Assert.notEmpty(property, "The property cannot be null or empty!");
    this.property = property;
  }

  public String getValue() {
    Assert.state(ObjectUtil.isNotNull(value), "The value was not properly specified!");
    return value;
  }

  public void setValue(final String value) {
    Assert.notNull(value, "The value cannot be null!");
    this.value = value;
  }

  protected String substring() {
    final String valueAsString = replaceProperties(getValue());

    final int beginIndex = ObjectUtil.getDefaultValue(NumberUtil.valueAsInteger(
      ObjectUtil.toString(replaceProperties(getBeginIndex()))), DEFAULT_BEGIN_INDEX);

    if (StringUtil.isNotEmpty(getEndIndex())) {
      final int endIndex = ObjectUtil.getDefaultValue(NumberUtil.valueAsInteger(
        ObjectUtil.toString(replaceProperties(getEndIndex()))), DEFAULT_END_INDEX);
      return valueAsString.substring(beginIndex, endIndex);
    }

    return valueAsString.substring(beginIndex);
  }

  @Override
  public void execute() throws BuildException {
    super.execute();
    setPropertyValue(getProperty(), substring());
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{property = ");
    buffer.append(getProperty());
    buffer.append(", value = ").append(getValue());
    buffer.append(", beginIndex = ").append(getBeginIndex());
    buffer.append(", endIndex = ").append(getEndIndex());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
