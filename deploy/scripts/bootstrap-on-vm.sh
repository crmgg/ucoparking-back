#!/usr/bin/env bash
# Crea deploy/ + Dockerfiles en /opt/uco-parking sin subir zip.
# Uso en la VM: bash bootstrap-on-vm.sh
set -euo pipefail

TARGET="${TARGET:-/opt/uco-parking}"
DEPLOY="$TARGET/deploy"

if [[ ! -d "$TARGET/uco-parking" || ! -d "$TARGET/UcoParkingFront" ]]; then
  echo "Faltan repos en $TARGET — clona uco-parking y UcoParkingFront primero."
  exit 1
fi

mkdir -p "$DEPLOY/kong" "$DEPLOY/scripts"

cat > "$DEPLOY/docker-compose.prod.yml" <<'EOF'
name: uco-parking-prod

services:
  sqlserver:
    image: mcr.microsoft.com/mssql/server:2022-latest
    platform: linux/amd64
    container_name: uco-sqlserver
    environment:
      ACCEPT_EULA: "Y"
      MSSQL_PID: "Developer"
      MSSQL_SA_PASSWORD: ${DB_PASSWORD}
    volumes:
      - sql_data:/var/opt/mssql
    healthcheck:
      test: ["CMD-SHELL", "/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P \"$$MSSQL_SA_PASSWORD\" -Q \"SELECT 1\" -C || exit 1"]
      interval: 15s
      timeout: 10s
      retries: 12
      start_period: 45s
    restart: unless-stopped
    networks:
      - uco-net

  redis:
    image: redis:7-alpine
    container_name: uco-redis
    restart: unless-stopped
    networks:
      - uco-net

  config-server:
    build:
      context: ../uco-parking-config-server
      dockerfile: Dockerfile
    container_name: uco-config-server
    depends_on:
      - redis
    restart: unless-stopped
    networks:
      - uco-net

  backend:
    build:
      context: ../uco-parking
      dockerfile: Dockerfile
    container_name: uco-backend
    environment:
      DB_HOST: sqlserver
      DB_PORT: "1433"
      DB_NAME: ucoparking
      DB_USERNAME: sa
      DB_PASSWORD: ${DB_PASSWORD}
      REDIS_HOST: redis
      REDIS_PORT: "6379"
      SPRING_CONFIG_IMPORT: optional:configserver:http://config-server:8888
      AUTH0_DOMAIN: ${AUTH0_DOMAIN}
      AUTH0_AUDIENCE: ${AUTH0_AUDIENCE}
      AUTH0_SECURITY_ENABLED: ${AUTH0_SECURITY_ENABLED:-false}
      NOTIFICATION_EMAIL_ENABLED: ${NOTIFICATION_EMAIL_ENABLED:-false}
      NOTIFICATION_FROM_EMAIL: ${NOTIFICATION_FROM_EMAIL:-}
      SMTP_HOST: ${SMTP_HOST:-smtp.gmail.com}
      SMTP_PORT: ${SMTP_PORT:-587}
      SMTP_USERNAME: ${SMTP_USERNAME:-}
      SMTP_PASSWORD: ${SMTP_PASSWORD:-}
    depends_on:
      sqlserver:
        condition: service_healthy
      redis:
        condition: service_started
      config-server:
        condition: service_started
    restart: unless-stopped
    networks:
      - uco-net

  frontend:
    build:
      context: ../UcoParkingFront
      dockerfile: Dockerfile
      args:
        VITE_AUTH0_DOMAIN: ${VITE_AUTH0_DOMAIN}
        VITE_AUTH0_CLIENT_ID: ${VITE_AUTH0_CLIENT_ID}
        VITE_AUTH0_AUDIENCE: ${VITE_AUTH0_AUDIENCE}
        VITE_API_BASE_URL: ${VITE_API_BASE_URL}
    container_name: uco-frontend
    ports:
      - "80:80"
    depends_on:
      - backend
    restart: unless-stopped
    networks:
      - uco-net

  kong:
    image: kong:3.9
    container_name: uco-kong
    environment:
      KONG_DATABASE: "off"
      KONG_DECLARATIVE_CONFIG: /kong/kong.yml
      KONG_PROXY_LISTEN: 0.0.0.0:8000
      KONG_ADMIN_LISTEN: 0.0.0.0:8001
    volumes:
      - ./kong/kong.prod.yml:/kong/kong.yml:ro
    depends_on:
      - backend
    restart: unless-stopped
    networks:
      - uco-net

  waf:
    image: nginx:1.27-alpine
    container_name: uco-waf
    ports:
      - "8000:80"
    volumes:
      - ./waf/nginx.conf:/etc/nginx/conf.d/default.conf:ro
    depends_on:
      - kong
      - frontend
    restart: unless-stopped
    networks:
      - uco-net

volumes:
  sql_data:

networks:
  uco-net:
    driver: bridge
EOF

cat > "$DEPLOY/deploy.sh" <<'EOF'
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
echo "API:    ${VITE_API_BASE_URL:-http://${PUBLIC_HOST}:8000}/uco-parking/actuator/health"
EOF
chmod +x "$DEPLOY/deploy.sh"

cat > "$DEPLOY/.env.example" <<'EOF'
PUBLIC_HOST=34.31.147.31
FRONTEND_PUBLIC_URL=http://34.31.147.31
VITE_AUTH0_DOMAIN=dev-oh62f3hilgh8p2rj.us.auth0.com
VITE_AUTH0_CLIENT_ID=tu-client-id
VITE_AUTH0_AUDIENCE=https://uco-parking-api
VITE_API_BASE_URL=http://34.31.147.31:8000
DB_PASSWORD=CambiaEstaPasswordSegura123!
AUTH0_DOMAIN=dev-oh62f3hilgh8p2rj.us.auth0.com
AUTH0_AUDIENCE=https://uco-parking-api
AUTH0_SECURITY_ENABLED=false
NOTIFICATION_EMAIL_ENABLED=false
EOF

cat > "$DEPLOY/kong/kong.prod.yml.template" <<'EOF'
_format_version: "3.0"

services:
  - name: uco-parking-backend
    url: http://backend:8080

routes:
  - name: uco-parking-route
    service: uco-parking-backend
    paths:
      - /uco-parking
    strip_path: false
    protocols:
      - http

plugins:
  - name: cors
    service: uco-parking-backend
    config:
      origins:
        - __FRONTEND_ORIGIN__
      methods:
        - GET
        - POST
        - PUT
        - PATCH
        - DELETE
        - OPTIONS
      headers:
        - Authorization
        - Content-Type
        - Accept
      exposed_headers:
        - Authorization
      credentials: false
      max_age: 3600

  - name: rate-limiting
    service: uco-parking-backend
    config:
      minute: 120
      policy: local
      fault_tolerant: true
      hide_client_headers: false
EOF

cat > "$TARGET/uco-parking/Dockerfile" <<'EOF'
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY src ./src
RUN ./mvnw -B package -DskipTests -Djava.version=21

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/uco-parking-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

if [[ -d "$TARGET/uco-parking-config-server" ]]; then
  cat > "$TARGET/uco-parking-config-server/Dockerfile" <<'EOF'
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY src ./src
RUN ./mvnw -B package -DskipTests -Djava.version=21

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
fi

cat > "$TARGET/UcoParkingFront/Dockerfile" <<'EOF'
FROM node:20-alpine AS build
WORKDIR /app

ARG VITE_AUTH0_DOMAIN
ARG VITE_AUTH0_CLIENT_ID
ARG VITE_AUTH0_AUDIENCE
ARG VITE_API_BASE_URL

ENV VITE_AUTH0_DOMAIN=$VITE_AUTH0_DOMAIN
ENV VITE_AUTH0_CLIENT_ID=$VITE_AUTH0_CLIENT_ID
ENV VITE_AUTH0_AUDIENCE=$VITE_AUTH0_AUDIENCE
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
EOF

cat > "$TARGET/UcoParkingFront/nginx.conf" <<'EOF'
server {
    listen 80;
    server_name _;
    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /health {
        access_log off;
        return 200 'ok';
        add_header Content-Type text/plain;
    }
}
EOF

echo "OK — creado:"
echo "  $DEPLOY/"
echo "  $TARGET/uco-parking/Dockerfile"
echo "  $TARGET/UcoParkingFront/Dockerfile + nginx.conf"
[[ -d "$TARGET/uco-parking-config-server" ]] && echo "  $TARGET/uco-parking-config-server/Dockerfile"
echo ""
echo "Siguiente:"
echo "  cd $DEPLOY"
echo "  cp .env.example .env && nano .env"
echo "  bash deploy.sh"
