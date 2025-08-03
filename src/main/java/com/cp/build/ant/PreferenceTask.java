/*
 * PreferenceTask.java (c) 27 October 2009
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

public class PreferenceTask extends AbstractTask {

  private String defaultValue;
  private String globalProperty;
  private String localProperty;
  private String name;

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(final String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getGlobalProperty() {
    return globalProperty;
  }

  public void setGlobalProperty(final String globalProperty) {
    this.globalProperty = globalProperty;
  }

  public String getLocalProperty() {
    return localProperty;
  }

  public void setLocalProperty(final String localProperty) {
    this.localProperty = localProperty;
  }

  public String getName() {
    Assert.state(StringUtil.isNotEmpty(name), "The name of the Preference was not properly specified!");
    return name;
  }

  public void setName(final String name) {
    Assert.notEmpty(name, "The name of the Preference cannot be null or empty!");
    this.name = name;
  }

  protected Object getPreferenceValue() {
    Object preferenceValue = getPropertyValue(getLocalProperty());
    preferenceValue = (ObjectUtil.isNotNull(preferenceValue) ? preferenceValue
      : getPropertyValue(getGlobalProperty()));
    preferenceValue = (ObjectUtil.isNotNull(preferenceValue) ? preferenceValue
      : getDefaultValue());
    return preferenceValue;
  }

  @Override
  public void execute() throws BuildException {
    super.execute();

    Object preferenceValue = getPreferenceValue();

    if (ObjectUtil.isNotNull(preferenceValue)) {
      replaceProperties(ObjectUtil.toString(preferenceValue));
      setPropertyValue(getName(), preferenceValue);
    }
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{default value = ");
    buffer.append(getDefaultValue());
    buffer.append(", global property = ").append(getGlobalProperty());
    buffer.append(", local property = ").append(getLocalProperty());
    buffer.append(", name = ").append(getName());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
