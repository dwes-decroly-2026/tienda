# Versión a desplegar

**RC a desplegar:** 2.15.0.3

---

# Cambios necesarios para realizar la actualización

## Cambios en infraestructura

### RC 2.15.0.3

Se añade un nuevo recurso al despliegue del componente **EJEMPLO_COMP_Servicio-Ejemplo**:

- Nuevo volumen persistente `ejemplo-data` (5 Gi) montado en `/data/ejemplo`.
- Aumento de memoria del contenedor: `limits.memory` de 1Gi a 2Gi.

## Cambios en API

### RC 2.15.0.3

- Nuevo endpoint `POST /api/v2/ejemplo/validate`: valida un documento y devuelve el resultado de la validación.
- El campo `status` de la respuesta de `GET /api/v2/ejemplo/{id}` pasa a devolver los valores `PENDING`, `OK` y `KO` (antes `0`, `1`, `2`).

## Cambios en configuración

### RC 2.15.0.3

Variables de entorno añadidas:

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `EJEMPLO_TIMEOUT` | `30000` | Timeout en ms de las llamadas al servicio externo |
| `EJEMPLO_API_URL` | `https://ejemplo.dominio.local/api` | URL del servicio externo de validación |

Variables de entorno eliminadas:

| Variable |
|---|
| `EJEMPLO_LEGACY_URL` |

## Tareas previas al despliegue

### RC 2.15.0.3

Hay que crear la propiedad `ejemplo.validacion.config` para cada tenant, en base al entorno.

\*Debe agregarse el valor de **WRITE_YOUR_TENANT_HERE** y **WRITE_YOUR_API_KEY_HERE** del entorno correspondiente.

```
/ejemplo/tools/populate-cs-properties$ python3 config-tool.py -a post -t WRITE_YOUR_TENANT_HERE -c ejemplo -m comp -p ejemplo.validacion.config -v
"{\"enabled\": true,\"apiKey\": \"WRITE_YOUR_API_KEY_HERE\",\"retries\": 3}"
```

## Tareas posteriores al despliegue

### RC 2.15.0.3

Sin cambios

## Rollback

### RC 2.15.0.3

Ejecutar los siguientes scripts del migrador para revertir los cambios de base de datos:

```sql
-- V2_15_0_1__rollback_add_validation_table.sql
DROP TABLE IF EXISTS ejemplo_validation;
ALTER TABLE ejemplo_document DROP COLUMN IF EXISTS validation_status;
```

Restaurar las variables de entorno eliminadas y desplegar la RC anterior (2.14.0.2).

# Historial de cambios

| Fecha | Autor | Descripción |
|---|---|---|
| 2026-11-18 | Nombre Apellido | Creación de la página |
| 2026-11-20 | Nombre Apellido | Añadidas tareas previas al despliegue |
