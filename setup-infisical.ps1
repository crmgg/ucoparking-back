# Configura Infisical Key Vault para uco-parking (solo backend).
# Ejecutar desde la carpeta uco-parking: .\setup-infisical.ps1

$ErrorActionPreference = "Stop"
$secretsFile = Join-Path $PSScriptRoot "infisical-secrets.env"
$templateFile = Join-Path $PSScriptRoot "infisical-secrets.template.env"

function Test-InfisicalLogin {
    $status = cmd /c "infisical login status 2>&1"
    return ($LASTEXITCODE -eq 0)
}

Write-Host "`n=== Infisical Key Vault - uco-parking ===`n" -ForegroundColor Cyan

if (-not (Get-Command infisical -ErrorAction SilentlyContinue)) {
    Write-Host "Infisical CLI no encontrado. Instala con:" -ForegroundColor Yellow
    Write-Host "  npm install -g @infisical/cli`n"
    exit 1
}

if (-not (Test-InfisicalLogin)) {
    Write-Host "No estas autenticado. Se abrira el login de Infisical..." -ForegroundColor Yellow
    infisical login
    if (-not (Test-InfisicalLogin)) {
        Write-Host "Login fallido. Ejecuta manualmente: infisical login" -ForegroundColor Red
        exit 1
    }
}

$configFile = Join-Path $PSScriptRoot ".infisical.json"
if (-not (Test-Path $configFile)) {
    Write-Host "`nPaso 1: Vincular proyecto" -ForegroundColor Cyan
    Write-Host "  - En app.infisical.com crea un proyecto llamado 'uco-parking' (si no existe)."
    Write-Host "  - Luego seleccionalo cuando aparezca el menu interactivo.`n"
    Set-Location $PSScriptRoot
    infisical init
    if (-not (Test-Path $configFile)) {
        Write-Host "No se creo .infisical.json. Abortando." -ForegroundColor Red
        exit 1
    }
}

if (-not (Test-Path $secretsFile)) {
    if (-not (Test-Path $templateFile)) {
        Write-Host "Falta infisical-secrets.template.env" -ForegroundColor Red
        exit 1
    }
    Copy-Item $templateFile $secretsFile
    Write-Host "Se creo infisical-secrets.env desde la plantilla." -ForegroundColor Yellow
    Write-Host "Edita DB_PASSWORD en infisical-secrets.env y vuelve a ejecutar este script.`n"
    notepad $secretsFile
    exit 0
}

$content = Get-Content $secretsFile -Raw
if ($content -match "DB_PASSWORD=CAMBIAR_AQUI" -or $content -match "DB_PASSWORD=\s*$") {
    Write-Host "Completa DB_PASSWORD en infisical-secrets.env antes de continuar." -ForegroundColor Red
    notepad $secretsFile
    exit 1
}

Write-Host "Subiendo secretos al entorno 'dev' en Infisical..." -ForegroundColor Cyan
Set-Location $PSScriptRoot
infisical secrets set --env dev --file $secretsFile

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error al subir secretos." -ForegroundColor Red
    exit 1
}

Write-Host "`nSecretos cargados. Verificando..." -ForegroundColor Green
infisical secrets --env dev

Write-Host "`nListo. Arranca el backend con:" -ForegroundColor Green
Write-Host "  .\run-dev.ps1`n"
