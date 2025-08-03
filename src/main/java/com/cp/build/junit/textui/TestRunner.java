/*
 * TestRunner.java (c) 14 June 2009
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.7.12
 * @see junit.textui.TestRunner
 */

package com.cp.build.junit.textui;

import java.lang.reflect.Constructor;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.runner.Version;

public class TestRunner extends junit.textui.TestRunner {

  @Override
  protected TestResult start(final String[] args) throws Exception {
    boolean wait = false;

    String testCase = null;
    String testName = null;

    // parse the command-line arguments
    for (int index = 0; index < args.length; index++) {
      if ("-c".equals(args[index])) {
        testCase = extractClassName(args[++index]);
      }
      else if ("-v".equals(args[index])) {
        System.err.println("JUnit " + Version.id() + " by Kent Beck and Erich Gamma");
      }
      else if ("-wait".equals(args[index])) {
        wait = true;
      }
      else if (testCase == null) {
        testCase = args[index];
      }
      else {
        testName = args[index];
      }
    }

    //System.out.println("test case (" + testCase + ")");
    //System.out.println("test name (" + testName + ")");

    // assert the testCase is not null or an empty String
    if (testCase == null || "".equals(testCase.trim())) {
      throw new IllegalArgumentException("Usage: TestRunner [-wait] testCaseName, where name is the name of the TestCase class");
    }

    try {
      return doRun(getTest(testCase, testName), wait);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      throw new Exception("Could not create and run test suite: " + e);
    }
  }

  protected Test getTest(final String testCase, final String testName) {
    Class<Test> testCaseClass = null;

    try {
      testCaseClass = loadSuiteClass(testCase);
      Assert.assertNotNull("Class not found for test case (" + testCase + ")!", testCaseClass);
    }
    catch (ClassNotFoundException e) {
      String testCaseName = e.getMessage();
      testCaseName = (testCaseName != null ? testCaseName : testCase);
      runFailed("Class not found: " + testCaseName);
      return null;
    }
    catch (Exception e) {
      runFailed("Error: " + e.toString());
      return null;
    }

    Test theTest = null;

    if (TestCase.class.isAssignableFrom(testCaseClass)) {
      if (testName != null && !"".equals(testName.trim())) {
        try {
          final Constructor<Test> testCaseClassConstructor = testCaseClass.getConstructor(String.class);
          theTest = testCaseClassConstructor.newInstance(testName);
        }
        catch (Exception e) {
          runFailed("Error: " + e.getMessage());
          theTest = getTest(testCase);
        }
      }
      else {
        theTest = getTest(testCase);
      }
    }

    clearStatus();

    return theTest;
  }

  public static void main(final String[] args) {
    final TestRunner testRunner = new TestRunner();
    try {
      final TestResult testResult = testRunner.start(args);

      if (testResult.wasSuccessful()) {
        System.exit(SUCCESS_EXIT);
      }
      else {
        System.exit(FAILURE_EXIT);
      }
    }
    catch (Exception e) {
      System.err.println(e.getMessage());
      System.exit(EXCEPTION_EXIT);
    }
  }

}
