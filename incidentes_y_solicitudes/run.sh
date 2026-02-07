#!/bin/bash
# Script para ejecutar el proyecto completo con Docker Compose
# Uso: ./run.sh

echo ""
echo "========================================"
echo "Incidentes y Solicitudes - Docker Setup"
echo "========================================"
echo ""

# Verificar si Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "ERROR: Docker no está instalado"
    echo "Descarga Docker desde: https://www.docker.com/products/docker-desktop"
    exit 1
fi

# Verificar si Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "ERROR: Docker Compose no está instalado"
    exit 1
fi

echo "✓ Docker y Docker Compose detectados"
echo ""
echo "Iniciando servicios..."
echo ""

# Ejecutar docker-compose
docker-compose up -d

# Esperar a que los servicios estén listos
echo ""
echo "Esperando a que los servicios se inicien..."
sleep 15

echo ""
echo "========================================"
echo "✓ Servicios iniciados correctamente!"
echo "========================================"
echo ""
echo "URLs disponibles:"
echo "  Frontend:  http://localhost:4200"
echo "  Backend:   http://localhost:8081"
echo "  Database:  localhost:5432"
echo ""
echo "Para detener los servicios, ejecuta:"
echo "  docker-compose down"
echo ""
echo "Para ver los logs:"
echo "  docker-compose logs -f"
echo ""

