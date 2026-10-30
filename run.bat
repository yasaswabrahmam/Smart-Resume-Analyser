@echo off
echo ========================================================
echo Starting Smart AI Resume Analyzer (Spring Boot + MySQL)
echo ========================================================

if "%JAVA_HOME%"=="" (
    set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot
)

set PATH=%JAVA_HOME%\bin;C:\Users\yasas\maven\apache-maven-3.9.6\bin;%PATH%

echo Java Home: %JAVA_HOME%
echo Running application on http://localhost:8085 ...
mvn spring-boot:run
