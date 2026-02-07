@echo off
cls
echo.
echo ==========================================
echo   DIAGNOSTICO - PROBLEMA FRONTEND
echo ==========================================
echo.

echo [1/5] Ver contenedores corriendo...
docker ps
echo.

echo [2/5] Ver estado de compose...
docker-compose ps
echo.

echo [3/5] Ver logs PostgreSQL...
docker logs incidentes_y_solicitudes-postgres-1 2>&1 | findstr /C:"ready" /C:"ERROR" /C:"started" | head -5
echo.

echo [4/5] Ver logs Backend...
docker logs incidentes_y_solicitudes-backend-1 2>&1 | findstr /C:"Tomcat" /C:"ERROR" /C:"started" | head -5
echo.

echo [5/5] Ver logs Frontend...
docker logs incidentes_y_solicitudes-frontend-1 2>&1 | findstr /C:"listening" /C:"ERROR" /C:"nginx" | head -5
echo.

echo ==========================================
echo   INTENTANDO LEVANTAR FRONTEND...
echo ==========================================
echo.

docker-compose up -d frontend
timeout /t 30
docker logs incidentes_y_solicitudes-frontend-1

pause

