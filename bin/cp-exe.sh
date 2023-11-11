# !/bin/bash
# cp-exe.sh Bash shell script file for running a Codeprimate application.

# environment variables
JAVA_CMD=`which java 2>&1`

echo "Running Java Runtime Environment (JRE) with Java launcher... $JAVA_CMD"

if [ ! -e "$JAVA_CMD" ]
then
  # verify the Java Runtime Environment (JRE) is installed and the JAVA_HOME environment variable is set.
  if [ -z "$JAVA_HOME" ]
  then
    echo "The Java Runtime Environment (JRE) must be installed and the JAVA_HOME environment variable must be set!"
    exit -1
  else
    JAVA_CMD=$JAVA_HOME/bin/java
  fi
fi

# run (launch) the Code Primate application
$JAVA_CMD -jar lib/%PROJECT_NAME*.jar
