# Propuesta: Cómo Escalar el Sistema y Mejorar la Observabilidad

## 1. Ideas para que el Sistema Crezca sin Problemas

### Cache con Redis

Ahora mismo cada vez que alguien consulta los estados o prioridades, le pegamos a la base de datos. Esto funciona bien con pocos usuarios, pero cuando tengamos muchos simultáneos va a ser un cuello de botella.

La idea es poner Redis en el medio como una capa de caché rápida. Si los datos están ahí, los devolvemos de inmediato. Si no están, vamos a Postgres, traemos los datos y los guardamos en Redis para la próxima vez.

**Qué cachear:**
- Los catálogos (estados, prioridades) por 1 hora - estos casi nunca cambian
- El listado de tickets por usuario por 5 minutos - se actualiza más seguido  
- Limpiar el caché cuando creamos o actualizamos tickets

Con esto podríamos reducir entre 60% y 70% las consultas a la base de datos, lo que se nota mucho cuando hay tráfico.

### Colas Asíncronas (RabbitMQ)

Si el día de mañana queremos mandar notificaciones por email cuando se crea un ticket, no deberíamos hacer que el usuario espere a que se envíe el correo. Lo mejor es guardar el ticket rápido y luego procesar las notificaciones en segundo plano.

**Cómo funciona:**
1. Usuario crea un ticket → Guardamos en BD (50ms)
2. Publicamos un evento en una cola de mensajes
3. Un worker separado lee la cola y envía emails/notificaciones
4. Si falla, reintenta automáticamente hasta 3 veces

La respuesta al usuario pasa de 500ms a 50ms, y si hay un problema con el servicio de email no afecta la creación de tickets.

### Particionamiento por Aplicación

Cuando tengamos millones de tickets en la tabla, las consultas se van a poner lentas. PostgreSQL tiene una funcionalidad que nos permite dividir la tabla en particiones más pequeñas.

**La idea es particionar por aplicación:**
```sql
CREATE TABLE tickets (...) PARTITION BY LIST (aplicacion);
CREATE TABLE tickets_app1 PARTITION OF tickets FOR VALUES IN ('APP1');
CREATE TABLE tickets_app2 PARTITION OF tickets FOR VALUES IN ('APP2');
```

Cuando alguien busca tickets de APP1, Postgres solo mira esa partición pequeña en lugar de escanear toda la tabla. Los índices también son más chicos y rápidos.

Con +1 millón de tickets, las consultas pueden ser hasta 5 veces más rápidas.

### Evitar Tickets Duplicados (Idempotencia)

Si un usuario hace doble clic en "Crear Ticket", se crean 2 tickets idénticos. Para evitar esto podemos usar una técnica llamada "idempotency key".

**Cómo funciona:**
- El frontend genera un ID único (UUID) por cada intento de creación
- Lo manda en un header: `Idempotency-Key: abc-123-def-456`
- El backend verifica si ya procesó ese ID antes
- Si ya lo procesó, devuelve el ticket que creó la primera vez
- Si no, crea el ticket nuevo y guarda el ID por 24 horas

Así el usuario puede hacer clic 10 veces y solo se crea 1 ticket.

---

## 2. Saber Qué Está Pasando en Producción (Observabilidad)

### Métricas que Necesitamos Ver

Lo más importante es saber cuándo algo anda mal ANTES de que los usuarios se quejen. Para eso necesitamos contar cosas:

**Errores por endpoint:**
- Cuántos errores 4xx (problemas del cliente) tiene cada endpoint
- Cuántos errores 5xx (problemas nuestros) tiene cada endpoint
- Si POST /comentarios tiene 50 errores 5xx en una hora, hay que revisar

**Performance:**
- Cuánto tarda cada endpoint en responder
- Si el 95% de las peticiones a GET /tickets tarda más de 2 segundos, tenemos un problema
- Cuántas peticiones por segundo está recibiendo cada endpoint

**Métricas de negocio:**
- Cuántos tickets se crean por día
- Cuántos tickets están ABIERTOS vs CERRADOS
- Cuántos tickets CRÍTICOS hay sin asignar
- Tiempo promedio de resolución

### Cómo Implementarlo

Spring Boot ya trae Micrometer integrado, solo necesitamos activarlo y conectarlo a Prometheus. Prometheus guarda las métricas y Grafana las muestra en dashboards bonitos.

Agregamos esto al código para contar errores:

```java
@RestControllerAdvice
public class MetricsExceptionHandler {
    // Contador de errores 4xx
    private final Counter http4xxCounter;
    // Contador de errores 5xx
    private final Counter http5xxCounter;
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleErrors(Exception ex) {
        if (es4xx) {
            http4xxCounter.increment(); // +1 al contador
        } else if (es5xx) {
            http5xxCounter.increment(); // +1 al contador
        }
        // devolver el error normal
    }
}
```

### Las Alertas que Necesitamos

No sirve de nada tener métricas si no nos avisan cuando algo anda mal. Acá van las alertas que deberíamos configurar:

**1. Muchos Errores 5xx (Problema Grave)**

Si más del 1% de las peticiones están fallando con error 500, algo está roto.

Qué hacer:
- Revisar los logs del backend: `docker compose logs backend -f`
- Verificar que PostgreSQL esté respondiendo
- Si es por mucha carga, escalar (agregar más réplicas)
- Si persiste, avisar al equipo de desarrollo

**2. Respuestas Muy Lentas**

Si el 95% de las peticiones tarda más de 2 segundos, el sistema se siente lento.

Qué hacer:
- Revisar si hay queries lentas en PostgreSQL
- Verificar que Redis esté funcionando
- Revisar los logs de la aplicación
- Considerar escalar horizontalmente (más servidores)

**3. Base de Datos Llegando al Límite**

Si PostgreSQL está usando más del 85% de su capacidad, pronto vamos a tener problemas.

Qué hacer:
- Revisar logs de Flyway (puede haber migraciones trabadas)
- Archivar tickets viejos (más de 1 año)
- Hacer mantenimiento (REINDEX, VACUUM)
- Considerar aumentar el almacenamiento

**4. Muchos Tokens JWT Inválidos**

Si de repente hay más de 10 validaciones JWT fallidas por minuto, puede haber un problema en la autenticación.

Qué hacer:
- Verificar la configuración JWT en application.yaml
- Revisar si cambió la clave secreta por error
- Validar que el reloj del servidor esté sincronizado (NTP)
- Avisar a los usuarios si es necesario que vuelvan a loguearse

### Qué Herramientas Usar

Para tener visibilidad completa del sistema, necesitamos 3 cosas:

**Prometheus:** Recopila las métricas cada 15 segundos
- Cuántas peticiones hay
- Cuánto tardan
- Cuántas fallan

**Grafana:** Muestra todo en dashboards visuales
- Gráficas de tráfico en tiempo real
- Tablas con errores
- Alarmas cuando algo anda mal

**Loki:** Para los logs de la aplicación
- Ver todos los errores que pasaron
- Buscar por usuario, ticket, etc
- Correlacionar errores con métricas

**Cómo agregarlo al proyecto:**

Solo necesitamos agregar estos servicios al docker-compose.yml:

```yaml
prometheus:
  image: prom/prometheus:latest
  volumes:
    - ./prometheus.yml:/etc/prometheus/prometheus.yml
  ports:
    - "9090:9090"

grafana:
  image: grafana/grafana:latest
  environment:
    - GF_SECURITY_ADMIN_PASSWORD=admin
  ports:
    - "3000:3000"
```

Y listo, con `docker compose up -d` ya tenemos todo el stack de observabilidad.

---

## 3. Plan de Implementación (por Fases)

No hay que hacer todo de una vez. Lo mejor es ir paso a paso:

**Fase 1 (1-2 semanas): Observabilidad Básica**
- Integrar Prometheus y Grafana
- Dashboard básico con métricas de errores
- Configurar alertas críticas (5xx, latencia)

Ya con esto podemos ver qué está pasando en el sistema en tiempo real.

**Fase 2 (2-3 semanas): Cache**
- Instalar Redis
- Cachear catálogos (estados, prioridades)
- Cachear listado de tickets

Esto va a hacer que el sistema se sienta mucho más rápido para los usuarios.

**Fase 3 (3-4 semanas): Colas Asíncronas**
- Instalar RabbitMQ
- Migrar las notificaciones a colas
- Worker separado para enviar emails

Las respuestas van a ser instantáneas.

**Fase 4 (4-6 semanas): Particionamiento**
- Analizar los datos actuales
- Crear particiones en PostgreSQL
- Migrar datos existentes

Solo vale la pena si ya tenemos +500k tickets.

**Fase 5 (En paralelo): Idempotencia**
- Agregar header Idempotency-Key
- Validación en backend
- Actualizar frontend

Se puede hacer en cualquier momento, no depende de las otras fases.

---

## 4. Resultados Esperados

Si implementamos todo esto, deberíamos ver estos cambios:

| Lo que medimos | Ahora | Meta después |
|----------------|-------|--------------|
| Tiempo de respuesta (95% de requests) | 800ms | menos de 200ms |
| Errores 5xx | 0.5% | menos de 0.1% |
| Requests por segundo | 50 | más de 500 |
| Disponibilidad | 99.0% | 99.9% |
| Tickets duplicados | 2% | 0% |

---

## Conclusión

El sistema actual funciona bien, pero cuando crezca el tráfico vamos a necesitar estas mejoras. Lo bueno es que podemos implementarlas de a poco, empezando por las más importantes (observabilidad y cache).

Con todo esto implementado, el sistema podría escalar de 100 usuarios concurrentes a más de 1,000 sin problemas, y vamos a saber exactamente qué está pasando en todo momento.




