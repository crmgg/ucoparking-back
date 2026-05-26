#!/usr/bin/env bash
# Ejecutar en la VM para desplegar o actualizar (manual o desde CD).
set -euo pipefail

TARGET="${TARGET:-/opt/uco-parking}"
GITHUB_USER="${GITHUB_USER:-crmgg}"
BRANCH="${BRANCH:-feature/notification-gateway}"

bash "$TARGET/deploy/scripts/clone-stack.sh"
cd "$TARGET/deploy"
bash deploy.sh
