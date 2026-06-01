#!/usr/bin/env bash
# Clona o actualiza los 3 repos y sincroniza deploy/ desde ucoparking-back.
# Uso: GITHUB_USER=crmgg BRANCH=feature/notification-gateway bash clone-stack.sh
set -euo pipefail

GITHUB_USER="${GITHUB_USER:?Define GITHUB_USER (ej: crmgg)}"
TARGET="${TARGET:-/opt/uco-parking}"
BRANCH="${BRANCH:-feature/notification-gateway}"
FRONT_BRANCH="${FRONT_BRANCH:-$BRANCH}"
CONFIG_SERVER_BRANCH="${CONFIG_SERVER_BRANCH:-master}"

mkdir -p "$TARGET"
cd "$TARGET"

clone_or_pull() {
  local dir="$1"
  local repo="$2"
  local branch="$3"
  if [[ -d "$dir/.git" ]]; then
    git -C "$dir" fetch origin
    git -C "$dir" reset --hard "origin/$branch"
  else
    git clone --branch "$branch" --depth 1 "https://github.com/${GITHUB_USER}/${repo}.git" "$dir"
  fi
}

clone_or_pull uco-parking ucoparking-back "$BRANCH"
clone_or_pull UcoParkingFront ucoParking-front "$FRONT_BRANCH"
clone_or_pull uco-parking-config-server uco-parking-config-server "$CONFIG_SERVER_BRANCH"

if [[ ! -d uco-parking/deploy ]]; then
  echo "Falta uco-parking/deploy en el repo ucoparking-back."
  exit 1
fi

# Guardar este script antes de copiar deploy/ (bash sigue leyendo el archivo en disco).
SELF_BACKUP="$(mktemp)"
cp "$0" "$SELF_BACKUP"

mkdir -p deploy
cp -r uco-parking/deploy/. deploy/
cp "$SELF_BACKUP" deploy/scripts/clone-stack.sh
rm -f "$SELF_BACKUP"
chmod +x deploy/deploy.sh deploy/scripts/*.sh 2>/dev/null || true

echo "Stack listo en $TARGET (rama $BRANCH)"
