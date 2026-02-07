# Configurar codificacion UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

cd "C:\Users\walte\OneDrive\Documentos\PROYECTO_ALTAMIRA\incidentes_y_solicitudes"

Write-Host "=== EJECUTANDO TESTS Y GENERANDO JACOCO ===" -ForegroundColor Cyan
.\mvnw.cmd clean test

Write-Host "`n=== VERIFICANDO SI JACOCO SE GENERO ===" -ForegroundColor Cyan
if (Test-Path "target\site\jacoco\index.html") {
    Write-Host "JACOCO EXISTE!" -ForegroundColor Green
    Write-Host "`nAbriendo Jacoco en navegador..." -ForegroundColor Yellow
    Start-Process "target\site\jacoco\index.html"
} else {
    Write-Host "JACOCO NO SE GENERO" -ForegroundColor Red
    Write-Host "`nRevisando errores en tests..." -ForegroundColor Yellow
    Get-Content "target\surefire-reports\*.txt" | Select-String "Failures|Errors" | Select-Object -First 10
}

Write-Host "`n=== RESUMEN DE TESTS ===" -ForegroundColor Cyan
Get-Content "target\surefire-reports\*.txt" | Select-String "Tests run" | Select-Object -First 5

