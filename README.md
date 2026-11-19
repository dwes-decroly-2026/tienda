# Tienda Decroly

API REST de la tienda de informática del centro, hecha con Spring Boot. Gestiona el catálogo de productos (alta, consulta, modificación y borrado), calcula el precio con IVA en euros o en otras monedas y documenta todos sus endpoints con OpenAPI.

En esta versión los productos se guardan **en memoria**: se pierden al parar la aplicación. Se pueden cargar de tres orígenes (ver *Configuración*).

## Requisitos

| Herramienta | Versión |
|---|---|
| Java (JDK) | 21 o superior |
| IntelliJ IDEA | Community o Ultimate |
| Maven | No hace falta instalarlo: el proyecto trae el *wrapper* (`mvnw` / `mvnw.cmd`) |
| Postman | Para probar la API con la colección incluida |
| Conexión a Internet | Solo para los precios en otras monedas y para el origen de datos `api` |

## Cómo arrancar la aplicación

**Desde IntelliJ:** abre la carpeta del proyecto, espera a que Maven descargue las dependencias y ejecuta `TiendaDecrolyApplication`.

**Desde una terminal**, en la carpeta del proyecto:

```
mvnw.cmd spring-boot:run        (Windows)
./mvnw spring-boot:run          (Linux y macOS)
```

La aplicación queda escuchando en `http://localhost:8080`. Para comprobar que funciona, abre `http://localhost:8080/api/productos` en el navegador.

## Configuración

Todas las propiedades están en `src/main/resources`:

| Fichero | Contenido |
|---|---|
| `application.properties` | Propiedades comunes. Activa el perfil `dev` por defecto. |
| `application-dev.properties` | Desarrollo: nombre de la tienda con la marca (DESARROLLO) y log del paquete `es.decroly.tienda` en DEBUG. |
| `application-prod.properties` | Producción: carga los productos del fichero `productos.json`. |

### Propiedades propias

| Propiedad | Valor por defecto | Para qué sirve |
|---|---|---|
| `tienda.nombre` | Tienda Decroly | Nombre de la tienda. |
| `tienda.iva` | 21 | IVA, en %, que se aplica al calcular el precio. |
| `tienda.envio-gratis-desde` | 50 | Importe a partir del cual el envío es gratis. |
| `tienda.productos.origen` | memoria | De dónde salen los productos al arrancar: `memoria` (3 productos escritos en el código), `json` (4 productos de `productos.json`) o `api` (20 productos de DummyJSON). |
| `apis.catalogo-url` | https://dummyjson.com | URL base del catálogo externo (origen `api`). |
| `apis.divisas-url` | https://api.frankfurter.dev/v1 | URL base del servicio de tipos de cambio. |
| `apis.timeout-segundos` | 3 | Tiempo máximo de espera al llamar al servicio de divisas. |

### Cambiar de perfil

- En IntelliJ (Community o Ultimate): *Run › Edit Configurations…*, y en *Environment variables* añade `SPRING_PROFILES_ACTIVE=prod`.
- En una terminal: `mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod`.

Cualquier propiedad se puede sustituir también con una variable de entorno, que tiene más prioridad que los ficheros (por ejemplo, `TIENDA_IVA=10`).

## Endpoints

### Productos

| Método y ruta | Qué hace | Respuestas |
|---|---|---|
| `GET /api/productos` | Lista todos los productos | 200 |
| `GET /api/productos/{id}` | Devuelve un producto | 200 · 404 |
| `POST /api/productos` | Crea un producto. El id lo asigna la tienda. | 201 (con cabecera `Location`) · 400 |
| `PUT /api/productos/{id}` | Modifica nombre, precio y stock | 200 · 400 · 404 |
| `DELETE /api/productos/{id}` | Borra un producto | 204 · 404 |
| `GET /api/productos/{id}/precio?moneda=USD` | Precio con IVA en euros (por defecto) o en otra moneda | 200 · 400 · 404 · 503 |

Ejemplo de cuerpo para crear o modificar un producto:

```json
{
  "nombre": "Alfombrilla XL",
  "precio": 12.50,
  "stock": 60
}
```

Reglas: el nombre es obligatorio y el precio no puede ser negativo.

### Endpoints de prueba

| Método y ruta | Qué hace |
|---|---|
| `GET /saludo` | Texto de bienvenida |
| `GET /hola/{nombre}` | Saludo personalizado (variable de ruta) |
| `GET /buscar?texto=raton` | Ejemplo de parámetro de consulta (por defecto, `todo`) |
| `GET /precio/{base}?iva=21` | Precio con IVA a partir de una base (por defecto, IVA del 21 %) |

### Errores

Todos los errores de la API se devuelven en formato **ProblemDetail** (`application/problem+json`) desde un manejador global:

| Código | Título | Cuándo |
|---|---|---|
| 400 | Peticion incorrecta | Nombre vacío, precio negativo o moneda no admitida |
| 404 | Recurso no encontrado | El producto no existe |
| 503 | Servicio externo no disponible | El servicio de divisas no responde o falla |

```json
{
  "type": "about:blank",
  "title": "Recurso no encontrado",
  "status": 404,
  "detail": "No existe el producto 99",
  "instance": "/api/productos/99"
}
```

## Documentación de la API

Con la aplicación arrancada:

| Dirección | Contenido |
|---|---|
| `http://localhost:8080/swagger-ui.html` | Swagger UI: documentación interactiva para probar los endpoints |
| `http://localhost:8080/v3/api-docs` | Especificación OpenAPI en JSON |
| `http://localhost:8080/v3/api-docs.yaml` | Especificación OpenAPI en YAML |

Para guardar la especificación en el repositorio, descarga `/v3/api-docs.yaml` y guárdala como `docs/openapi.yaml`.

Las interfaces de los servicios (`ProductoService`) están documentadas con Javadoc.

## Pruebas

### Pruebas automáticas

```
mvnw.cmd test        (Windows)
./mvnw test          (Linux y macOS)
```

| Clase de prueba | Qué comprueba |
|---|---|
| `ProductoServiceMemoriaTest` | Listar, buscar, crear y borrar productos, y los errores de producto inexistente y precio negativo. |
| `PrecioServiceTest` | El cálculo del precio con un IVA del 21 % y del 10 %, y el error de producto inexistente. |
| `TiendaDecrolyApplicationTests` | Que la aplicación arranca. |

Las pruebas no necesitan Internet.

### Colección de Postman

En la carpeta `postman/` están la colección `tienda-decroly.postman_collection.json` y el entorno `local.postman_environment.json`.

1. En Postman, pulsa *Import* y selecciona los dos ficheros.
2. Elige el entorno `local` y revisa las variables de la colección:
   - `origen`: el valor de `tienda.productos.origen` (`memoria`, `json` o `api`).
   - `iva`: el valor de `tienda.iva`.
3. Arranca la aplicación y ejecuta la colección completa con *Run collection*, en orden.

Para repetir la ejecución, reinicia antes la aplicación: los datos viven en memoria.

## Estructura del proyecto

```
tienda-decroly/
├── src/main/java/es/decroly/tienda/
│   ├── TiendaDecrolyApplication.java
│   ├── config/        Propiedades propias (TiendaProperties, ApisProperties) y OpenAPI
│   ├── controllers/   Controladores REST
│   ├── domain/        Producto
│   ├── dto/           Datos de entrada y salida (precios, respuestas de las APIs externas)
│   ├── exceptions/    Excepciones propias y manejador global de errores
│   └── services/      Interfaces e implementaciones de los servicios
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   ├── application-prod.properties
│   └── productos.json
├── src/test/java/es/decroly/tienda/   Pruebas
├── postman/           Colección y entorno de Postman
└── pom.xml
```

## Tecnologías

Spring Boot 4 (Spring MVC), Lombok, springdoc-openapi, RestClient, JUnit 5.
