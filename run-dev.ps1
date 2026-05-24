# Arranca el backend inyectando secretos desde Infisical Key Vault.
# Requisitos: infisical CLI instalado y `infisical init` ejecutado en esta carpeta.
#
# Uso: .\run-dev.ps1

infisical run --env dev -- cmd /c ".\mvnw.cmd spring-boot:run" @args
