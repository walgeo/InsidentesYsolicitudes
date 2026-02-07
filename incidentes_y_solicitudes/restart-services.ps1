#!/usr/bin/env pwsh

$ProjectDir = "C:\Users\walte\OneDrive\Documentos\PROYECTO_ALTAMIRA\incidentes_y_solicitudes"
Set-Location $ProjectDir

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "Reiniciando servicios..." -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan

# Detener e eliminar
Write-Host "`n1. Deteniendo contenedores..." -ForegroundColor Yellow
docker compose down -v 2>&1 | Out-Null

# Limpiar volúmenes
Write-Host "2. Limpiando volúmenes residuales..." -ForegroundColor Yellow
docker volume prune -f 2>&1 | Out-Null

# Esperar un poco
Start-Sleep -Seconds 5

# Levantar
Write-Host "3. Levantando contenedores..." -ForegroundColor Yellow
docker compose up -d --build

# Esperar a que estén listos
Write-Host "`n4. Esperando a que los servicios se inicialicen..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Verificar estado
Write-Host "`n5. Estado de los contenedores:" -ForegroundColor Yellow
docker compose ps

# Verificar logs del backend
Write-Host "`n6. Logs del backend (últimas 30 líneas):" -ForegroundColor Yellow
docker compose logs backend --tail=30

# Verificar conectividad
Write-Host "`n7. Verificando puertos..." -ForegroundColor Yellow
$ports = @{
    "Frontend" = 4200
    "Backend" = 8081
    "PostgreSQL" = 5432
}

foreach ($name in $ports.Keys) {
    $port = $ports[$name]
    $connection = Test-NetConnection -ComputerName localhost -Port $port -WarningAction SilentlyContinue
    if ($connection.TcpTestSucceeded) {
        Write-Host "✓ $name (puerto $port): ACTIVO" -ForegroundColor Green
    } else {
        Write-Host "✗ $name (puerto $port): NO RESPONDE" -ForegroundColor Red
    }
}

Write-Host "`n======================================" -ForegroundColor Cyan
Write-Host "Reinicio completado" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan

