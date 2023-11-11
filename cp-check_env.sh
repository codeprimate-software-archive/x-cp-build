#!/bin/bash
# Check Environment Bash shell script file for checking the user's environment setup.

# Determine the user whom invoked this shell script file.
if [ -z "$USER" ]
then
  export USER=`whoami`
  #echo "user ($USER)"

  if [ -z "$USER" ]
  then
    echo "Please set the USER environment variable!"
    exit -1
  fi
fi

# Determine the host on which this shell script file was invoked.
if [ -z "$HOST" ]
then
  export HOST=`hostname`
  #echo "host ($HOST)"

  if [ -z "$HOST" ]
  then
    echo "Please set the HOST environment variable!"
    exit -2
  fi
fi

# Check for the JAVA_HOME environment variable
if [ -z "$JAVA_HOME" ]
then
  echo "Please install the Java Development Kit (JDK) and set the JAVA_HOME environment variable!"
  exit -3
fi

# Check for the ANT_HOME environment variable.
if [ -z "$ANT_HOME" ]
then
  echo "Please install Ant and set the ANT_HOME environment variable!"
  exit -4
fi
