# Servicio Escolar

API REST desarrollada con **Spring Boot** para gestionar estudiantes, solicitudes de documentos escolares y cobros relacionados con los trámites.

## Tecnologías utilizadas

- **Java 21**
- **Spring Boot 4.1.1**
- **Spring Data JPA** para la persistencia de datos.
- **MySQL** como base de datos.
- **Maven** para la gestión de dependencias y compilación.

## Funcionalidades

- Registro y consulta de estudiantes.
- Creación y consulta de solicitudes de documentos escolares.
- Generación de PDF y envío simulado de claves para los trámites.
- Cierre de solicitudes mediante una clave de validación.
- Consulta y simulación de pagos asociados a las solicitudes.

## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- Java JDK 21.
- MySQL Server.
- Git (opcional, para clonar el repositorio).

## Instalación y ejecución local

### 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
cd servicioescolar
```

### 2. Configurar la base de datos

Crea una base de datos en MySQL con el siguiente comando:

```sql
CREATE DATABASE servicioescolar;
```

Configura las credenciales de conexión en el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/servicioescolar?serverTimezone=UTC
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

Sustituye `TU_USUARIO` y `TU_PASSWORD` por tus credenciales locales de MySQL.

Hibernate está configurado para actualizar el esquema de la base de datos automáticamente mediante `spring.jpa.hibernate.ddl-auto=update`.

### 3. Ejecutar la aplicación

Desde la carpeta raíz del proyecto, ejecuta:

**Windows:**

```bash
mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

También puedes ejecutar el proyecto desde tu IDE, iniciando la clase `ServicioescolarApplication.java`.

Una vez iniciada, la API estará disponible en:

```text
http://localhost:1501
```

## Endpoints principales

| Método HTTP | Endpoint | Descripción |
|---|---|---|
| POST | `/estudiantes` | Registrar un estudiante. |
| GET | `/estudiantes` | Listar estudiantes. |
| GET | `/estudiantes/{id}` | Consultar un estudiante por ID. |
| POST | `/solicitudes?idEstudiante={id}&documento={documento}` | Crear una solicitud escolar. |
| GET | `/solicitudes/{id}` | Consultar una solicitud. |
| POST | `/solicitudes/{id}/enviar-clave` | Generar el PDF y simular el envío de una clave. |
| POST | `/solicitudes/{id}/cerrar?clave={clave}` | Cerrar una solicitud mediante su clave. |
| GET | `/cobros/solicitud/{idSolicitud}` | Consultar el cobro de una solicitud. |
| POST | `/cobros/solicitud/{idSolicitud}/pagar` | Simular el pago de una solicitud. |

Los endpoints pueden probarse con herramientas como Postman o Insomnia.

## Estructura del proyecto

```text
servicioescolar/
├── src/
│   └── main/
│       ├── java/com/api/servicioescolar/
│       │   ├── controllers/
│       │   ├── models/
│       │   ├── repositories/
│       │   ├── services/
│       │   └── ServicioescolarApplication.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

- **controllers:** exponen los endpoints de la API REST.
- **models:** definen las entidades del sistema.
- **repositories:** gestionan el acceso a la base de datos.
- **services:** contienen la lógica de negocio.
- **resources:** almacenan la configuración de la aplicación.

## Notas

- La aplicación utiliza el puerto `1501` por defecto.
- El servicio de pago es una simulación; no procesa transacciones bancarias reales.
- El envío de claves por correo está simulado mediante registros en el log de la aplicación.
- Se requiere una instancia de MySQL en ejecución para utilizar la aplicación.

## Autor

Proyecto desarrollado como parte de un sistema de gestión de servicios escolares.
