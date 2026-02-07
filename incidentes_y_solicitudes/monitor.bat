@echo off
REM Monitoreo de servicios

cls
echo.
echo ========================================
echo   MONITOREO DE SERVICIOS DOCKER
echo ========================================
echo.

:loop
echo [%date% %time%] Verificando servicios...
echo.

docker ps

echo.
echo Logs del Backend (últimas 5 líneas):
docker-compose logs --tail=5 backend 2>nul || echo "Backend aún iniciando..."

echo.
echo Logs del Frontend (últimas 5 líneas):
docker-compose logs --tail=5 frontend 2>nul || echo "Frontend aún iniciando..."

echo.
echo Logs de PostgreSQL (últimas 5 líneas):
docker-compose logs --tail=5 postgres 2>nul || echo "PostgreSQL iniciado"

echo.
echo Presiona Ctrl+C para salir o espera para siguiente actualización...
timeout /t 30

cls
goto loop

