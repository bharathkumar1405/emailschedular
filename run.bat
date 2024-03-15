@echo off

rem Get the directory of the batch script
set "SCRIPT_DIR=%~dp0"

rem Set the path to the JDK folder
set "JDK_PATH=%SCRIPT_DIR%jdk-17"

rem Set the path to the emailSchedular.jar file
set "APP_JAR=%SCRIPT_DIR%emailSchedular.jar"

rem Check if emailSchedular.jar exists
if not exist "%APP_JAR%" (
    echo emailSchedular.jar not found in the same folder as the script.
    echo Please place the emailSchedular.jar file in the same folder as the script.
    pause
    exit /b 1
)
rem Open Google Chrome
start chrome http://localhost

rem Run the emailSchedular.jar file using Java
"%JDK_PATH%\bin\java.exe" -jar "%APP_JAR%"


pause
