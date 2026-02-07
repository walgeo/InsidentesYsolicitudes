@echo off
REM Script para ejecutar el proyecto completo con Docker Compose
REM Uso: .\run.bat

echo.
echo ========================================
echo Incidentes y Solicitudes - Docker Setup
echo ========================================
echo.

REM Verificar si Docker está instalado
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker no está instalado o no está en el PATH
    echo Descarga Docker desde: https://www.docker.com/products/docker-desktop
    exit /b 1
)

REM Verificar si Docker Compose está instalado
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker Compose no está instalado
    exit /b 1
)

echo ✓ Docker y Docker Compose detectados
echo.
echo Iniciando servicios...
echo.

REM Ejecutar docker-compose
docker-compose up -d

REM Esperar a que los servicios estén listos
echo.
echo Esperando a que los servicios se inicien...
timeout /t 15 /nobreak

echo.
echo ========================================
echo ✓ Servicios iniciados correctamente!
echo ========================================
echo.
echo URLs disponibles:
echo   Frontend:  http://localhost:4200
echo   Backend:   http://localhost:8081
echo   Database:  localhost:5432
echo.
echo Para detener los servicios, ejecuta:
echo   docker-compose down
echo.
echo Para ver los logs:
echo   docker-compose logs -f
echo.

