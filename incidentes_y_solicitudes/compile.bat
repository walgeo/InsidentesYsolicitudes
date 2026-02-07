@echo off
cd C:\Users\walte\OneDrive\Documentos\PROYECTO_ALTAMIRA\incidentes_y_solicitudes
echo.
echo ===================================
echo COMPILANDO PROYECTO...
echo ===================================
echo.
call mvnw.cmd clean compile -DskipTests
echo.
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===================================
    echo COMPILACION EXITOSA!
    echo ===================================
    echo.
) else (
    echo.
    echo ===================================
    echo ERROR EN LA COMPILACION
    echo ===================================
    echo.
)
pause

