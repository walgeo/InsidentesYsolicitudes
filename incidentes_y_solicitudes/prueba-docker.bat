@echo off
REM Script para probar Docker Compose
REM Ejecutar este archivo para verificar que todo funciona

echo.
echo ========================================
echo   PRUEBA DE DOCKER - Incidentes
echo ========================================
echo.

REM 1. Verificar Docker
echo [1/6] Verificando Docker...
docker --version
if %errorlevel% neq 0 (
    echo ERROR: Docker no está instalado
    pause
    exit /b 1
)
echo ✓ Docker OK
echo.

REM 2. Verificar Docker Compose
echo [2/6] Verificando Docker Compose...
docker-compose --version
if %errorlevel% neq 0 (
    echo ERROR: Docker Compose no está instalado
    pause
    exit /b 1
)
echo ✓ Docker Compose OK
echo.

REM 3. Detener servicios previos
echo [3/6] Deteniendo servicios anteriores...
docker-compose down 2>nul
echo ✓ Limpiar anterior OK
echo.

REM 4. Iniciar servicios
echo [4/6] Iniciando servicios (esto tarda 2-3 minutos)...
docker-compose up -d
if %errorlevel% neq 0 (
    echo ERROR: No se pudieron iniciar los servicios
    docker-compose logs
    pause
    exit /b 1
)
echo ✓ Servicios iniciados
echo.

REM 5. Esperar a que se inicialice
echo [5/6] Esperando a que se inicialicen (30 segundos)...
timeout /t 30 /nobreak
echo ✓ Inicialización completada
echo.

REM 6. Verificar servicios
echo [6/6] Verificando servicios...
echo.
echo CONTENEDORES:
docker ps

echo.
echo LOGS:
docker-compose logs --tail=20

echo.
echo ========================================
echo ✓ PRUEBA COMPLETADA!
echo ========================================
echo.
echo Acceso:
echo   Frontend:  http://localhost:4200
echo   Backend:   http://localhost:8081
echo   Database:  localhost:5432
echo.
echo Comandos útiles:
echo   Ver logs:        docker-compose logs -f backend
echo   Detener:         docker-compose down
echo   Reiniciar:       docker-compose restart
echo.
pause

