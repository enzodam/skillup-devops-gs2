#!/bin/bash
set -e  

# ======================================================
#   VARIÁVEIS DA INFRA
# ======================================================
RESOURCE_GROUP="rg-skillup"
LOCATION="canadacentral"

# PostgreSQL
POSTGRES_SERVER="pg-skillup-server"
POSTGRES_DB="skillupdb"
POSTGRES_USER="skillupadmin"
POSTGRES_PASSWORD="SkillUp@2024!"   # senha fixa usada na GS

# App Service
PLAN_NAME="plan-skillup"
WEBAPP_NAME="skillup-api-web"
RUNTIME="JAVA:17-java17"

echo "========================================"
echo "  CRIANDO RECURSOS DA GS - SKILLUP"
echo "  RG:    $RESOURCE_GROUP"
echo "  LOCAL: $LOCATION"
echo "========================================"

# ======================================================
echo "[1/6] Criando Resource Group..."
az group create \
  --name "$RESOURCE_GROUP" \
  --location "$LOCATION"

# ======================================================
echo "[2/6] Criando Servidor PostgreSQL..."
az postgres flexible-server create \
  --name "$POSTGRES_SERVER" \
  --resource-group "$RESOURCE_GROUP" \
  --location "$LOCATION" \
  --admin-user "$POSTGRES_USER" \
  --admin-password "$POSTGRES_PASSWORD" \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --version 14 \
  --storage-size 32 \
  --yes

# ======================================================
echo "[3/6] Criando Database skillupdb..."
az postgres flexible-server db create \
  --resource-group "$RESOURCE_GROUP" \
  --server-name "$POSTGRES_SERVER" \
  --database-name "$POSTGRES_DB"

# ======================================================
echo "[4/6] Liberando acesso público para o PostgreSQL..."
az postgres flexible-server firewall-rule create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$POSTGRES_SERVER" \
  --rule-name "AllowAll" \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 255.255.255.255

# ======================================================
echo "[5/6] Criando App Service Plan F1..."
az appservice plan create \
  --name "$PLAN_NAME" \
  --resource-group "$RESOURCE_GROUP" \
  --location "$LOCATION" \
  --sku F1 \
  --is-linux

# ======================================================
echo "[6/6] Criando Web App Java 17..."
az webapp create \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP" \
  --plan "$PLAN_NAME" \
  --runtime "$RUNTIME"

echo "========================================"
echo "  TUDO CERTO! Infra criada:"
echo "  - Resource Group: $RESOURCE_GROUP"
echo "  - PostgreSQL:     $POSTGRES_SERVER / DB: $POSTGRES_DB"
echo "  - WebApp:         $WEBAPP_NAME"
echo "========================================"
