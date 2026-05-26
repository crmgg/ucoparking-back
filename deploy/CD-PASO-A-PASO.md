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

Secrets en GitHub → repo **ucoparking-back** → Settings → Secrets:

| Secret | Valor |
|--------|--------|
| `DEPLOY_VM_HOST` | IP de la VM |
| `DEPLOY_VM_USER` | `ubuntu` |
| `DEPLOY_VM_SSH_KEY` | contenido del `.pem` (privada) |

Push a `feature/notification-gateway` dispara el deploy (o usa **Run workflow** manual).

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
