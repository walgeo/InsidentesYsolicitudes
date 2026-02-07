Write-Host "=== VERIFICANDO ESTADO DE SERVICIOS ===" -ForegroundColor Cyan
Write-Host ""

# Verificar contenedores
Write-Host "1. Contenedores Docker:" -ForegroundColor Yellow
docker ps --format "{{.Names}} - {{.Status}}"
Write-Host ""

# Verificar puertos
Write-Host "2. Puertos en uso:" -ForegroundColor Yellow
$ports = @(4200, 8081, 5432)
foreach ($port in $ports) {
    $result = netstat -ano | Select-String ":$port " | Select-Object -First 1
    if ($result) {
        Write-Host "   Puerto $port : ACTIVO" -ForegroundColor Green
    } else {
        Write-Host "   Puerto $port : INACTIVO" -ForegroundColor Red
    }
}
Write-Host ""

# Verificar backend
Write-Host "3. Backend Health:" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8081/actuator/health" -UseBasicParsing -TimeoutSec 5
    Write-Host "   Backend: ONLINE" -ForegroundColor Green
    Write-Host "   Respuesta: $($response.Content)"
} catch {
    Write-Host "   Backend: OFFLINE o iniciando..." -ForegroundColor Red
}
Write-Host ""

# Verificar frontend
Write-Host "4. Frontend:" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:4200" -UseBasicParsing -TimeoutSec 5
    Write-Host "   Frontend: ONLINE" -ForegroundColor Green
} catch {
    Write-Host "   Frontend: OFFLINE o iniciando..." -ForegroundColor Red
}
Write-Host ""

Write-Host "=== URLS ===" -ForegroundColor Cyan
Write-Host "Frontend: http://localhost:4200"
Write-Host "Backend:  http://localhost:8081"
Write-Host "Health:   http://localhost:8081/actuator/health"
Write-Host ""

