# Libera un espacio de parqueadero via API.
# Uso: .\release-space.ps1 2

param(
    [int]$SpaceNumber = 2,
    [string]$ApiBase = "http://localhost:8000/uco-parking/v1/students"
)

try {
    $result = Invoke-RestMethod -Uri "$ApiBase/admin/release/$SpaceNumber" -Method Post
    Write-Host "Espacio $SpaceNumber liberado -> $($result.status)" -ForegroundColor Green
    exit 0
} catch {
    Write-Host "Admin release fallo, intentando release normal..." -ForegroundColor Yellow
}

$spots = Invoke-RestMethod $ApiBase
$spot = $spots | Where-Object { $_.spaceNumber -eq $SpaceNumber }

if (-not $spot) {
    Write-Host "No existe el espacio $SpaceNumber" -ForegroundColor Red
    exit 1
}

Write-Host "Espacio $SpaceNumber -> $($spot.status) (dueño: $($spot.occupiedByStudentId))"

if ($spot.status -eq 'AVAILABLE') {
    Write-Host "Ya esta libre." -ForegroundColor Green
    exit 0
}

if (-not $spot.occupiedByStudentId) {
    Write-Host "Ocupado pero sin studentId; usa SQL manual." -ForegroundColor Red
    exit 1
}

$body = @{ spaceNumber = $SpaceNumber; studentId = $spot.occupiedByStudentId } | ConvertTo-Json
$result = Invoke-RestMethod -Uri "$ApiBase/release" -Method Post -ContentType "application/json" -Body $body
Write-Host "Liberado -> $($result.status)" -ForegroundColor Green
