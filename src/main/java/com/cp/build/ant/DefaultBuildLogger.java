/*
 * DefaultBuildLogger.java (c) 8 June 2008
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.6.15
 * @see org.apache.tools.ant.DefaultBuildLogger
 */

package com.cp.build.ant;

import java.io.PrintStream;

public class DefaultBuildLogger extends org.apache.tools.ant.DefaultLogger {

  @Override
  protected void printMessage(final String message, final PrintStream stream, final int priority) {
    // no implementation!
  }

  @Override
  protected void log(final String message) {
    // no implementation!
  }

}
