/*
 * GetFullyQualifiedClassNameTask.java (c) 12 December 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.12.12
 * @see com.cp.build.ant.AbstractTask
 * @see java.io.File
 * @see org.apache.tools.ant.util.FileUtils
 */

package com.cp.build.ant;

import com.cp.build.lang.Assert;
import com.cp.build.lang.ObjectUtil;
import com.cp.build.lang.StringUtil;
import com.cp.build.util.OsPlatform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

public class GetFullyQualifiedClassNameTask extends AbstractTask {

  protected static final Map<OsPlatform, Replacement> REPLACEMENT_MAP = new HashMap<OsPlatform, Replacement>(3);

  static {
    REPLACEMENT_MAP.put(OsPlatform.WINDOWS, new Replacement("\\", "\\\\"));
    REPLACEMENT_MAP.put(OsPlatform.UNIX, new Replacement("\\", "/"));
  }

  private String filepath;
  private String property;
  private String targetos = System.getProperty("os.name").toLowerCase();

  public String getFilepath() {
    Assert.state(StringUtil.isNotEmpty(filepath), "The fully-qualifed file path was not properly initialized!");
    return filepath;
  }

  public void setFilepath(final String filepath) {
    Assert.notEmpty(filepath, "The fully-qualified file path cannot be null or empty!");
    this.filepath = filepath;
  }

  public String getProperty() {
    Assert.state(StringUtil.isNotEmpty(property), "The name of the property was not properly initialized!");
    return property;
  }

  public void setProperty(final String property) {
    Assert.notEmpty(property, "The name of the property cannot be null or empty!");
    this.property = property;
  }

  public String getTargetos() {
    return ObjectUtil.getDefaultValue(targetos, System.getProperty("os.name").toLowerCase());
  }

  public void setTargetos(final String targetos) {
    this.targetos = targetos;
  }

  protected OsPlatform getTargetOsPlatform() {
    return OsPlatform.getByCode(getTargetos());
  }

  protected File asFile() {
    return new File(convertFilepath(replaceProperties(getFilepath())));
  }

  protected String convertFilepath(final String filepath) {
    final Replacement replacement = REPLACEMENT_MAP.get(getTargetOsPlatform());

    if (ObjectUtil.isNotNull(replacement)) {
      return replacement.replace(filepath);
    }

    throw new BuildException("The token os platform (" + getTargetos() + ") is not valid!");
  }

  public void execute() throws BuildException {
    super.execute();

    final File sourceFile = asFile();
    System.out.println("source file (" + sourceFile + ")");

    try {
      if (sourceFile.getCanonicalFile().exists()) {
        final BufferedReader sourceFileReader = new BufferedReader(new InputStreamReader(
          new FileInputStream(sourceFile.getCanonicalFile())));
        final String sourceFileContent = FileUtils.readFully(sourceFileReader);

        final int indexOfPackageDeclaration = sourceFileContent.indexOf("package");

        final String packageName = sourceFileContent.substring(indexOfPackageDeclaration + "package".length(),
          sourceFileContent.indexOf(";", indexOfPackageDeclaration)).trim();

        String className = sourceFile.getName();
        className = className.substring(0, className.indexOf("."));

        final StringBuffer buffer = new StringBuffer(packageName);
        buffer.append(".");
        buffer.append(className);

        sourceFileReader.close();
        setPropertyValue(getProperty(), buffer.toString());
      }
      else {
        throw new BuildException("The source file (" + sourceFile + ") could not be found in the file system!");
      }
    }
    catch (IOException e) {
      throw new BuildException(e.getMessage(), e);
    }
  }

  public static void main(final String... args) {
    final GetFullyQualifiedClassNameTask task = new GetFullyQualifiedClassNameTask();
    task.setFilepath("password");
    task.setProperty("myProperty");
    task.execute();
    System.out.println(task.getPropertyValue("myProperty"));
  }

  protected static final class Replacement {

    private final String replacement;
    private final String token;

    public Replacement(final String token, final String replacement) {
      Assert.notEmpty(token, "The token cannot be null or empty!");
      Assert.notNull(replacement, "The replacement cannot be null!");
      this.replacement = replacement;
      this.token = token;
    }

    public String getReplacement() {
      return replacement;
    }

    public String getToken() {
      return token;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof Replacement)) {
        return false;
      }

      final Replacement that = (Replacement) obj;

      return ObjectUtil.equals(getReplacement(), that.getReplacement())
        && ObjectUtil.equals(getToken(), that.getToken());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getReplacement());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getToken());
      return hashValue;
    }

    public String replace(final String value) {
      //return value.replaceAll(getToken(), getReplacement());
      final StringBuffer buffer = new StringBuffer();

      int currentIndex = -1;
      int offset = 0;
      final int tokenLength = getToken().length();

      while ((currentIndex = value.indexOf(getToken(), offset)) != -1) {
        buffer.append(value.substring(offset, currentIndex));
        buffer.append(getReplacement());
        offset = (currentIndex + tokenLength);
      }

      buffer.append(value.substring(offset));

      return buffer.toString();
    }

    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{replacement = ");
      buffer.append(getReplacement());
      buffer.append(", token = ").append(getToken());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
