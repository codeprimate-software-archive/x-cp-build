rem cp-exe.bat Windows batch file to run a Codeprimate application.

@echo off
setlocal enableextensions

rem verify the Java Runtime Environment is installed and the JAVA_HOME environment variable is set.
if not exits %JAVA_HOME% goto :no_java_home

rem environment variables
set JAVA_CMD=%JAVA_HOME%\bin\java

rem run (launch) the Code Primate application
%JAVA_CMD% -jar lib\%PROJECT_NAME*.jar

:no_java_home
echo "The Java Runtime Environment (JRE) must be installed and the JAVA_HOME environment variable must be set!"

:exit
endlocal