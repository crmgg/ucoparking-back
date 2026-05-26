# Arranca uco-parking-container con secretos SMTP y red Docker del demo.
# Uso: .\run-docker.ps1

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
$secretsFile = Join-Path $root "infisical-secrets.env"
$image = "uco-parking:latest"
$container = "uco-parking-container"
$network = "uco-network"

if (-not (Test-Path $secretsFile)) {
    Write-Host "Falta infisical-secrets.env. Copia infisical-secrets.template.env y completa SMTP_PASSWORD." -ForegroundColor Red
    exit 1
}

Write-Host "Construyendo imagen $image..." -ForegroundColor Cyan
docker build -t $image $root | Out-Null

docker rm -f $container 2>$null | Out-Null

Write-Host "Iniciando $container en red $network..." -ForegroundColor Cyan
docker run -d `
    --name $container `
    --network $network `
    --add-host=host.docker.internal:host-gateway `
    --env-file $secretsFile `
    -e DB_HOST=sqlserver-ucoparking `
    -e DB_PORT=1433 `
    -e REDIS_HOST=redis-ucoparking `
    -e REDIS_PORT=6379 `
    -e AUTH0_SECURITY_ENABLED=false `
    $image | Out-Null

Start-Sleep -Seconds 20

$fromEmail = (Get-Content $secretsFile | Where-Object { $_ -match '^NOTIFICATION_FROM_EMAIL=' }) -replace '^NOTIFICATION_FROM_EMAIL=', ''
$fromEmail = $fromEmail.Trim()
if (-not $fromEmail) { $fromEmail = "test@example.com" }

$body = @{
    templateCode = "WELCOME_STUDENT"
    recipient    = $fromEmail
    channel      = "EMAIL"
    variables    = @{ studentName = "Test UCO Parking" }
} | ConvertTo-Json

try {
    $probe = Invoke-RestMethod `
        -Uri "http://localhost:8000/uco-parking/v1/notifications/send" `
        -Method Post `
        -ContentType "application/json" `
        -Body $body
    if ($probe.status -eq "SKIPPED") {
        Write-Host "AVISO: correo desactivado ($($probe.detail))" -ForegroundColor Yellow
    } elseif ($probe.status -eq "SENT") {
        Write-Host "SMTP OK: correo de prueba enviado." -ForegroundColor Green
    } else {
        Write-Host "Gateway respondio: status=$($probe.status) detail=$($probe.detail)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Backend aun arrancando o Kong no responde. Revisa: docker logs $container" -ForegroundColor Yellow
}

Write-Host "`nListo. API: http://localhost:8000/uco-parking/v1/students" -ForegroundColor Green
