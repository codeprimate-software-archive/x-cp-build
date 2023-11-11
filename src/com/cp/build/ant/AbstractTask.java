/*
 * AbstractTask.java (c) 29 November 2009
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.7.12
 * @see org.apache.tools.ant.PropertyHelper
 * @see org.apache.tools.ant.Task
 */

package com.cp.build.ant;

import com.cp.build.lang.Assert;
import com.cp.build.lang.ObjectUtil;
import com.cp.build.lang.StringUtil;
import org.apache.tools.ant.PropertyHelper;

public abstract class AbstractTask extends org.apache.tools.ant.Task {

  private PropertyHelper propertyHelper;

  protected PropertyHelper getPropertyHelper() {
    if (ObjectUtil.isNull(propertyHelper)) {
      propertyHelper = PropertyHelper.getPropertyHelper(getProject());
    }

    return propertyHelper;
  }

  protected Object getPropertyValue(final String propertyName) {
    if (StringUtil.isNotEmpty(propertyName)) {
      final String resolvedProperty = getPropertyHelper().replaceProperties(null, propertyName, null);
      return getPropertyHelper().getProperty(resolvedProperty);
    }

    return null;
  }

  protected boolean setPropertyValue(final String propertyName, final Object propertyValue) {
    if (ObjectUtil.isNotNull(propertyValue)) {
      Assert.notEmpty(propertyName, "The name of the property must be specified!");
      return getPropertyHelper().setProperty(propertyName, propertyValue, false);
    }

    return false;
  }

  protected String replaceProperties(final String value) {
    return getPropertyHelper().replaceProperties(null, value, null);
  }

}
