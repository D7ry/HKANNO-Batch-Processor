@echo off
setlocal

rem cd to here
cd /d %~dp0

rem HCT installed?
bin\hct.exe

if errorlevel 301 (
    echo Please install HCT x64.
    choice /c yn /t 5 /d n /m "Open the shared folder on Google Drive now"
    if not errorlevel 2 (
        start https://drive.google.com/open?id=0B8SgSQGjqypSMTNWSUloUWdKMEk
        exit /b
    )
)

echo -- HCT Start
for %%P in (%*) do (
    bin\hct.exe %%P -o "win32\%%~nxP"
)

echo -- HCT Finished
pause 1>nul
