#!/bin/bash

# Script para ejecutar pruebas del proyecto "Incidentes y Solicitudes"
# Uso: ./run-tests.sh [opción]

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Directorio del proyecto
PROJECT_DIR="C:\Users\walte\OneDrive\Documentos\PROYECTO_ALTAMIRA\incidentes_y_solicitudes"

echo -e "${BLUE}╔═════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║  Ejecutor de Pruebas - Incidentes y Solicitudes     ║${NC}"
echo -e "${BLUE}╚═════════════════════════════════════════════════════╝${NC}"
echo ""

# Función para mostrar menú
show_menu() {
    echo -e "${YELLOW}Selecciona una opción:${NC}"
    echo ""
    echo -e "${BLUE}1)${NC} Ejecutar TODAS las pruebas (recomendado)"
    echo -e "${BLUE}2)${NC} Ejecutar solo pruebas unitarias"
    echo -e "${BLUE}3)${NC} Ejecutar solo pruebas de integración"
    echo -e "${BLUE}4)${NC} Ejecutar pruebas de TicketService"
    echo -e "${BLUE}5)${NC} Ejecutar pruebas de ComentarioService"
    echo -e "${BLUE}6)${NC} Ejecutar pruebas + generar reporte de cobertura"
    echo -e "${BLUE}7)${NC} Ver resultados de la última ejecución"
    echo -e "${BLUE}8)${NC} Ver cobertura en navegador"
    echo -e "${BLUE}9)${NC} Salir"
    echo ""
    read -p "Ingresa el número: " choice
}

# Función para ejecutar pruebas
run_tests() {
    local test_filter=$1
    local description=$2

    echo ""
    echo -e "${GREEN}▶ Ejecutando: $description${NC}"
    echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""

    cd "$PROJECT_DIR"

    if [ -z "$test_filter" ]; then
        mvnw clean test
    else
        mvnw test -Dtest="$test_filter"
    fi

    echo ""
    echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
}

# Función para ver resultados
view_results() {
    echo ""
    echo -e "${GREEN}▶ Resumen de pruebas:${NC}"
    echo ""

    cd "$PROJECT_DIR"
    Get-Content "target/surefire-reports/*.txt" | Select-String "Tests run:"

    echo ""
    echo -e "${YELLOW}Archivos de reporte disponibles:${NC}"
    Get-ChildItem "target/surefire-reports/*.txt" | Select-Object Name
}

# Función para ver cobertura
view_coverage() {
    local jacoco_file="$PROJECT_DIR\target\site\jacoco\index.html"

    if [ -f "$jacoco_file" ]; then
        echo -e "${GREEN}▶ Abriendo reporte de cobertura...${NC}"
        start "$jacoco_file"
    else
        echo -e "${RED}❌ Reporte de cobertura no encontrado.${NC}"
        echo -e "${YELLOW}Primero ejecuta: mvnw clean test jacoco:report${NC}"
    fi
}

# Loop principal
while true; do
    show_menu

    case $choice in
        1)
            run_tests "" "TODAS las pruebas (limpio + test)"
            ;;
        2)
            run_tests "*ServiceTest" "Pruebas unitarias de servicios"
            ;;
        3)
            run_tests "*IntegrationTest" "Pruebas de integración"
            ;;
        4)
            run_tests "TicketServiceTest" "Pruebas de TicketService"
            ;;
        5)
            run_tests "ComentarioServiceTest" "Pruebas de ComentarioService"
            ;;
        6)
            echo ""
            echo -e "${GREEN}▶ Ejecutando pruebas + cobertura${NC}"
            cd "$PROJECT_DIR"
            mvnw clean test jacoco:report
            echo ""
            read -p "¿Deseas abrir el reporte de cobertura? (s/n): " open_coverage
            if [ "$open_coverage" = "s" ] || [ "$open_coverage" = "S" ]; then
                start "target/site/jacoco/index.html"
            fi
            ;;
        7)
            view_results
            ;;
        8)
            view_coverage
            ;;
        9)
            echo -e "${YELLOW}¡Hasta luego!${NC}"
            exit 0
            ;;
        *)
            echo -e "${RED}❌ Opción inválida${NC}"
            ;;
    esac

    echo ""
    read -p "Presiona Enter para continuar..."
    clear
done

