@ECHO OFF
REM // %1 = Working Directory

SETLOCAL EnableDelayedExpansion

REM // ********* VS2015 Settings ********* //
SET WorkingRoot=%~1
SET BuildType=rebuild
SET Rel_Platform="release|Any CPU"
SET DevenvDirectory=C:\Program Files (x86)\Microsoft Visual Studio 14.0\Common7\IDE
SET ProjectWorkspace="%WorkingRoot%workspace\xxx\CAS-codescan\cas-fortify\dev\cas-xxx\src\xxx\delegator.sln"
SET ProjectWorkspaceT="D:\ci-jenkins\workspace\xxx\CAS-codescan\cas-fortify\dev\cas-xxx\src\xxx\delegator.sln"


REM // ********* Security Settings ********* //
SET Build_ID=cas-xxx


REM // ********* Clean & Init ********* //
ECHO "%VS140COMNTOOLS%vsvars32.bat"
call "%VS140COMNTOOLS%vsvars32.bat"

REM // ********* Translate ********* //
ECHO "sourceanalyzer" -b %Build_ID%  "%DevenvDirectory%\devenv" %ProjectWorkspaceT% /%BuildType% %Rel_Platform%
"sourceanalyzer" -b %Build_ID% "%DevenvDirectory%\devenv" %ProjectWorkspaceT% /%BuildType% %Rel_Platform%

ENDLOCAL
EXIT /B

EXIT %ERRORLEVEL%
