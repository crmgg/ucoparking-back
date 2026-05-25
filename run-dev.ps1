# Arranca el backend con secretos locales (infisical-secrets.env).
# Uso: .\run-dev.ps1

$port = 8080
$listener = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1

if ($listener) {
    Write-Host ""
    Write-Host "Backend YA esta corriendo en http://localhost:$port/uco-parking" -ForegroundColor Green
    Write-Host "No vuelvas a darle Run en IntelliJ: eso causa el error de puerto ocupado."
    Write-Host "Para reiniciarlo: .\stop-dev.ps1  y luego  .\run-dev.ps1"
    Write-Host ""
    exit 0
}

$secretsFile = Join-Path $PSScriptRoot "infisical-secrets.env"
if (-not (Test-Path $secretsFile)) {
    Write-Host "Falta infisical-secrets.env. Copia infisical-secrets.template.env y completa SMTP_PASSWORD." -ForegroundColor Red
    exit 1
}

Get-Content $secretsFile | ForEach-Object {
    if ($_ -match '^\s*(#|$)') { return }
    $parts = $_ -split '=', 2
    if ($parts.Count -lt 2) { return }
    $name = $parts[0].Trim()
    $value = $parts[1].Trim()
    if ($name) {
        Set-Item -Path "Env:$name" -Value $value
    }
}

if ($env:NOTIFICATION_EMAIL_ENABLED -ne "true") {
    Write-Host "AVISO: NOTIFICATION_EMAIL_ENABLED no es true. Los correos no se enviaran." -ForegroundColor Yellow
} else {
    Write-Host "Correo SMTP activo para $($env:NOTIFICATION_FROM_EMAIL)" -ForegroundColor Green
}

.\mvnw.cmd spring-boot:run @args
