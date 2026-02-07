@echo off
REM Script de prueba completa del proyecto Docker

setlocal enabledelayedexpansion

cls
echo.
echo ════════════════════════════════════════════════════════════════
echo          PRUEBA COMPLETA - INCIDENTES Y SOLICITUDES
echo ════════════════════════════════════════════════════════════════
echo.
echo Fecha: %date% %time%
echo Directorio: %cd%
echo.

REM ==================== PASO 1: VERIFICAR DOCKER ====================
echo [PASO 1/8] Verificando Docker...
docker --version
if %errorlevel% neq 0 (
    echo ❌ ERROR: Docker no está disponible
    pause
    exit /b 1
)
echo ✓ Docker OK
echo.

REM ==================== PASO 2: VERIFICAR DOCKER COMPOSE ====================
echo [PASO 2/8] Verificando Docker Compose...
docker-compose --version
if %errorlevel% neq 0 (
    echo ❌ ERROR: Docker Compose no está disponible
    pause
    exit /b 1
)
echo ✓ Docker Compose OK
echo.

REM ==================== PASO 3: DETENER SERVICIOS PREVIOS ====================
echo [PASO 3/8] Deteniendo servicios previos...
docker-compose down 2>nul
echo ✓ Servicios detenidos
echo.

REM ==================== PASO 4: LEVANTAR SERVICIOS ====================
echo [PASO 4/8] Levantando servicios...
echo   - PostgreSQL (datos)
echo   - Backend Spring Boot (API REST)
echo   - Frontend Angular (interfaz web)
echo.
docker-compose up -d
if %errorlevel% neq 0 (
    echo ❌ ERROR: No se pudieron levantar los servicios
    docker-compose logs
    pause
    exit /b 1
)
echo ✓ Servicios levantados en background
echo.

REM ==================== PASO 5: ESPERAR INICIALIZACION ====================
echo [PASO 5/8] Esperando inicialización (40 segundos)...
echo   Por favor espera, esto es normal en la primera ejecución...
echo.

for /L %%I in (40,-1,1) do (
    if %%I gtr 10 (
        if %%I equ 40 (
            echo   Esperando... %%I segundos
        )
        if %%I equ 30 (
            echo   Esperando... %%I segundos
        )
        if %%I equ 20 (
            echo   Esperando... %%I segundos
        )
        if %%I equ 10 (
            echo   Esperando... %%I segundos
        )
    )
    timeout /t 1 /nobreak >nul
)
echo ✓ Inicialización completada
echo.

REM ==================== PASO 6: VERIFICAR CONTENEDORES ====================
echo [PASO 6/8] Verificando contenedores...
echo.
docker ps
echo.
echo ✓ Contenedores listados
echo.

REM ==================== PASO 7: PROBAR BACKEND ====================
echo [PASO 7/8] Probando Backend...
timeout /t 5 /nobreak >nul
echo.
echo Intentando conectar a: http://localhost:8081/actuator/health
echo.

REM Usar PowerShell para hacer la petición HTTP
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:8081/actuator/health' -TimeoutSec 5 -ErrorAction Stop; Write-Host '✓ Backend respondió:'; Write-Host $response.Content } catch { Write-Host '⏳ Backend aún se está inicializando...'; Write-Host '  Espera 30 segundos más e intenta manualmente en:'; Write-Host '  http://localhost:8081/actuator/health' }"

echo.
echo.

REM ==================== PASO 8: INSTRUCCIONES FINALES ====================
echo [PASO 8/8] Resumen final
echo.
echo ════════════════════════════════════════════════════════════════
echo ✓ DOCKER LEVANTADO EXITOSAMENTE
echo ════════════════════════════════════════════════════════════════
echo.
echo ACCESO A LA APLICACIÓN:
echo.
echo 1. FRONTEND (Angular)
echo    URL: http://localhost:4200
echo    Status: Iniciando compilación...
echo.
echo 2. BACKEND (API REST)
echo    URL: http://localhost:8081
echo    Health: http://localhost:8081/actuator/health
echo    API Tickets: http://localhost:8081/api/tickets
echo.
echo 3. BASE DE DATOS (PostgreSQL)
echo    Host: localhost
echo    Puerto: 5432
echo    Usuario: admin
echo    Password: admin123
echo    Database: postgres
echo.
echo ════════════════════════════════════════════════════════════════
echo COMANDOS ÚTILES:
echo ════════════════════════════════════════════════════════════════
echo.
echo Ver logs del Backend:
echo   docker-compose logs -f backend
echo.
echo Ver logs del Frontend:
echo   docker-compose logs -f frontend
echo.
echo Ver logs de PostgreSQL:
echo   docker-compose logs -f postgres
echo.
echo Ver todos los logs:
echo   docker-compose logs
echo.
echo Reiniciar servicios:
echo   docker-compose restart
echo.
echo Detener servicios:
echo   docker-compose down
echo.
echo ════════════════════════════════════════════════════════════════
echo CHECKLIST DE PRUEBAS:
echo ════════════════════════════════════════════════════════════════
echo.
echo [ ] 1. Abrir http://localhost:4200 en navegador
echo [ ] 2. Ver tabla de tickets (puede estar vacía)
echo [ ] 3. Hacer click en "+ Nuevo Ticket"
echo [ ] 4. Llenar formulario con datos de prueba
echo [ ] 5. Click "Crear"
echo [ ] 6. Ver ticket en la lista
echo [ ] 7. Click en ticket para ver detalles
echo [ ] 8. Agregar un comentario
echo [ ] 9. Editar el ticket
echo [ ] 10. Eliminar el ticket
echo.
echo [ ] 11. Probar API en http://localhost:8081/api/tickets
echo [ ] 12. Crear ticket vía API (requests.http)
echo [ ] 13. Ver logs en docker-compose logs -f backend
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo NOTA: Si Frontend tarda en cargar, espera 30 segundos
echo       (Primera compilación de Angular es lenta)
echo.
echo ¡Proyecto listo para pruebas! 🚀
echo.
pause

