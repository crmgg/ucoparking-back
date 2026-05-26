#!/usr/bin/env bash
# Clona o actualiza los 3 repos y sincroniza deploy/ desde ucoparking-back.
# Uso: GITHUB_USER=crmgg BRANCH=feature/notification-gateway bash clone-stack.sh
set -euo pipefail

GITHUB_USER="${GITHUB_USER:?Define GITHUB_USER (ej: crmgg)}"
TARGET="${TARGET:-/opt/uco-parking}"
BRANCH="${BRANCH:-feature/notification-gateway}"

sudo mkdir -p "$TARGET"
sudo chown "$USER:$USER" "$TARGET"
cd "$TARGET"

clone_or_pull() {
  local dir="$1"
  local repo="$2"
  if [[ -d "$dir/.git" ]]; then
    git -C "$dir" fetch origin
    git -C "$dir" reset --hard "origin/$BRANCH"
  else
    git clone --branch "$BRANCH" --depth 1 "https://github.com/${GITHUB_USER}/${repo}.git" "$dir"
  fi
}

clone_or_pull uco-parking ucoparking-back
clone_or_pull UcoParkingFront ucoParking-front
clone_or_pull uco-parking-config-server uco-parking-config-server

if [[ ! -d uco-parking/deploy ]]; then
  echo "Falta uco-parking/deploy en el repo ucoparking-back."
  exit 1
fi

mkdir -p deploy
cp -r uco-parking/deploy/. deploy/
chmod +x deploy/deploy.sh deploy/scripts/*.sh 2>/dev/null || true

echo "Stack listo en $TARGET (rama $BRANCH)"
