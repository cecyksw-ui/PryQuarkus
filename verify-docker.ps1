# Script de verificación de Docker para Windows
# Ejecutar en PowerShell como Administrador

Write-Host "=== VERIFICACIÓN DE DOCKER SETUP ===" -ForegroundColor Green

# Verificar Docker
Write-Host "`n1. Verificando Docker..." -ForegroundColor Yellow
try {
    $dockerVersion = docker --version
    Write-Host "✅ Docker instalado: $dockerVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker no está instalado o no funciona" -ForegroundColor Red
    exit 1
}

# Verificar Docker Compose
Write-Host "`n2. Verificando Docker Compose..." -ForegroundColor Yellow
try {
    $composeVersion = docker-compose --version
    Write-Host "✅ Docker Compose disponible: $composeVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker Compose no disponible" -ForegroundColor Red
}

# Verificar que Docker daemon esté corriendo
Write-Host "`n3. Verificando Docker daemon..." -ForegroundColor Yellow
try {
    docker info | Out-Null
    Write-Host "✅ Docker daemon está corriendo" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker daemon no está corriendo. Inicia Docker Desktop" -ForegroundColor Red
    exit 1
}

# Probar pull de imagen
Write-Host "`n4. Probando descarga de imagen..." -ForegroundColor Yellow
try {
    docker pull hello-world:latest
    Write-Host "✅ Descarga de imágenes funcionando" -ForegroundColor Green
} catch {
    Write-Host "❌ Error al descargar imágenes" -ForegroundColor Red
}

# Probar ejecución de contenedor
Write-Host "`n5. Probando ejecución de contenedor..." -ForegroundColor Yellow
try {
    $output = docker run --rm hello-world
    if ($output -match "Hello from Docker!") {
        Write-Host "✅ Ejecución de contenedores funcionando" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Error al ejecutar contenedores" -ForegroundColor Red
}

# Verificar espacio en disco
Write-Host "`n6. Verificando espacio en disco..." -ForegroundColor Yellow
$disk = Get-WmiObject -Class Win32_LogicalDisk -Filter "DeviceID='C:'"
$freeSpaceGB = [math]::Round($disk.FreeSpace / 1GB, 2)
if ($freeSpaceGB -gt 10) {
    Write-Host "✅ Espacio disponible: $freeSpaceGB GB" -ForegroundColor Green
} else {
    Write-Host "⚠️ Poco espacio disponible: $freeSpaceGB GB (recomendado >10GB)" -ForegroundColor Yellow
}

Write-Host "`n=== VERIFICACIÓN COMPLETADA ===" -ForegroundColor Green
Write-Host "Docker está listo para usar con tu proyecto Quarkus!" -ForegroundColor Cyan