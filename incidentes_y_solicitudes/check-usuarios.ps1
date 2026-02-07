#!/usr/bin/env pwsh
# Script para consultar usuarios en la BD

$ProjectDir = "C:\Users\walte\OneDrive\Documentos\PROYECTO_ALTAMIRA\incidentes_y_solicitudes"
Set-Location $ProjectDir

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Consultando usuarios en PostgreSQL" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan

try {
    # Esperar a que PostgreSQL esté listo
    Write-Host "`nEsperando a que PostgreSQL esté disponible..." -ForegroundColor Yellow
    $retries = 0
    $maxRetries = 30

    while ($retries -lt $maxRetries) {
        try {
            docker exec incidentes-postgres pg_isready -U postgres > $null 2>&1
            if ($LASTEXITCODE -eq 0) {
                Write-Host "PostgreSQL está disponible" -ForegroundColor Green
                break
            }
        } catch {
            # Ignorar el error
        }
        $retries++
        Start-Sleep -Seconds 2
    }

    if ($retries -eq $maxRetries) {
        Write-Host "PostgreSQL no está disponible después de esperar" -ForegroundColor Red
        exit 1
    }

    Write-Host "`nConsultando tabla usuarios..." -ForegroundColor Yellow
    $usuariosResult = docker exec incidentes-postgres psql -U postgres -d postgres -c "SELECT usuario, nombre, apellido, correo, rol_id FROM incidentes_db.usuarios;" 2>&1
    Write-Host $usuariosResult

    Write-Host "`nConsultando tabla roles..." -ForegroundColor Yellow
    $rolesResult = docker exec incidentes-postgres psql -U postgres -d postgres -c "SELECT id, nombre, descripcion FROM incidentes_db.cat_roles;" 2>&1
    Write-Host $rolesResult

    Write-Host "`n=====================================" -ForegroundColor Cyan
    Write-Host "Credenciales por defecto:" -ForegroundColor Cyan
    Write-Host "=====================================" -ForegroundColor Cyan
    Write-Host "Usuario: admin" -ForegroundColor Green
    Write-Host "Contraseña: admin" -ForegroundColor Green
    Write-Host "Rol: ADMIN (ID: 3)" -ForegroundColor Green
    Write-Host "=====================================" -ForegroundColor Cyan

} catch {
    Write-Host "Error: $_" -ForegroundColor Red
    exit 1
}

