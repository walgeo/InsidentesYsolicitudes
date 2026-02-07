# Sistema de Gestión de Incidentes y Solicitudes

Aplicación web para la gestión de incidentes y solicitudes de servicio con autenticación JWT, control de acceso basado en roles (RBAC) y cobertura de pruebas con JaCoCo.

## ⚡ Importante

**¿Qué necesitas instalar?**

```powershell
# 1. Tener Docker Desktop instalado
# 2. Ejecutar UNA línea de comando
docker-compose up -d estando situado en la carpeta incidentes y solicitudes

# 3. El proyecto está corriendo en http://localhost:4200  el usuario contraseña de acceso son los mismos "admin", sin comillas;
```

No necesitas instalar nada.

Todo está adentro del contenedor. Solo ejecuta Docker y listo.

---

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Levantamiento](#instalación-y-levantamiento)
- [Pruebas](#pruebas)
- [Acceso a la Aplicación](#acceso-a-la-aplicación)
- [Credenciales por Defecto](#credenciales-por-defecto)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Solución de Problemas](#solución-de-problemas)

## 🎯 Descripción General

Sistema de gestión integral que permite:

- ✅ Crear, leer, actualizar y eliminar (CRUD) incidentes y solicitudes
- 🔐 Autenticación mediante JWT (JSON Web Tokens)
- 👥 Control de acceso basado en roles (RBAC): User, Agent, Admin
- 📊 Paginación y filtrado de tickets
- 💬 Sistema de comentarios por ticket
- 📈 Cobertura de pruebas con JaCoCo
- 🐳 Containerización con Docker Compose

### Stack Tecnológico

**Backend:**
- Java 21
- Spring Boot 3.4.2
- Spring Security
- PostgreSQL 16
- JPA/Hibernate
- Flyway (migraciones)

**Frontend:**
- Angular 18
- TypeScript
- RxJS
- Bootstrap 5

**DevOps:**
- Docker
- Docker Compose
- Maven

## 📦 Requisitos Previos

### ✅ Requisito Único: Docker Desktop

**NO necesitas instalar Java, Node.js, PostgreSQL, ni ningún otro software adicional.**

Solo necesitas:

1. **Docker Desktop** (versión 4.0 o superior)
   - [Descargar para Windows](https://www.docker.com/products/docker-desktop)
   - Incluye automáticamente:
     - Docker Engine
     - Docker Compose
     - Todos los servicios necesarios (Java, Node.js, PostgreSQL)

### Verificar Instalación

```powershell
docker --version
docker-compose --version
```

Ambos comandos deben devolver números de versión.

### ¿Por Qué Solo Docker Desktop?

Todo está containerizado:
- ✅ **Backend (Java 21):** Dentro del contenedor
- ✅ **Frontend (Angular):** Dentro del contenedor
- ✅ **Base de Datos (PostgreSQL):** Dentro del contenedor
- ✅ **Maven & npm:** Dentro de los contenedores

**Simplemente ejecuta los contenedores y listo.**

## 🚀 Instalación y Levantamiento

### ⚡ Opción 1: Script Automatizado (Recomendado - 2 Clics)

**Pasos:**
1. Abre PowerShell
2. Navega a la carpeta del proyecto:
   ```powershell
   cd C:\Users\walte\OneDrive\Documentos\PROYECTO_ALTAMIRA\incidentes_y_solicitudes
   ```
3. Ejecuta el script:
   ```powershell
   .\LEVANTAR_Y_PROBAR.bat
   ```

**El script automáticamente:**
- ✅ Verifica Docker Desktop está instalado
- ✅ Detiene servicios previos
- ✅ Levanta PostgreSQL (5432)
- ✅ Levanta Backend (8081)
- ✅ Levanta Frontend (4200)
- ✅ Espera inicialización completa
- ✅ Abre navegador automáticamente
- ✅ Muestra mensajes de progreso

**Tiempo estimado:** 2-3 minutos en primera ejecución

### Opción 2: Comandos Manuales Rápidos

```powershell
# 1. Navegar al directorio del proyecto
cd C:\Users\walte\OneDrive\Documentos\PROYECTO_ALTAMIRA\incidentes_y_solicitudes

# 2. Levantar TODO con un comando
docker-compose up -d

# 3. Esperar 45-60 segundos para inicialización

# 4. Verificar que todo esté UP
docker-compose ps

# 5. Ver si hay errores (opcional)
docker-compose logs -f backend

# 6. Abrir navegador
start http://localhost:4200
```

### ✅ Verificación Rápida

Una vez ejecutado, verifica que todo funciona:

```powershell
# Todos los contenedores deben estar "Up"
docker-compose ps

# Resultado esperado:
# NAME                  STATUS
# incidentes-frontend   Up ... 0.0.0.0:4200->80/tcp
# incidentes-backend    Up ... 0.0.0.0:8081->8081/tcp
# incidentes-postgres   Up ... 0.0.0.0:5432->5432/tcp
```

## 🧪 Pruebas

El proyecto incluye pruebas unitarias e integración con cobertura de código mediante JaCoCo.

### Ejecutar Todas las Pruebas (Recomendado)

```powershell
.\run-tests.ps1
```

Este script:
1. ✅ Ejecuta todas las pruebas unitarias e integración
2. ✅ Genera reporte JaCoCo automáticamente
3. ✅ Abre el reporte en tu navegador
4. ✅ Muestra resumen de cobertura en consola

**Tiempo estimado:** 2-3 minutos

### Ejecutar Pruebas sin Abrir Reporte

```powershell
.\run-tests-simple.ps1
```

### Pruebas Manuales (No recomendado)

Si prefieres controlar todo manualmente:

```powershell
# Limpiar y ejecutar pruebas
.\mvnw.cmd clean test

# Generar reporte JaCoCo
.\mvnw.cmd jacoco:report

# Abrir reporte
start target\site\jacoco\index.html
```

### Ver Resultados

Después de ejecutar `.\run-tests.ps1`:

1. **Reporte JaCoCo:** Se abre automáticamente en navegador
2. **Cobertura esperada:**
   - Controladores: 90%+
   - Servicios: 85%+
   - Repositorios: 80%+
   - **Total: 75%+**

3. **Archivo de log:**
   - `target/test-output.log`

### Troubleshooting de Pruebas

Si las pruebas fallan:

```powershell
# 1. Limpiar caché de Maven
.\mvnw.cmd clean

# 2. Ejecutar nuevamente
.\run-tests.ps1

# 3. Si persiste, ver logs detallados
Get-Content target\test-output.log | Select-Object -Last 50
```

## 🌐 Acceso a la Aplicación

### 🎯 Paso 1: Esperar a que TODO esté listo

Después de ejecutar `docker-compose up -d`, espera **45-60 segundos** para que:
- ✅ PostgreSQL esté escuchando en puerto 5432
- ✅ Backend termine de compilar y migrar la BD (Java tarda)
- ✅ Frontend termine de compilar Angular

### 🎯 Paso 2: Acceder a la Aplicación

**Frontend (La Aplicación Web):**
- **URL:** http://localhost:4200
- **Puedes:** 
  - Navegar, crear tickets, agregar comentarios
  - Todo lo que necesitas está aquí
- **Puerto:** 4200 (Nginx + Angular)

**Backend (API REST - Solo si necesitas probar API):**
- **URL Base:** http://localhost:8081
- **Health Check:** http://localhost:8081/actuator/health
- **Swagger:** http://localhost:8081/swagger-ui.html

**Base de Datos (Solo si necesitas admin):**
- **Host:** localhost
- **Puerto:** 5432
- **Usuario:** postgres
- **Contraseña:** postgres
- **Cliente:** DBeaver o pgAdmin (instalar en tu máquina si quieres)

### ⏱️ Esperando Inicialización

Si ves **502 Bad Gateway** en el frontend:
1. El backend aún está compilando (normal)
2. Espera 30 segundos más
3. Recarga la página (F5)
4. Si persiste, ejecuta: `docker-compose logs -f backend`

## 🔐 Credenciales por Defecto

```
Usuario:     admin
Contraseña:  admin
Rol:         ADMIN
```

**Importante:** Las credenciales se crean automáticamente en la primera ejecución mediante la migración Flyway `V3__create_usuarios_y_roles.sql`.

### Cambiar Contraseña

Si necesitas cambiar la contraseña del usuario admin:

1. Conéctate a la BD con DBeaver
2. Ejecuta el script SQL en `src/main/resources/db/migration/V3__create_usuarios_y_roles.sql`
3. Recompila el hash BCrypt si deseas una contraseña diferente

## 📁 Estructura del Proyecto

```
incidentes_y_solicitudes/
├── src/
│   ├── main/
│   │   ├── java/com/altamira/incidentes_y_solicitudes/
│   │   │   ├── api/              # Controladores REST
│   │   │   ├── config/           # Configuración (Security, JWT, etc.)
│   │   │   ├── persistence/      # Entidades, repositorios, mappers
│   │   │   ├── service/          # Lógica de negocio
│   │   │   └── IncidentesYSolicitudesApplication.java
│   │   └── resources/
│   │       ├── application.yaml  # Config producción
│   │       ├── application-local.yaml
│   │       ├── application-test.yaml
│   │       └── db/migration/     # Scripts Flyway
│   └── test/
│       ├── java/com/altamira/incidentes_y_solicitudes/
│       │   ├── controller/       # Tests de controladores
│       │   ├── service/          # Tests de servicios
│       │   ├── persistence/      # Tests de persistencia
│       │   └── integration/      # Tests de integración
│       └── resources/
│           └── application-test.yaml
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/       # Componentes Angular
│   │   │   ├── guards/           # Guards de autenticación
│   │   │   ├── interceptors/     # Interceptors HTTP/JWT
│   │   │   ├── services/         # Servicios Angular
│   │   │   └── models/           # Modelos TypeScript
│   │   └── styles.css
│   ├── angular.json
│   ├── package.json
│   └── Dockerfile
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── LEVANTAR_Y_PROBAR.bat
├── run-tests.ps1
├── run-tests-simple.ps1
└── README.md
```

## 🔍 Características Principales

### Autenticación y Autorización

- **JWT:** Tokens con expiración configurable
- **RBAC:** Tres roles predefinidos
  - `ROLE_USER`: Usuario básico
  - `ROLE_AGENT`: Agente de soporte
  - `ROLE_ADMIN`: Administrador del sistema

### Gestión de Incidentes

- Crear nuevos incidentes/solicitudes
- Filtrar por aplicación, estado y prioridad
- Paginación configurable (10, 20, 50 registros)
- Ordenamiento por fecha, prioridad, estado
- Edición en línea de campos
- Eliminación lógica de registros

### Comentarios

- Agregar comentarios a cada ticket
- Registro de auditoría (quién, cuándo)
- Historial completo de comentarios

### Catálogos

- Estados: ABIERTO, EN PROGRESO, CERRADO, RECHAZADO
- Prioridades: BAJA, MEDIA, ALTA, CRÍTICA
- Tipos: INCIDENTE, SOLICITUD, CAMBIO
- Aplicaciones: Configurable

## 🛠️ Comandos Útiles

```powershell
# Ver estado de contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres

# Reiniciar servicios
docker-compose restart

# Detener servicios
docker-compose stop

# Levantar servicios detenidos
docker-compose start

# Eliminar contenedores y redes (No elimina volúmenes)
docker-compose down

# Eliminar todo incluido volúmenes
docker-compose down -v

# Limpiar cache de Docker
docker system prune -a

# Compilar solo (sin tests)
.\mvnw.cmd clean compile

# Construir JAR
.\mvnw.cmd clean package -DskipTests

# Ejecutar backend localmente (sin Docker)
.\mvnw.cmd clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

## 🐛 Solución de Problemas

### El backend no levanta

**Síntoma:** Error de conexión a PostgreSQL

**Solución:**
```powershell
# Verificar que PostgreSQL esté corriendo
docker-compose logs postgres

# Reiniciar solo PostgreSQL
docker-compose restart postgres

# Esperar 10 segundos y reiniciar backend
docker-compose restart backend
```

### El frontend muestra 502 Bad Gateway

**Síntoma:** Errores de conexión desde Angular a la API

**Solución:**
1. Verificar que el backend esté completamente levantado:
   ```powershell
   docker-compose logs -f backend
   ```
2. Aguardar a que aparezca "Started IncidentesYSolicitudesApplication"
3. Recargar el navegador (F5)

### Las pruebas fallan

**Síntoma:** Errores en run-tests.ps1

**Solución:**
```powershell
# Limpiar caché de Maven
.\mvnw.cmd clean

# Ejecutar pruebas con output detallado
.\mvnw.cmd test -X

# Ver logs del error específico
Get-Content target\test-output.log
```

### JaCoCo no genera reporte

**Síntoma:** Carpeta `target/site/jacoco/` no existe

**Solución:**
```powershell
# Asegurar que JaCoCo esté configurado
.\mvnw.cmd clean test jacoco:report

# Si sigue sin funcionar, verificar pom.xml tiene jacoco-maven-plugin
```

### Puerto 4200 ya está en uso

**Síntoma:** Error "bind: address already in use"

**Solución:**
```powershell
# Encontrar qué proceso usa el puerto
netstat -ano | findstr ":4200"

# Detener el contenedor anterior
docker-compose down

# Esperar 10 segundos y levantar nuevamente
docker-compose up -d
```

## 📊 Estructura de Tests

```
test/
├── java/com/altamira/incidentes_y_solicitudes/
│   ├── IncidentesYSolicitudesApplicationTests.java    (Context loading)
│   ├── IncidentesYSolicitudesIntegrationTest.java     (Integration tests)
│   ├── config/
│   │   └── SecurityConfigTest.java
│   ├── controller/
│   │   ├── TicketControllerWebMvcTest.java
│   │   ├── ComentarioControllerWebMvcTest.java
│   │   ├── EstadoControllerWebMvcTest.java
│   │   └── PrioridadControllerWebMvcTest.java
│   ├── persistence/
│   │   ├── mapper/MapperTest.java
│   │   └── spec/TicketSpecificationsTest.java
│   └── service/
│       ├── TicketServiceTest.java
│       ├── ComentarioServiceTest.java
│       ├── EstadoServiceTest.java
│       └── PrioridadServiceTest.java
└── resources/
    └── application-test.yaml
```

**Total de Tests:** 111+
**Cobertura esperada:** 75%+

## 🔄 Flujo de Desarrollo

1. **Editar código**
   - Cambios en `src/main/java` se recargan automáticamente (DevTools)
   - Cambios en `frontend/src` se recompilan automáticamente

2. **Ejecutar pruebas**
   ```powershell
   .\run-tests.ps1
   ```

3. **Ver cobertura**
   - Abre `target/site/jacoco/index.html` en el navegador

4. **Validar en Docker**
   ```powershell
   docker-compose down
   docker-compose up -d
   ```

## 📝 Notas Importantes

- **Primera ejecución:** Puede tardar 2-3 minutos (compilación de Angular)
- **Base de datos:** Se crea automáticamente con Flyway
- **Usuarios:** Se crean automáticamente (admin/admin)
- **Contraseñas:** Hasheadas con BCrypt
- **JWT:** Válido por 24 horas (configurable en `application.yaml`)
- **CORS:** Habilitado solo para localhost:4200

## 🚀 Próximos Pasos

1. Abre http://localhost:4200 en tu navegador
2. Inicia sesión con `admin` / `admin`
3. Crea tu primer ticket
4. Ejecuta pruebas con `.\run-tests.ps1`
5. Consulta la cobertura de código

## 📞 Soporte

Para reportar problemas:
1. Revisa la sección [Solución de Problemas](#solución-de-problemas)
2. Ejecuta `docker-compose logs` para ver los errores
3. Verifica que todos los puertos (4200, 8081, 5432) estén disponibles

## 📄 Licencia

Este proyecto es para uso educativo y de desarrollo interno.

---

**Última actualización:** Febrero 2026
**Estado:** ✅ Producción

