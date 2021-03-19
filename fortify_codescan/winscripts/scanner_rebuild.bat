@ECHO OFF
REM // %1 = Working Directory


REM // ********* VS2015 Settings ********* //
SET WorkingRoot=%~1
SET BuildType=rebuild
SET Rel_Platform="Release|x64"
SET DevenvDirectory=C:\Program Files (x86)\Microsoft Visual Studio 14.0\Common7\IDE
SET ProjectWorkspace="%WorkingRoot%workspace\TMCAS\CAS-codescan\cas-fortify\dev\cas-xx\src\xxx\buildall.sln"
SET ProjectWorkspaceT="D:\ci-jenkins\workspace\TMCAS\CAS-codescan\cas-fortify\dev\cas-xxx\src\xxx\buildall.sln"

REM // ********* Security Settings ********* //
SET Build_ID=cas-xxx


REM // ********* Clean & Init ********* //
ECHO "%VS140COMNTOOLS%\..\..\VC\vcvarsall.bat"
call "%VS140COMNTOOLS%\..\..\VC\vcvarsall.bat" amd64

REM // ********* Translate ********* //
ECHO "sourceanalyzer" -b %Build_ID%  "%DevenvDirectory%\devenv" %ProjectWorkspaceT% /%BuildType% %Rel_Platform%
"sourceanalyzer" -b %Build_ID% "%DevenvDirectory%\devenv" %ProjectWorkspaceT% /%BuildType% %Rel_Platform%


ENDLOCAL
EXIT /B

EXIT %ERRORLEVEL%
