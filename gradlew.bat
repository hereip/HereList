@echo off
setlocal

set "GRADLE_VERSION=8.13"
set "BOOTSTRAP_DIR=%USERPROFILE%\.gradle\herelist-bootstrap"
set "GRADLE_EXE=%BOOTSTRAP_DIR%\gradle-%GRADLE_VERSION%\bin\gradle.bat"

if not exist "%GRADLE_EXE%" (
    if not exist "%BOOTSTRAP_DIR%" mkdir "%BOOTSTRAP_DIR%"
    echo Gradle %GRADLE_VERSION% is not installed. Downloading it now...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference = 'Stop'; $archive = Join-Path $env:TEMP 'gradle-8.13-bin.zip'; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-8.13-bin.zip' -OutFile $archive; Expand-Archive -LiteralPath $archive -DestinationPath '%BOOTSTRAP_DIR%' -Force; Remove-Item $archive"
    if errorlevel 1 exit /b 1
)

call "%GRADLE_EXE%" %*
exit /b %ERRORLEVEL%
