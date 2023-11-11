#!/bin/bash
# cp-build.sh shell script file used to setup the user's environment and run Ant.

# Determine the operating system environment (OS)...

basedirpath=`pwd -P`
cygwin=false;
darwin=false;
linux=false;
unix=false;

case "`uname`" in
  CYGWIN*)
    cygwin=true
    basedirpath=`cygpath -am .`
    ;;
  Darwin*)
    darwin=true
    JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Home
    ;;
  Linux*)
    linux=true
    ;;
  Unix*)
    unix=true
    ;;
esac

if [ -f ./cp-check_env.sh ]
then
  . ./cp-check_env.sh
else
  echo "WARNING... failed to determine if the user''s ($USER) environment was properly configured!"
fi

USER_PROJECTS_FILE=./etc/config/$USER-projects.xml

if [ ! -f $USER_PROJECTS_FILE ]
then
  echo "Please create a user projects configuration file ($USER-projects.xml) in the ./etc/config directory of the cp Ant build & deployment management system (BDMS)!"
  exit -5
fi

export PROJECT_NAME=$1

if [ -z "$PROJECT_NAME" ]
then
  echo "Please specify the project name as a command-line argument to this Bash shell script file ($0)!"
  exit -6
fi

echo "Project: "$PROJECT_NAME

# local and environment variables
ANT_BUILD_FILE=./etc/cp-build.xml
ANT_CMD="$ANT_HOME/bin/ant --noconfig"
export ANT_TARGET=$2

if [ -z "$ANT_TARGET" ]
then
  export ANT_TARGET=help
fi

# Get the fully-qualified Java class name to launch (run).
if [ "$ANT_TARGET" == "run" ]
then
  export RUN_CLASS_NAME=$3

  # Validate the name of the Java class file.
  if [ -z "$RUN_CLASS_NAME" ]
  then
    echo "$RUN_CLASS_NAME is not the name of a fully-qualified Java class name to run."
    echo "Please specify a fully-qualified class name to run!"
    exit -7
  fi

  # name of Bash shell script file
  shift
  # project name
  shift
  # target
  shift
  # fully-qualified Java class name to run
  shift

  for arg in "$@"
  do
    COMMAND_LINE_ARGS=$COMMAND_LINE_ARGS" "$arg
  done

  #echo "command-line args ($COMMAND_LINE_ARGS)"
  export COMMAND_LINE_ARGS
fi

# Get the name of the JUnit test case class if it exists and set the TEST_CLASS_NAME environment variable.
if [ "$ANT_TARGET" == "test" -a -n "$3" ]
then
  export TEST_CLASS_NAME=$3
else
  export TEST_CLASS_NAME=" "
fi

# Get the name of the test method (actual test case) to run from the JUnit test case class if it exists
# and set the TEST_METHOD_NAME environment variable.
if [ "$ANT_TARGET" == "test" -a -n "$TEST_CLASS_NAME" -a -n "$4" ]
then
  export TEST_METHOD_NAME=$4
else
  export TEST_METHOD_NAME=" "
fi

# Initialize the cp Ant build and deployment tool logging.
LOGGING_PROPERTIES=file://`pwd -P`/etc/config/logging.properties

if [ -f ./etc/config/$USER-log4j.xml ]
then
  #echo "Using user-defined log4j configuration file ($USER-log4j.xml)!"
  LOG4J_CONFIGURATION=file:///$basedirpath/etc/config/$USER-log4j.xml
else
  #echo "Using default log4j configuration file (default-log4j.xml)!"
  LOG4J_CONFIGURATION=file:///$basedirpath/etc/config/default-log4j.xml
fi

export ANT_ARGS=$ANT_ARGS" -lib ./lib/cp-build.jar -lib ./lib/commons-logging-1.1.1.jar -lib ./lib/log4j-1.2.16.jar \
  -logger com.cp.build.ant.Log4jBuildLogger"

export ANT_OPTS=$ANT_OPTS" -Dbasedir=$basedirpath -Dargs.user=$USER  -Dargs.host=$HOST -Duser.projects.file=$USER_PROJECTS_FILE \
  -Djava.util.logging.config.file=$LOGGING_PROPERTIES -Dlog4j.configuration=$LOG4J_CONFIGURATION -Dlog4j.debug=false -Dcp-build.debug=false"

echo "Anttarget: "$ANT_TARGET

# Run Ant command
# $ANT_CMD -buildfile $ANT_BUILD_FILE -lib ./lib/cp-build.jar -lib ./lib/ant-contrib-1.0b3.jar -lib ./lib/commons-logging.jar -lib ./lib/log4j-1.2.16.jar \
#  -listener org.apache.tools.ant.listener.Log4jListener -logger com.cp.build.ant.DefaultBuildLogger \
#  $ANT_TARGET

$ANT_CMD -buildfile $ANT_BUILD_FILE $ANT_TARGET
