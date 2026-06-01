# Simula el Run verde de IntelliJ y prueba health.
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

Write-Host "=== 1. Docker ===" -ForegroundColor Cyan
docker start sqlserver-ucoparking 2>$null | Out-Null
docker start redis-ucoparking 2>$null | Out-Null
docker ps --format "table {{.Names}}\t{{.Status}}" | Select-String "ucoparking|sqlserver|redis"

Write-Host "`n=== 2. Variables (como IntelliJ) ===" -ForegroundColor Cyan
Get-Content ".\infisical-secrets.env" | ForEach-Object {
    if ($_ -match '^\s*(#|$)') { return }
    $p = $_ -split '=', 2
    if ($p.Count -ge 2) { Set-Item -Path "Env:$($p[0].Trim())" -Value $p[1].Trim() }
}
$env:SPRING_CONFIG_IMPORT = "optional:configserver:http://localhost:8888,optional:file:./infisical-secrets.env"
$env:SPRING_CLOUD_CONFIG_IMPORT_CHECK_ENABLED = "false"

Write-Host "DB=$env:DB_HOST`:$env:DB_PORT AUTH0_SECURITY_ENABLED=$env:AUTH0_SECURITY_ENABLED"

Write-Host "`n=== 3. Compilar ===" -ForegroundColor Cyan
.\mvnw.cmd -q -DskipTests compile
if ($LASTEXITCODE -ne 0) { throw "Compile fallo" }

Write-Host "`n=== 4. Arrancar backend (90s max) ===" -ForegroundColor Cyan
$log = Join-Path $env:TEMP "uco-intellij-test.log"
Remove-Item $log -ErrorAction SilentlyContinue
$proc = Start-Process -FilePath ".\mvnw.cmd" -ArgumentList "spring-boot:run","-q" -NoNewWindow -PassThru -RedirectStandardOutput $log -RedirectStandardError $log

$ok = $false
for ($i = 0; $i -lt 90; $i++) {
    Start-Sleep -Seconds 1
    if (Test-Path $log) {
        $t = Get-Content $log -Raw -ErrorAction SilentlyContinue
        if ($t -match "Started UcoParkingApplication") { $ok = $true; break }
        if ($t -match "APPLICATION FAILED TO START") { break }
    }
    if ($proc.HasExited) { break }
}

Write-Host "`n=== 5. Health ===" -ForegroundColor Cyan
try {
    $health = Invoke-RestMethod "http://localhost:8080/uco-parking/actuator/health" -TimeoutSec 5
    Write-Host ($health | ConvertTo-Json -Compress) -ForegroundColor Green
} catch {
    Write-Host "Health fallo: $($_.Exception.Message)" -ForegroundColor Red
}

if ($ok) {
    Write-Host "`nOK: Backend arranca como IntelliJ Run verde." -ForegroundColor Green
} else {
    Write-Host "`nFALLO. Ultimas lineas del log:" -ForegroundColor Red
    if (Test-Path $log) { Get-Content $log -Tail 25 }
}

Get-Process -Name java -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
Write-Host "`nJava detenido." -ForegroundColor Yellow
