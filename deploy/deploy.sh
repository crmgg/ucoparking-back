#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

if [[ ! -f .env ]]; then
  echo "Falta deploy/.env — copia .env.example y completa PUBLIC_HOST, Auth0 y DB_PASSWORD."
  exit 1
fi

set -a
# shellcheck disable=SC1091
source .env
set +a

if [[ -z "${FRONTEND_PUBLIC_URL:-}" ]]; then
  echo "FRONTEND_PUBLIC_URL vacio en .env"
  exit 1
fi

mkdir -p kong
sed "s|__FRONTEND_ORIGIN__|${FRONTEND_PUBLIC_URL}|g" kong/kong.prod.yml.template > kong/kong.prod.yml

echo "Desplegando stack UCO Parking..."
docker compose -f docker-compose.prod.yml --env-file .env up -d --build

echo ""
echo "Estado de contenedores:"
docker compose -f docker-compose.prod.yml ps

echo ""
echo "Front:  ${FRONTEND_PUBLIC_URL}"
echo "API (WAF): ${VITE_API_BASE_URL:-http://${PUBLIC_HOST}:8000}/uco-parking/actuator/health"
echo "Health: ${VITE_API_BASE_URL:-http://${PUBLIC_HOST}:8000}/uco-parking/actuator/health"
