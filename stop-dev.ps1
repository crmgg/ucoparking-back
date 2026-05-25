# Detiene el proceso que escucha en el puerto 8080 (backend).
# Uso: .\stop-dev.ps1

$port = 8080
$pids = @(Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue |
    Select-Object -ExpandProperty OwningProcess -Unique)

if ($pids.Count -eq 0) {
    Write-Host "Nada escuchando en el puerto $port."
    exit 0
}

foreach ($procId in $pids) {
    Stop-Process -Id $procId -Force -ErrorAction SilentlyContinue
    Write-Host "Proceso $procId detenido (puerto $port)."
}
