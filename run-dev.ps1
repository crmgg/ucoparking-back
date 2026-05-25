# Arranca el backend (puerto 8080) solo si aun no esta corriendo.
# Uso: .\run-dev.ps1

$port = 8080
$listener = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1

if ($listener) {
    Write-Host ""
    Write-Host "Backend YA esta corriendo en http://localhost:$port/uco-parking" -ForegroundColor Green
    Write-Host "No vuelvas a darle Run en IntelliJ: eso causa el error de puerto ocupado."
    Write-Host ""
    Write-Host "Prueba: http://localhost:$port/uco-parking/actuator/health"
    Write-Host "Para reiniciarlo: .\stop-dev.ps1  y luego  .\run-dev.ps1"
    Write-Host ""
    exit 0
}

Write-Host "Arrancando backend en http://localhost:$port/uco-parking ..."
.\mvnw.cmd spring-boot:run @args
