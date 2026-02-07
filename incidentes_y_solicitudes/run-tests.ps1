# Script para ejecutar pruebas - Incidentes y Solicitudes
# Ejecuta en PowerShell: .\run-tests.ps1

# Colores
$Colors = @{
    Reset = "`e[0m"
    Blue = "`e[34m"
    Green = "`e[32m"
    Yellow = "`e[33m"
    Red = "`e[31m"
}

# Directorio del proyecto
$ProjectDir = "C:\Users\walte\OneDrive\Documentos\PROYECTO_ALTAMIRA\incidentes_y_solicitudes"

# Función para mostrar encabezado
function Show-Header {
    Write-Host ""
    Write-Host "$($Colors.Blue)╔════════════════════════════════════════════════════╗$($Colors.Reset)"
    Write-Host "$($Colors.Blue)║  Ejecutor de Pruebas - Incidentes y Solicitudes    ║$($Colors.Reset)"
    Write-Host "$($Colors.Blue)╚════════════════════════════════════════════════════╝$($Colors.Reset)"
    Write-Host ""
}

# Función para mostrar menú
function Show-Menu {
    Write-Host "$($Colors.Yellow)Selecciona una opción:$($Colors.Reset)"
    Write-Host ""
    Write-Host "$($Colors.Blue)1$($Colors.Reset) - Ejecutar TODAS las pruebas"
    Write-Host "$($Colors.Blue)2$($Colors.Reset) - Ejecutar solo pruebas unitarias"
    Write-Host "$($Colors.Blue)3$($Colors.Reset) - Ejecutar solo pruebas de integración"
    Write-Host "$($Colors.Blue)4$($Colors.Reset) - Ejecutar pruebas de TicketService"
    Write-Host "$($Colors.Blue)5$($Colors.Reset) - Ejecutar pruebas de ComentarioService"
    Write-Host "$($Colors.Blue)6$($Colors.Reset) - Ejecutar pruebas + generar reporte"
    Write-Host "$($Colors.Blue)7$($Colors.Reset) - Ver resultados de la última ejecución"
    Write-Host "$($Colors.Blue)8$($Colors.Reset) - Ver cobertura en navegador"
    Write-Host "$($Colors.Blue)9$($Colors.Reset) - Ver cobertura CSV"
    Write-Host "$($Colors.Blue)10$($Colors.Reset) - Limpiar reportes antiguos"
    Write-Host "$($Colors.Blue)11$($Colors.Reset) - Salir"
    Write-Host ""
    $choice = Read-Host "Ingresa el número"
    return $choice
}

# Función para ejecutar pruebas
function Run-Tests {
    param(
        [string]$TestFilter = "",
        [string]$Description = ""
    )

    Write-Host ""
    Write-Host "$($Colors.Green)▶ Ejecutando: $Description$($Colors.Reset)"
    Write-Host "$($Colors.Yellow)━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━$($Colors.Reset)"
    Write-Host ""

    Set-Location $ProjectDir

    if ($TestFilter -eq "") {
        & .\mvnw.cmd clean test
    } else {
        & .\mvnw.cmd test -Dtest=$TestFilter
    }

    Write-Host ""
    Write-Host "$($Colors.Yellow)━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━$($Colors.Reset)"
    Write-Host ""
}

# Función para ver resultados
function View-Results {
    Write-Host ""
    Write-Host "$($Colors.Green)▶ Resumen de pruebas:$($Colors.Reset)"
    Write-Host ""

    Set-Location $ProjectDir
    Get-Content "target/surefire-reports/*.txt" | Select-String "Tests run:"

    Write-Host ""
    Write-Host "$($Colors.Yellow)Archivos de reporte disponibles:$($Colors.Reset)"
    Get-ChildItem "target/surefire-reports/*.txt" -ErrorAction SilentlyContinue | Select-Object Name
}

# Función para ver cobertura
function View-Coverage {
    $JacocoFile = "$ProjectDir\target\site\jacoco\index.html"

    if (Test-Path $JacocoFile) {
        Write-Host "$($Colors.Green)▶ Abriendo reporte de cobertura...$($Colors.Reset)"
        Start-Process $JacocoFile
    } else {
        Write-Host "$($Colors.Red)❌ Reporte de cobertura no encontrado.$($Colors.Reset)"
        Write-Host "$($Colors.Yellow)Primero ejecuta la opción 6$($Colors.Reset)"
    }
}

# Función para ver CSV
function View-CSV {
    $CsvFile = "$ProjectDir\target\site\jacoco\jacoco.csv"

    if (Test-Path $CsvFile) {
        Write-Host "$($Colors.Green)▶ Primeras 20 líneas del CSV:$($Colors.Reset)"
        Write-Host ""
        Get-Content $CsvFile | Select-Object -First 20
        Write-Host ""
        Write-Host "$($Colors.Yellow)Archivo completo: $CsvFile$($Colors.Reset)"
    } else {
        Write-Host "$($Colors.Red)❌ Archivo CSV no encontrado.$($Colors.Reset)"
    }
}

# Función para limpiar
function Clean-Reports {
    Write-Host "$($Colors.Yellow)Limpiando reportes antiguos...$($Colors.Reset)"
    Set-Location $ProjectDir
    & .\mvnw.cmd clean
    Write-Host "$($Colors.Green)✓ Reportes limpios$($Colors.Reset)"
}

# Loop principal
$continue = $true
while ($continue) {
    Clear-Host
    Show-Header

    $choice = Show-Menu

    switch ($choice) {
        "1" {
            Run-Tests "" "TODAS las pruebas (limpio + test)"
        }
        "2" {
            Run-Tests "*ServiceTest" "Pruebas unitarias de servicios"
        }
        "3" {
            Run-Tests "*IntegrationTest" "Pruebas de integración"
        }
        "4" {
            Run-Tests "TicketServiceTest" "Pruebas de TicketService"
        }
        "5" {
            Run-Tests "ComentarioServiceTest" "Pruebas de ComentarioService"
        }
        "6" {
            Write-Host ""
            Write-Host "$($Colors.Green)▶ Ejecutando pruebas + cobertura$($Colors.Reset)"
            Set-Location $ProjectDir
            & .\mvnw.cmd clean test jacoco:report
            Write-Host ""
            $openCoverage = Read-Host "$($Colors.Yellow)¿Deseas abrir el reporte de cobertura? (s/n)$($Colors.Reset)"
            if ($openCoverage -eq "s" -or $openCoverage -eq "S") {
                Start-Process "$ProjectDir\target\site\jacoco\index.html"
            }
        }
        "7" {
            View-Results
        }
        "8" {
            View-Coverage
        }
        "9" {
            View-CSV
        }
        "10" {
            Clean-Reports
        }
        "11" {
            Write-Host "$($Colors.Yellow)¡Hasta luego!$($Colors.Reset)"
            $continue = $false
        }
        default {
            Write-Host "$($Colors.Red)❌ Opción inválida$($Colors.Reset)"
        }
    }

    if ($continue) {
        Write-Host ""
        Read-Host "$($Colors.Yellow)Presiona Enter para continuar$($Colors.Reset)"
    }
}



