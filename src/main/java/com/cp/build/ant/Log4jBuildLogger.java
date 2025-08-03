/*
 * Log4jBuildLogger.java (c) 26 June 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John Blum
 * @version 2010.7.12
 * @see org.apache.tools.ant.BuildLogger
 */

package com.cp.build.ant;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;

public class Log4jBuildLogger implements BuildLogger {

  private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy hh:mm.ss a");

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  private long buildEndTime;
  private long buildStartTime;

  private PrintStream err;
  private PrintStream out;

  public Log4jBuildLogger() {
    final Logger logger = Logger.getLogger("org.apache.tools.ant");

    log("org.apache.tools.ant logger level (" + logger.getLevel() + ")");

    final Logger rootLogger = Logger.getRootLogger();

    log("root logger level (" + rootLogger.getLevel() + ")");

    if (rootLogger.getAllAppenders() instanceof NullEnumeration) {
      logger.fatal("Failed to initialize Log4j logging using log configuration file ("
        + System.getProperty("log4j.configuration") + ")");
    }
    else {
      logger.fatal("Initialized Log4j using log configuration file ("
        + System.getProperty("log4j.configuration") + ")");
    }
  }

  protected boolean isDebug() {
    return Boolean.getBoolean("cp-build.debug");
  }

  public void setEmacsMode(final boolean emacsMode) {
    // no op!
  }

  public void setErrorPrintStream(final PrintStream err) {
    this.err = err;
  }

  public void setMessageOutputLevel(final int level) {
    // no op!
  }

  public void setOutputPrintStream(final PrintStream output) {
    this.out = output;
  }

  public void buildStarted(final BuildEvent event) {
    final Logger logger = Logger.getLogger(Project.class);
    final String buildStartedDateTime = DATE_FORMATTER.format(Calendar.getInstance().getTime());
    logger.info("Build started @ (" + buildStartedDateTime + ")...");
    log("Build started @ (" + buildStartedDateTime + ")...");
    buildStartTime = System.currentTimeMillis();
  }

  public void buildFinished(final BuildEvent event) {
    buildEndTime = System.currentTimeMillis();

    final Logger logger = Logger.getLogger(Project.class);

    if (event.getException() == null) {
      logger.error("Build Successful!");
    }
    else {
      logger.error("Build Failed!");

      final StringWriter strWriter = new StringWriter(1024);
      final PrintWriter writer = new PrintWriter(strWriter, true);
      event.getException().printStackTrace(writer);
      writer.flush();
      writer.close();

      logger.error(LINE_SEPARATOR);
      logger.error(strWriter.toString());
      logger.error(LINE_SEPARATOR);
    }

    final String buildTimeInMinutes = getTimeInMinutes(buildEndTime - buildStartTime);

    logger.error("Total Build Time (" + buildTimeInMinutes + ")");
    log("Total Build Time (" + buildTimeInMinutes + ")");
  }

  public void targetStarted(final BuildEvent event) {
    final Logger logger = Logger.getLogger(Target.class);
    logger.debug("Invoking target (" + event.getTarget().getName() + ")...");
    log("Invoking target (" + event.getTarget().getName() + ")...");
  }

  public void targetFinished(final BuildEvent event) {
    final Logger logger = Logger.getLogger(Target.class);
    logger.debug("Target (" + event.getTarget().getName() + ") finished.");
    log("Target (" + event.getTarget().getName() + ") finished.");
  }

  public void taskStarted(final BuildEvent event) {
    final Logger logger = Logger.getLogger(event.getTask().getClass());
    logger.debug("Running task (" + event.getTask().getTaskName() + ")...");
  }

  public void taskFinished(final BuildEvent event) {
    final Logger logger = Logger.getLogger(event.getTask().getClass());
    logger.debug("Task (" + event.getTask().getTaskName() + ") finished.");
  }

  public void messageLogged(final BuildEvent event) {
    //log(event.getMessage());
    if (event.getTask() != null) {
      log("Task Name (" + event.getTask().getTaskName() + ") and Task Type ("
        + event.getTask().getClass().getName() + ")");
      log("Priority (" + getPriorityDescription(event.getPriority()) + ")");

      final Logger logger = Logger.getLogger(event.getTask().getClass());

      switch (event.getPriority()) {
        case Project.MSG_ERR:
          logger.error(event.getMessage());
          break;
        case Project.MSG_WARN:
          logger.warn(event.getMessage());
          break;
        case Project.MSG_INFO:
          logger.info(event.getMessage());
          break;
        case Project.MSG_VERBOSE:
        case Project.MSG_DEBUG:
          logger.debug(event.getMessage());
          break;
        default:
          logger.fatal(event.getMessage());
      }
    }
    else {
      if (event.getPriority() == Project.MSG_ERR) {
        out.println(event.getMessage());
      }
    }
  }

  private String getPriorityDescription(final int priority) {
    switch (priority) {
      case Project.MSG_DEBUG:
        return "debug";
      case Project.MSG_ERR:
        return "error";
      case Project.MSG_INFO:
        return "info";
      case Project.MSG_VERBOSE:
        return "verbose";
      case Project.MSG_WARN:
        return "warn";
      default:
        return "Unknown priority (" + priority + ")!";
    }
  }

  private String getTimeInMinutes(long elapsedTime) {
    final StringBuffer buffer = new StringBuffer();

    final long numberOfMinutes = (elapsedTime / 60000L);

    if (numberOfMinutes >= 1) {
      buffer.append(numberOfMinutes);
      buffer.append(numberOfMinutes > 1 ? " minutes, " : " minute, ");
      elapsedTime -= (numberOfMinutes * 60000);
    }
    else {
      buffer.append("0 minutes, ");
    }

    final long numberOfSeconds = (elapsedTime / 1000);

    buffer.append(numberOfSeconds);
    buffer.append(numberOfSeconds > 1 || numberOfSeconds == 0 ? " seconds" : " second");

    final long numberOfMilliseconds = (elapsedTime - (numberOfSeconds * 1000));

    buffer.append(" & ");
    buffer.append(numberOfMilliseconds);
    buffer.append(numberOfMilliseconds > 1 || numberOfMilliseconds == 0 ? " milliseconds" : " millisecond");

    return buffer.toString();
  }

  protected void log(final String message) {
    if (isDebug()) {
      System.out.println(message);
    }
  }

}
