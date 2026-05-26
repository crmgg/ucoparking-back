# Prueba SMTP del Notification Gateway (via Kong).
# Uso: .\verify-email.ps1

$ErrorActionPreference = "Stop"
$secretsFile = Join-Path $PSScriptRoot "infisical-secrets.env"

if (-not (Test-Path $secretsFile)) {
    Write-Host "Falta infisical-secrets.env" -ForegroundColor Red
    exit 1
}

$fromEmail = (Get-Content $secretsFile | Where-Object { $_ -match '^NOTIFICATION_FROM_EMAIL=' }) -replace '^NOTIFICATION_FROM_EMAIL=', ''
$fromEmail = $fromEmail.Trim()

$body = @{
    templateCode = "RESERVATION_CONFIRMED"
    recipient    = $fromEmail
    channel      = "EMAIL"
    variables    = @{
        studentName = "Prueba UCO"
        spaceNumber = "5"
    }
} | ConvertTo-Json

Write-Host "Enviando correo de prueba a $fromEmail ..." -ForegroundColor Cyan

try {
    $response = Invoke-RestMethod `
        -Uri "http://localhost:8000/uco-parking/v1/notifications/send" `
        -Method Post `
        -ContentType "application/json" `
        -Body $body

    $response | ConvertTo-Json

    if ($response.status -eq "SENT") {
        Write-Host "`nCorreo enviado. Revisa la bandeja de $fromEmail" -ForegroundColor Green
    } elseif ($response.status -eq "FAILED") {
        Write-Host "`nFallo envio:" -ForegroundColor Red
        Write-Host $response.detail
        Write-Host "`nRevisa RESEND_API_KEY en infisical-secrets.env (https://resend.com/api-keys)" -ForegroundColor Yellow
        Write-Host "Luego ejecuta .\run-docker.ps1"
    } else {
        Write-Host "`nEstado: $($response.status) - $($response.detail)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Error llamando al API:" -ForegroundColor Red
    Write-Host $_.Exception.Message
    if ($_.ErrorDetails.Message) { Write-Host $_.ErrorDetails.Message }
}
