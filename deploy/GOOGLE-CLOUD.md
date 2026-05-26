# Despliegue en Google Cloud (gratis / trial)

Guia para dejar **todo el stack online** con Docker + CD desde GitHub Actions.

Usa el mismo `docker-compose.prod.yml` que Oracle; solo cambia **donde creas la VM**.

---

## Requisitos

1. Cuenta Google Cloud: [cloud.google.com/free](https://cloud.google.com/free) → **Empezar gratis**
2. Tarjeta para verificacion (no cobran si no activas facturacion completa; trial = **$300 / 90 dias**)
3. Repo en GitHub con el monorepo (carpetas `uco-parking`, `UcoParkingFront`, `deploy`, etc.)

> **Region recomendada (capa gratis):** `us-central1`, `us-east1` o `us-west1`  
> **Maquina:** `e2-micro` (1 vCPU, 1 GB RAM — justo para demo; el primer deploy tarda ~15 min)

---

## Paso 1 — Crear proyecto y VM

1. Entra a [console.cloud.google.com](https://console.cloud.google.com)
2. Arriba → **Seleccionar proyecto** → **Proyecto nuevo** → nombre: `uco-parking`
3. Menu ☰ → **Compute Engine** → **Instancias de VM** → **Crear instancia**

| Campo | Valor |
|-------|--------|
| Nombre | `uco-parking-vm` |
| Region | `us-central1` (Iowa) |
| Zona | cualquiera |
| Serie | E2 |
| Tipo | `e2-micro` |
| SO | **Ubuntu 22.04 LTS** |
| Disco | 30 GB balanced |
| Firewall | Marca **Permitir trafico HTTP** |

4. **Crear**

Anota la **IP externa** (ej. `34.123.45.67`).

### Abrir puerto 8000 (Kong)

Menu ☰ → **Red de VPC** → **Firewall** → **Crear regla de firewall**:

| Campo | Valor |
|-------|--------|
| Nombre | `allow-uco-kong` |
| Destinos | Todas las instancias en la red |
| Filtro origen | `0.0.0.0/0` |
| Protocolos | TCP → **8000** |

O desde Cloud Shell:

```bash
gcloud compute firewall-rules create allow-uco-kong \
  --allow=tcp:8000 \
  --source-ranges=0.0.0.0/0 \
  --description="Kong API UCO Parking"
```

Puertos finales:

| Puerto | Uso |
|--------|-----|
| 22 | SSH |
| 80 | Frontend |
| 8000 | WAF (entrada API) → Kong |

---

## Paso 2 — Conectarte por SSH

En la lista de VMs → boton **SSH** (abre terminal en el navegador).

O desde tu PC (Windows PowerShell):

```powershell
# Genera clave si no tienes (una sola vez)
ssh-keygen -t ed25519 -f $env:USERPROFILE\.ssh\gcp-uco -N '""'

# Sube la clave publica en: VM → Editar → Claves SSH → Agregar item
# Pega el contenido de gcp-uco.pub

ssh -i $env:USERPROFILE\.ssh\gcp-uco TU_USUARIO@IP_EXTERNA
```

En VM Ubuntu de Google el usuario suele ser tu cuenta de Google o un usuario que elijas al configurar SSH.

---

## Paso 3 — Instalar Docker y desplegar (una vez)

Dentro de la VM:

```bash
cd ~
git clone https://github.com/TU_USUARIO/TU_REPO_MONOREPO.git uco-parking-tmp
bash uco-parking-tmp/deploy/scripts/setup-vm.sh
```

**Cierra la sesion SSH y vuelve a entrar.**

```bash
sudo mkdir -p /opt/uco-parking
sudo chown $USER:$USER /opt/uco-parking
git clone https://github.com/TU_USUARIO/TU_REPO_MONOREPO.git /opt/uco-parking
cd /opt/uco-parking/deploy
cp .env.example .env
nano .env
bash deploy.sh
```

### Editar `.env` (importante)

```env
PUBLIC_HOST=34.123.45.67
FRONTEND_PUBLIC_URL=http://34.123.45.67
VITE_API_BASE_URL=http://34.123.45.67:8000
VITE_AUTH0_DOMAIN=dev-oh62f3hilgh8p2rj.us.auth0.com
VITE_AUTH0_CLIENT_ID=tu-client-id
VITE_AUTH0_AUDIENCE=https://uco-parking-api
DB_PASSWORD=UnaPasswordFuerte123!
```

`DB_PASSWORD` debe tener mayusculas, minusculas, numeros y simbolos (SQL Server lo exige).

### Probar

- Front: `http://IP_EXTERNA`
- Health: `http://IP_EXTERNA:8000/uco-parking/actuator/health`

La primera vez espera **2–5 minutos** (SQL Server arranca lento).

---

## Paso 4 — Auth0

Application (SPA) → Settings:

- **Allowed Callback URLs:** `http://TU_IP`
- **Allowed Logout URLs:** `http://TU_IP`
- **Allowed Web Origins:** `http://TU_IP`

---

## Paso 5 — CD automatico (GitHub Actions)

Repo → **Settings → Secrets and variables → Actions** → New secret:

| Secret | Valor |
|--------|--------|
| `DEPLOY_VM_HOST` | IP externa de la VM |
| `DEPLOY_VM_USER` | usuario SSH (ej. tu usuario de Google o `ubuntu`) |
| `DEPLOY_VM_SSH_KEY` | clave **privada** completa (`.pem` o sin extension) |

El workflow `.github/workflows/cd.yml` en cada push a `main`:

1. SSH a la VM  
2. `git pull`  
3. `bash deploy.sh`

Prueba manual: **Actions → CD → Run workflow**.

---

## Arquitectura

```text
Internet
   │
   ├─ :80   → frontend (Vue + nginx)
   └─ :8000 → WAF → Kong → backend:8080
                      ├─ config-server:8888
                      ├─ sqlserver:1433
                      └─ redis:6379
```

---

## Comandos utiles

```bash
cd /opt/uco-parking/deploy
docker compose -f docker-compose.prod.yml ps
docker compose -f docker-compose.prod.yml logs -f backend
bash deploy.sh
docker compose -f docker-compose.prod.yml down
```

---

## Problemas frecuentes

| Problema | Solucion |
|----------|----------|
| No entra por :8000 | Crear regla firewall `allow-uco-kong` |
| SQL Server reinicia | Password debil en `DB_PASSWORD` |
| VM muy lenta / OOM | Normal en e2-micro; espera o sube a `e2-small` (gasta creditos) |
| Auth0 error | URLs exactas con `http://IP` sin barra final |
| Build falla por memoria | `sudo fallocate -l 2G /swapfile && sudo mkswap /swapfile && sudo swapon /swapfile` |

---

## Costes

- **Trial:** $300 gratis 90 dias (suficiente para el semestre)
- **Always Free:** `e2-micro` en ciertas regiones US (720 h/mes) — puede quedar gratis si apagas la VM cuando no la uses
- **Apagar VM** cuando no presentes: Compute Engine → Detener instancia (no pagas CPU; si conservas IP estatica puede haber cargo minimo)

---

## Para el informe / profe

- **CI:** GitHub Actions compila en cada push/PR  
- **CD:** GitHub Actions despliega a VM Google Cloud en cada push a `main`  
- **Infra:** Docker Compose en Compute Engine  
- **Gateway:** Kong en puerto 8000  
