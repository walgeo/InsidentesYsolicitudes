# Script simple para ejecutar pruebas
# Ejecuta: .\run-tests-simple.ps1

# Configurar codificación UTF-8 para visualización correcta
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
# Ajustar la construcción de la ruta para evitar caracteres innecesarios
$ProjectDir = (Join-Path $ScriptDir "..") -replace '\.$', ''

# Verificar si el archivo mvnw.cmd existe antes de cada ejecución
$MvnwCmdPath = Join-Path $ProjectDir "mvnw.cmd"
if (-Not (Test-Path $ScriptDir)) {
    Write-Host "Error: mvnw.cmd no se encuentra en la ruta esperada: $MvnwCmdPath"
    exit
}

do {
    Clear-Host
    Write-Host ""
    Write-Host "=========================================="
    Write-Host "  Ejecutor de Pruebas"
    Write-Host "=========================================="
    Write-Host ""
    Write-Host "1 - Ejecutar TODAS las pruebas"
    Write-Host "2 - Ejecutar solo unitarias"
    Write-Host "3 - Ejecutar solo integracion"
    Write-Host "4 - Ejecutar TicketServiceTest"
    Write-Host "5 - Ejecutar ComentarioServiceTest"
    Write-Host "6 - Ejecutar con cobertura"
    Write-Host "7 - Ver resultados"
    Write-Host "8 - Ver cobertura en navegador"
    Write-Host "9 - Salir"
    Write-Host ""

    $choice = Read-Host "Selecciona una opcion (1-9)"

    switch ($choice) {
        "1" {
            Write-Host "`nEjecutando TODAS las pruebas..."
            Set-Location $ProjectDir
            & $MvnwCmdPath clean test
        }
        "2" {
            Write-Host "`nEjecutando pruebas unitarias..."
            Set-Location $ProjectDir
            & $MvnwCmdPath test -Dtest=*ServiceTest
        }
        "3" {
            Write-Host "`nEjecutando pruebas de integracion..."
            Set-Location $ProjectDir
            & $MvnwCmdPath test -Dtest=*IntegrationTest
        }
        "4" {
            Write-Host "`nEjecutando TicketServiceTest..."
            Set-Location $ProjectDir
            & $MvnwCmdPath test -Dtest=TicketServiceTest
        }
        "5" {
            Write-Host "`nEjecutando ComentarioServiceTest..."
            Set-Location $ProjectDir
            & $MvnwCmdPath test -Dtest=ComentarioServiceTest
        }
        "6" {
            Write-Host "`nEjecutando pruebas con cobertura..."
            Set-Location $ProjectDir
            # Agregar la opción -Xshare:off para desactivar la compartición de clases y eliminar la advertencia
            & $MvnwCmdPath clean test jacoco:report -DargLine="-XX:+EnableDynamicAgentLoading -Xshare:off"
            # Verificar si el archivo jacoco.exec se genera después de ejecutar las pruebas
            $JacocoExecFile = "$ProjectDir\target\jacoco.exec"
            if (-Not (Test-Path $JacocoExecFile)) {
                Write-Host "Advertencia: El archivo jacoco.exec no se generó. Verifica la configuración de JaCoCo y la ejecución de las pruebas."
            } else {
                Write-Host "El archivo jacoco.exec se generó correctamente."
            }
            Write-Host "`nAbrir reporte? (s/n)"
            $open = Read-Host
            if ($open -eq "s" -or $open -eq "S") {
                $JacocoFile = "$ProjectDir\target\site\jacoco\index.html"
                if (Test-Path $JacocoFile) {
                    Start-Process $JacocoFile
                } else {
                    Write-Host "Error: El archivo de cobertura no se encuentra en la ruta esperada: $JacocoFile"
                }
            }
        }
        "7" {
            Write-Host "`nUltimos resultados:"
            Set-Location $ProjectDir
            Get-Content "target/surefire-reports/*.txt" | Select-String "Tests run:"
        }
        "8" {
            Write-Host "`nAbriendo cobertura en navegador..."
            $JacocoFile = "$ProjectDir\target\site\jacoco\index.html"
            if (Test-Path $JacocoFile) {
                Start-Process $JacocoFile
            } else {
                Write-Host "Archivo no encontrado. Primero ejecuta la opcion 6."
            }
        }
        "9" {
            Write-Host "`nHasta luego!"
            exit
        }
        default {
            Write-Host "`nOpcion invalida"
        }
    }

    if ($choice -ne "9") {
        Write-Host "`nPresiona Enter para volver al menu..."
        Read-Host
    }

} while ($choice -ne "9")

