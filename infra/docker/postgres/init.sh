#!/usr/bin/env bash

set -euo pipefail

echo "Initializing CloudStore databases..."

psql \
  --username "$POSTGRES_USER" \
  --dbname "$POSTGRES_DB" \
  --set=ON_ERROR_STOP=1 \
  --set=auth_db_name="$AUTH_DB_NAME" \
  --set=auth_db_user="$AUTH_DB_USER" \
  --set=auth_password="$AUTH_DB_PASSWORD" \
  --set=catalog_db_name="$CATALOG_DB_NAME" \
  --set=catalog_db_user="$CATALOG_DB_USER" \
  --set=catalog_password="$CATALOG_DB_PASSWORD" \
  --set=inventory_db_name="$INVENTORY_DB_NAME" \
  --set=inventory_db_user="$INVENTORY_DB_USER" \
  --set=inventory_password="$INVENTORY_DB_PASSWORD" \
  --set=order_db_name="$ORDER_DB_NAME" \
  --set=order_db_user="$ORDER_DB_USER" \
  --set=order_password="$ORDER_DB_PASSWORD" \
<<-'EOSQL'

CREATE USER :"auth_db_user"
    WITH PASSWORD :'auth_password';

CREATE DATABASE :"auth_db_name"
    OWNER :"auth_db_user";


CREATE USER :"catalog_db_user"
    WITH PASSWORD :'catalog_password';

CREATE DATABASE :"catalog_db_name"
    OWNER :"catalog_db_user";


CREATE USER :"inventory_db_user"
    WITH PASSWORD :'inventory_password';

CREATE DATABASE :"inventory_db_name"
    OWNER :"inventory_db_user";


CREATE USER :"order_db_user"
    WITH PASSWORD :'order_password';

CREATE DATABASE :"order_db_name"
    OWNER :"order_db_user";

EOSQL

echo "CloudStore databases initialized successfully."