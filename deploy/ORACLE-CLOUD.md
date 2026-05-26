# Despliegue gratis en Oracle Cloud (Always Free)

Guia para dejar **todo el stack online** con Docker y CD automatico desde GitHub Actions.

## Requisitos

- Cuenta Oracle Cloud (gratis): https://www.oracle.com/cloud/free/
- Repo en GitHub con el monorepo completo **o** repos separados + carpeta `deploy/` (ver abajo)
- **VM x86 (AMD)** — **no uses ARM Ampere**; SQL Server en Docker no funciona en ARM.

### Repos separados (tu caso actual)

Si tienes `ucoparking-back`, `ucoParking-front` y `uco-parking-config-server` por separado:

1. Sube también la carpeta `deploy/` a GitHub (puede ser un 4.º repo `uco-parking-deploy` o un monorepo nuevo).
2. En la VM usa `deploy/scripts/clone-stack.sh` y copia `deploy/` a `/opt/uco-parking/deploy`.

Lo mas simple para CD: crea un repo **`uco-parking-stack`** con **toda** la carpeta del proyecto (como en tu PC) y usa ese para el despliegue.

---

## Paso 1 — Crear la VM en Oracle

1. Oracle Console → **Compute** → **Instances** → **Create instance**
2. Nombre: `uco-parking-vm`
3. **Image:** Ubuntu 22.04
4. **Shape:** `VM.Standard.E2.1.Micro` (AMD, Always Free) o similar **x86**
5. Red: asigna IP publica
6. Descarga la **claves SSH** (.pem) al crear

### Abrir puertos (Security List / NSG)

| Puerto | Uso |
|--------|-----|
| 22 | SSH |
| 80 | Frontend (nginx) |
| 8000 | WAF (entrada API) → Kong |

No expongas 8080, 8888 ni 1433 a internet.

---

## Paso 2 — Preparar la VM (una sola vez)

Conectate por SSH:

```bash
ssh -i tu-clave.pem ubuntu@TU_IP_PUBLICA
```

Ejecuta:

```bash
curl -fsSL https://raw.githubusercontent.com/TU_USUARIO/TU_REPO/main/deploy/scripts/setup-oracle-vm.sh | bash
```

O clona el repo y corre:

```bash
bash deploy/scripts/setup-oracle-vm.sh
```

**Cierra sesion SSH y vuelve a entrar** (grupo docker).

Clona el monorepo:

```bash
sudo mkdir -p /opt/uco-parking
sudo chown $USER:$USER /opt/uco-parking
git clone https://github.com/TU_USUARIO/TU_REPO_MONOREPO.git /opt/uco-parking
cd /opt/uco-parking/deploy
cp .env.example .env
nano .env   # completa IP, Auth0, DB_PASSWORD
bash deploy.sh
```

Prueba:

- Front: `http://TU_IP`
- API health: `http://TU_IP:8000/uco-parking/actuator/health`

---

## Paso 3 — Auth0

En Auth0 Dashboard → Application (SPA):

- **Allowed Callback URLs:** `http://TU_IP`
- **Allowed Logout URLs:** `http://TU_IP`
- **Allowed Web Origins:** `http://TU_IP`

En `.env` de deploy:

```env
FRONTEND_PUBLIC_URL=http://TU_IP
VITE_API_BASE_URL=http://TU_IP:8000
```

Cada cambio en `.env` del front requiere rebuild:

```bash
cd /opt/uco-parking/deploy && bash deploy.sh
```

---

## Paso 4 — CD automatico (GitHub Actions)

En GitHub → repo monorepo → **Settings → Secrets and variables → Actions**:

| Secret | Valor |
|--------|--------|
| `OCI_VM_HOST` | IP publica de la VM |
| `OCI_VM_USER` | `ubuntu` |
| `OCI_VM_SSH_KEY` | contenido del archivo `.pem` (privada) |
| `OCI_VM_APP_DIR` | `/opt/uco-parking` (opcional) |

El workflow `.github/workflows/cd.yml` hace en cada push a `main`:

1. SSH a la VM
2. `git pull`
3. `deploy/deploy.sh` (rebuild Docker)

Tambien puedes lanzarlo manualmente en **Actions → CD - Oracle Cloud → Run workflow**.

---

## Arquitectura en produccion

```text
Internet
   │
   ├─ :80   → frontend (Vue + nginx)
   └─ :8000 → WAF → Kong → backend:8080
                      │
                      ├─ config-server:8888
                      ├─ sqlserver:1433
                      └─ redis:6379
```

---

## Comandos utiles en la VM

```bash
cd /opt/uco-parking/deploy
docker compose -f docker-compose.prod.yml ps
docker compose -f docker-compose.prod.yml logs -f backend
bash deploy.sh                    # redesplegar
docker compose -f docker-compose.prod.yml down   # parar todo
```

---

## Problemas frecuentes

| Problema | Solucion |
|----------|----------|
| SQL Server no arranca | Password fuerte en `DB_PASSWORD` (mayus, minus, numero, simbolo) |
| Front sin login Auth0 | Revisar URLs en Auth0 y variables `VITE_*` en `.env` |
| API 502 en Kong | Espera 2–3 min; backend depende de SQL. `docker logs uco-backend` |
| Build lento en VM free | Normal; primera vez tarda ~10–15 min |

---

## Para el informe / profe

- **CI:** `.github/workflows/ci.yml` — compila en cada push/PR
- **CD:** `.github/workflows/cd.yml` — despliega a Oracle VM en cada push a `main`
- **Infra:** Docker Compose en VM Always Free (Oracle Cloud)
- **Gateway:** Kong `:8000` delante del backend
