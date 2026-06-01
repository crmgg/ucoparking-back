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

COMPOSE=(docker compose -f docker-compose.prod.yml --env-file .env)

cleanup_uco_containers() {
  echo "Limpiando contenedores uco (incluye prefijos viejos de compose)..."
  "${COMPOSE[@]}" down --remove-orphans 2>/dev/null || true

  # uco-config-server y 0f2d88ac2f66_uco-config-server, etc.
  mapfile -t names < <(docker ps -a --format '{{.Names}}' | grep -E '(^|_)uco-' || true)
  for name in "${names[@]:-}"; do
    [[ -n "$name" ]] && docker rm -f "$name" 2>/dev/null || true
  done

  mapfile -t legacy < <(docker ps -a --format '{{.Names}}' | grep -E 'config-server|uco-parking-prod' || true)
  for name in "${legacy[@]:-}"; do
    [[ -n "$name" ]] && docker rm -f "$name" 2>/dev/null || true
  done
}

echo "Desplegando stack UCO Parking..."
cleanup_uco_containers

"${COMPOSE[@]}" up -d --build --remove-orphans --force-recreate

echo ""
echo "Estado de contenedores:"
"${COMPOSE[@]}" ps

echo ""
echo "Front:  ${FRONTEND_PUBLIC_URL}"
echo "API (WAF): ${VITE_API_BASE_URL:-http://${PUBLIC_HOST}:8000}/uco-parking/actuator/health"
echo "Health: ${VITE_API_BASE_URL:-http://${PUBLIC_HOST}:8000}/uco-parking/actuator/health"
