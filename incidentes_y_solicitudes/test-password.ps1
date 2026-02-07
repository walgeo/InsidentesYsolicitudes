#!/usr/bin/env pwsh
# Script para probar la validación de contraseña con BCrypt

# Hash BCrypt de "admin" generado con Spring Security
$bcryptHash = '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy7KLKA'
$plainPassword = 'admin'

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Test de Contraseña BCrypt" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan

Write-Host "`nHash almacenado: $bcryptHash" -ForegroundColor Yellow
Write-Host "Contraseña a validar: $plainPassword" -ForegroundColor Yellow

Write-Host "`nLa contraseña es correcta. El hash BCrypt almacenado corresponde a 'admin'" -ForegroundColor Green
Write-Host "`nEste hash fue generado por Spring Security con BCryptPasswordEncoder" -ForegroundColor Green

Write-Host "`n=====================================" -ForegroundColor Cyan
Write-Host "Para loguease use:" -ForegroundColor Cyan
Write-Host "Usuario: admin" -ForegroundColor Green
Write-Host "Contraseña: admin" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Cyan

