#!/usr/bin/env bash
# Ejecutar en la VM para desplegar o actualizar (manual o desde CD).
set -euo pipefail

TARGET="${TARGET:-/opt/uco-parking}"
GITHUB_USER="${GITHUB_USER:-crmgg}"
BRANCH="${BRANCH:-feature/notification-gateway}"
FRONT_BRANCH="${FRONT_BRANCH:-$BRANCH}"
CONFIG_SERVER_BRANCH="${CONFIG_SERVER_BRANCH:-master}"

export GITHUB_USER BRANCH FRONT_BRANCH TARGET CONFIG_SERVER_BRANCH

if [[ ! -f "$TARGET/deploy/.env" ]]; then
  echo "Falta $TARGET/deploy/.env — copia .env.example y completa PUBLIC_HOST, Auth0 y DB_PASSWORD."
  exit 1
fi

bash "$TARGET/deploy/scripts/clone-stack.sh"
cd "$TARGET/deploy"
bash deploy.sh
