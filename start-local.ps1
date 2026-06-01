# SQL (14333) + Redis (6379) para el boton Run de IntelliJ.
$ErrorActionPreference = "Continue"
$compose = Join-Path $PSScriptRoot "..\uco-parking-sql\docker-compose.yml"
if (-not (Test-Path $compose)) {
    Write-Host "No encuentro $compose" -ForegroundColor Red
    exit 1
}

foreach ($name in @("sqlserver-ucoparking", "redis-ucoparking")) {
    docker start $name 2>$null | Out-Null
}

docker compose -f $compose up -d 2>$null | Out-Null

$running = docker ps --format "{{.Names}}" | Where-Object { $_ -match "sqlserver-ucoparking|redis-ucoparking" }
if ($running -match "sqlserver-ucoparking" -and $running -match "redis-ucoparking") {
    Write-Host "OK: SQL localhost:14333 y Redis localhost:6379" -ForegroundColor Green
    Write-Host "Ahora Run verde en IntelliJ (UcoParkingApplication)." -ForegroundColor Green
} else {
    Write-Host "Falta algun contenedor. Revisa: docker ps" -ForegroundColor Yellow
}
