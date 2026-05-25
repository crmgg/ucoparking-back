# Prueba rapida del Notification Gateway (correo de bienvenida).
# Uso: .\test-email.ps1

$body = @{
    templateCode = "WELCOME_STUDENT"
    recipient    = "claryrivillaas@gmail.com"
    channel      = "EMAIL"
    variables    = @{ studentName = "Clary" }
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod `
        -Uri "http://localhost:8080/uco-parking/v1/notifications/send" `
        -Method Post `
        -ContentType "application/json" `
        -Body $body
    $response | ConvertTo-Json
} catch {
    Write-Host "Error: el backend no responde en :8080 o fallo SMTP." -ForegroundColor Red
    Write-Host $_.Exception.Message
}
