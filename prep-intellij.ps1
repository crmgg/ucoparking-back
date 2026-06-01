# Prepara Run verde IntelliJ.
Set-Location $PSScriptRoot

$stale = Join-Path $PSScriptRoot "target\test-classes\application.properties"
if (Test-Path $stale) {
    Remove-Item $stale -Force
    Write-Host "Borrada config vieja de tests en target/" -ForegroundColor Yellow
}

docker start sqlserver-ucoparking 2>$null | Out-Null
Write-Host "SQL en localhost:14333" -ForegroundColor Green
Write-Host ""
Write-Host "IntelliJ:" -ForegroundColor Cyan
Write-Host "  1. File -> Invalidate Caches -> Restart (solo esta vez)"
Write-Host "  2. Arriba elige: UcoParkingApplication"
Write-Host "  3. Debe decir perfil 'local' (Edit Configurations)"
Write-Host "  4. Run verde"
Write-Host ""
Write-Host "Si el log dice uco-parking-test o puerto 1434 -> config mala." -ForegroundColor Yellow
