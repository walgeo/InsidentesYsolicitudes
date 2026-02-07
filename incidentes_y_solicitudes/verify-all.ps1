Write-Host "=================================" -ForegroundColor Cyan
Write-Host "  VERIFICACIÓN COMPLETA DEL SISTEMA" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan
Write-Host ""

# 1. Verificar contenedores
Write-Host "1. Estado de Contenedores Docker:" -ForegroundColor Yellow
$containers = docker ps --format "{{.Names}},{{.Status}}" 2>$null
if ($containers) {
    foreach ($line in $containers) {
        $parts = $line -split ','
        $name = $parts[0]
        $status = $parts[1]
        if ($status -like "*Up*") {
            Write-Host "   ✅ $name - $status" -ForegroundColor Green
        } else {
            Write-Host "   ❌ $name - $status" -ForegroundColor Red
        }
    }
} else {
    Write-Host "   ❌ No hay contenedores corriendo" -ForegroundColor Red
}
Write-Host ""

# 2. Verificar puertos
Write-Host "2. Puertos en Uso:" -ForegroundColor Yellow
$ports = @(
    @{Port=5432; Service="PostgreSQL"},
    @{Port=8081; Service="Backend"},
    @{Port=4200; Service="Frontend"}
)

foreach ($p in $ports) {
    $result = netstat -ano 2>$null | Select-String ":$($p.Port) "
    if ($result) {
        Write-Host "   ✅ Puerto $($p.Port) ($($p.Service)) - ACTIVO" -ForegroundColor Green
    } else {
        Write-Host "   ❌ Puerto $($p.Port) ($($p.Service)) - INACTIVO" -ForegroundColor Red
    }
}
Write-Host ""

# 3. Verificar Backend Health
Write-Host "3. Backend API:" -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8081/actuator/health" -TimeoutSec 5 -ErrorAction Stop
    Write-Host "   ✅ Health Check: $($health.status)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Backend no responde (puede estar iniciando...)" -ForegroundColor Red
}

# Verificar endpoint de tickets
try {
    $tickets = Invoke-RestMethod -Uri "http://localhost:8081/api/tickets?page=0&size=5" -TimeoutSec 5 -ErrorAction Stop
    Write-Host "   ✅ Endpoint /api/tickets: FUNCIONANDO" -ForegroundColor Green
    Write-Host "   📊 Total de tickets: $($tickets.totalElements)" -ForegroundColor Cyan
    if ($tickets.content) {
        Write-Host "   📋 Primeros tickets:" -ForegroundColor Cyan
        foreach ($ticket in $tickets.content | Select-Object -First 3) {
            Write-Host "      - ID $($ticket.id): $($ticket.titulo)" -ForegroundColor White
        }
    }
} catch {
    Write-Host "   ❌ Endpoint /api/tickets no responde" -ForegroundColor Red
    Write-Host "      Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 4. Verificar Frontend
Write-Host "4. Frontend Angular:" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:4200" -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Host "   ✅ Frontend cargando correctamente" -ForegroundColor Green
    }
} catch {
    Write-Host "   ❌ Frontend no responde" -ForegroundColor Red
}
Write-Host ""

# 5. Verificar base de datos
Write-Host "5. Base de Datos PostgreSQL:" -ForegroundColor Yellow
try {
    $dbCheck = docker exec incidentes-postgres psql -U admin -d postgres -c "SET search_path TO incidentes_db; SELECT COUNT(*) as total FROM tickets;" -t 2>$null
    if ($dbCheck) {
        $count = $dbCheck.Trim()
        if ([int]$count -gt 0) {
            Write-Host "   ✅ Base de datos operativa" -ForegroundColor Green
            Write-Host "   📊 Tickets en BD: $count" -ForegroundColor Cyan
        } else {
            Write-Host "   ⚠️  Base de datos vacía (0 tickets)" -ForegroundColor Yellow
        }
    }
} catch {
    Write-Host "   ❌ No se pudo conectar a la base de datos" -ForegroundColor Red
}
Write-Host ""

# 6. Verificar migraciones Flyway
Write-Host "6. Migraciones Flyway:" -ForegroundColor Yellow
try {
    $migrations = docker exec incidentes-postgres psql -U admin -d postgres -c "SELECT version, description, success FROM flyway_schema_history ORDER BY installed_rank;" -t 2>$null
    if ($migrations) {
        Write-Host "   ✅ Migraciones ejecutadas:" -ForegroundColor Green
        Write-Host $migrations
    }
} catch {
    Write-Host "   ⚠️  No se pudieron verificar migraciones" -ForegroundColor Yellow
}
Write-Host ""

# RESUMEN FINAL
Write-Host "=================================" -ForegroundColor Cyan
Write-Host "  ACCESOS" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan
Write-Host "Frontend:    http://localhost:4200" -ForegroundColor White
Write-Host "Backend API: http://localhost:8081/api/tickets" -ForegroundColor White
Write-Host "Health:      http://localhost:8081/actuator/health" -ForegroundColor White
Write-Host ""
Write-Host "=================================" -ForegroundColor Cyan
Write-Host "  INSTRUCCIONES PARA DEPURAR" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan
Write-Host "1. Abre: http://localhost:4200" -ForegroundColor White
Write-Host "2. Presiona F12 (abrir DevTools)" -ForegroundColor White
Write-Host "3. Ve a la pestaña 'Console'" -ForegroundColor White
Write-Host "4. Busca los logs que empiezan con 🔍 🌐 ✅ o ❌" -ForegroundColor White
Write-Host "5. Comparte los logs que veas en la consola" -ForegroundColor White
Write-Host ""

# Logs recientes del backend
Write-Host "=================================" -ForegroundColor Cyan
Write-Host "  LOGS RECIENTES DEL BACKEND" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan
docker logs incidentes-backend --tail 20 2>$null
Write-Host ""

