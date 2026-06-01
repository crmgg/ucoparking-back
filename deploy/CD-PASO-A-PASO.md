# CD — paso a paso (Oracle Cloud, repos separados)

## Parte A — Primera vez (~1 h)

### 1. Crear VM Oracle (AMD x86)
- Ubuntu 22.04, shape `VM.Standard.E2.1.Micro` (Always Free)
- IP pública + descargar clave `.pem`
- Abrir puertos **22, 80, 8000**

### 2. Conectar por SSH (PowerShell)
```powershell
ssh -i C:\ruta\a\tu-clave.pem ubuntu@TU_IP
```

### 3. Instalar Docker (en la VM)
```bash
curl -fsSL https://raw.githubusercontent.com/crmgg/ucoparking-back/feature/notification-gateway/deploy/scripts/setup-vm.sh | bash
```
> Cierra SSH y vuelve a entrar.

### 4. Clonar stack + configurar
```bash
sudo mkdir -p /opt/uco-parking
sudo chown $USER:$USER /opt/uco-parking
cd /opt/uco-parking

# Primera vez: clona solo el back para obtener deploy/
git clone --branch feature/notification-gateway --depth 1 https://github.com/crmgg/ucoparking-back.git uco-parking
cp -r uco-parking/deploy .
chmod +x deploy/deploy.sh deploy/scripts/*.sh

GITHUB_USER=crmgg BRANCH=feature/notification-gateway bash deploy/scripts/clone-stack.sh

cd deploy
cp .env.example .env
nano .env   # IP, Auth0, DB_PASSWORD
bash deploy.sh
```

### 5. Probar
- Front: `http://TU_IP`
- API: `http://TU_IP:8000/uco-parking/actuator/health`

### 6. Auth0
En la app SPA → Allowed Callback / Logout / Web Origins:
- `http://TU_IP`

---

## Parte B — CD automático (~15 min)

Secrets en GitHub → repo **ucoparking-back** → **Settings → Secrets and variables → Actions** (nivel **repositorio**, no solo Environment):

| Secret | Valor |
|--------|--------|
| `DEPLOY_VM_HOST` | IP de la VM |
| `DEPLOY_VM_USER` | usuario SSH de la VM (GCP: ej. `claryrivillaas`; Oracle: `ubuntu`) |
| `DEPLOY_VM_SSH_KEY` | contenido del `.pem` (privada, con `-----BEGIN` y `-----END`) |

**Importante:** el workflow CD ya **no** usa `environment: production`. Si creaste un Environment llamado `production` con reglas que solo permiten `main`, eso causaba fallos en ~8 s con la rama `feature/notification-gateway`.

**Primera vez en la VM** (obligatorio antes del CD automático):

```bash
sudo mkdir -p /opt/uco-parking
sudo chown $USER:$USER /opt/uco-parking
cd /opt/uco-parking
GITHUB_USER=crmgg BRANCH=feature/notification-gateway bash -c '
  git clone --branch feature/notification-gateway --depth 1 https://github.com/crmgg/ucoparking-back.git _boot
  mkdir -p deploy && cp -r _boot/deploy/. deploy/ && rm -rf _boot
  chmod +x deploy/deploy.sh deploy/scripts/*.sh
'
cd deploy
cp .env.example .env
nano .env   # IP, Auth0, DB_PASSWORD
bash deploy.sh   # primera vez ~15 min
```

Push a `feature/notification-gateway` dispara el deploy (o usa **Run workflow** manual).

### Si CD falla en ~8–10 segundos

| Causa | Solución |
|-------|----------|
| Secrets vacíos o mal nombrados | Revisa los 3 secrets en Actions (no en Environment) |
| Usuario SSH incorrecto | GCP: usuario de tu cuenta, no siempre `ubuntu` |
| Clave `.pem` incompleta | Pega la clave privada entera en `DEPLOY_VM_SSH_KEY` |
| Puerto 22 cerrado | Firewall GCP/Oracle: permitir TCP 22 desde `0.0.0.0/0` |
| Falta `.env` en la VM | Crea `/opt/uco-parking/deploy/.env` (ver arriba) |

---

## Actualizar manualmente en la VM
```bash
GITHUB_USER=crmgg BRANCH=feature/notification-gateway bash /opt/uco-parking/deploy/scripts/cd-remote.sh
```

---

## Notas
- **Local no cambia** — esto solo afecta la VM.
- Primer `deploy.sh` tarda ~15–20 min (build Docker + SQL Server).
- `DB_PASSWORD` debe tener mayúsculas, minúsculas, números y símbolos.
