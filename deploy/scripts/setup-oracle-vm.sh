#!/usr/bin/env bash
# Ejecutar UNA VEZ en la VM Ubuntu de Oracle Cloud (como usuario con sudo).
set -euo pipefail

echo "=== Instalando Docker en Ubuntu ==="
sudo apt-get update
sudo apt-get install -y ca-certificates curl git
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "${VERSION_CODENAME}") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
sudo usermod -aG docker "$USER"

echo ""
echo "Docker instalado. Cierra sesion SSH y vuelve a entrar para usar docker sin sudo."
echo ""
echo "Siguiente paso (despues de reconectar):"
echo "  sudo mkdir -p /opt/uco-parking"
echo "  sudo chown \$USER:\$USER /opt/uco-parking"
echo "  git clone <URL-DE-TU-REPO-MONOREPO> /opt/uco-parking"
echo "  cd /opt/uco-parking/deploy"
echo "  cp .env.example .env   # editar con IP publica y secretos"
echo "  bash deploy.sh"
