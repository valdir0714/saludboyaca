# SaludBoyacá — Gestión de Citas Médicas

**Centro de Salud Municipal de Paipa, Boyacá**  
SENA · CIMM · Regional Boyacá · ADSO 2026

---

## Descripción

Sistema web Java EE para gestión de citas médicas con autenticación OTP por correo, roles diferenciados (MÉDICO, RECEPCIONISTA, ENFERMERO), módulo público con CAPTCHA, internacionalización en 3 idiomas (ES/EN/IT) y despliegue en Docker.

---

## Tecnologías

- **Backend:** Java 17, Jakarta EE 10, Servlets, JSP/JSTL 3.0
- **Base de datos:** MySQL 8
- **Autenticación:** OTP por correo (Jakarta Mail / Angus Mail)
- **PDF:** OpenPDF
- **Contenedor:** Tomcat 10.1
- **Empaquetado:** Maven 3.8, WAR
- **Docker:** Multi-Stage Build

---

## Credenciales de prueba

| Usuario    | Contraseña  | Rol            |
|------------|-------------|----------------|
| cpedraza   | admin123    | MÉDICO         |
| msuarez    | enfermero1  | ENFERMERO      |
| jbaez      | recep123    | RECEPCIONISTA  |
| lospina    | medico456   | MÉDICO         |
| dmoreno    | medico789   | MÉDICO         |

> **Nota:** El OTP se envía al correo registrado del usuario. Para pruebas locales, configura `EMAIL_FROM` y `EMAIL_PASS` con una cuenta Gmail + App Password.

---

## Configuración local

### Requisitos
- JDK 17+
- Maven 3.8+
- MySQL 8 corriendo en localhost
- Tomcat 10.1 (o usar Docker)

### 1. Base de datos
```sql
CREATE DATABASE saludboyaca CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE saludboyaca;
-- Ejecutar el script:
SOURCE src/main/resources/saludboyaca.sql;
```

### 2. Variables de entorno (opcional)
```bash
export DB_URL="jdbc:mysql://localhost:3306/saludboyaca?useSSL=false&serverTimezone=UTC"
export DB_USER="root"
export DB_PASS=""
export EMAIL_FROM="tu-correo@gmail.com"
export EMAIL_PASS="xxxx xxxx xxxx xxxx"
```

Si no se establecen, usa los valores por defecto de `Conexion.java` (localhost, root, sin contraseña).

### 3. Compilar y desplegar
```bash
mvn clean package -DskipTests
# Copiar target/saludboyaca.war a $TOMCAT_HOME/webapps/ROOT.war
```

---

## Docker

### Build local
```bash
docker build -t tuusuario/saludboyaca:v1 .
```

### Ejecutar con MySQL Railway
```bash
docker run -d -p 8080:8080 \
  -e DB_URL="jdbc:mysql://HOST_RAILWAY:PORT/railway?useSSL=false&serverTimezone=UTC" \
  -e DB_USER="TU_USUARIO" \
  -e DB_PASS="TU_CONTRASEÑA" \
  -e EMAIL_FROM="saludboyaca.sena@gmail.com" \
  -e EMAIL_PASS="xxxx xxxx xxxx xxxx" \
  --name saludboyaca \
  tuusuario/saludboyaca:v1
```

### Ejecutar con MySQL local (Docker Desktop)
```bash
docker run -d -p 8080:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/saludboyaca?useSSL=false&serverTimezone=UTC" \
  -e DB_USER="root" -e DB_PASS="" \
  -e EMAIL_FROM="..." -e EMAIL_PASS="..." \
  --name saludboyaca tuusuario/saludboyaca:v1
```

Acceder en: **http://localhost:8080/**

---

## Despliegue en hosting

### Render (render.com)
1. New → Web Service → "Deploy an existing image from a registry"
2. Image URL: `tuusuario/saludboyaca:v1`
3. Instance Type: Free · Environment: agregar las 4 variables de entorno
4. URL resultado: `https://saludboyaca.onrender.com`

### Koyeb (koyeb.com)
1. Create Service → Docker · Image: `tuusuario/saludboyaca:v1`
2. Port: 8080 · Instance: nano · Variables de entorno
3. URL resultado: `https://saludboyaca-xxxx.koyeb.app`

---

## Estructura del proyecto

```
src/main/java/sena/adso/saludboyaca/
├── model/        Conexion.java (lee vars de entorno)
├── dto/          Usuario, Paciente, Especialidad, Horario, Cita
├── dao/          UsuarioDAO, PacienteDAO, CitaDAO, HorarioDAO, OTPTokenDAO, EspecialidadDAO
├── servlet/      Login, OTP, Dashboard, Paciente, Cita, Horario, Consulta, Logout, Captcha
└── util/         LocaleFilter, AuthFilter, OTPService, CaptchaGenerator, PDFGenerator

src/main/resources/
├── messages.properties        (español)
├── messages_en.properties     (inglés)
├── messages_it.properties     (italiano)
└── saludboyaca.sql

src/main/webapp/
├── resources/css/saludboyaca.css
└── WEB-INF/views/
    ├── login.jsp, otp_verificacion.jsp, dashboard.jsp, error.jsp
    ├── pacientes/ (lista.jsp, formulario.jsp)
    ├── citas/     (lista.jsp, formulario.jsp, detalle.jsp)
    ├── horarios/  (lista.jsp)
    ├── consulta_cita.jsp
    └── templates/ (header.jsp)
```

---

## Flujo de autenticación

```
/login (GET) → login.jsp (sin CAPTCHA)
    ↓ POST usuario+contraseña
LoginServlet → valida BD → genera OTP → envía por correo → session otpVerificado=false
    ↓ redirect
/otp (GET) → otp_verificacion.jsp (código 6 dígitos, contador 5 min)
    ↓ POST código
OTPServlet → valida código y timestamp → session otpVerificado=true
    ↓ redirect
/dashboard → AuthFilter exige usuario + otpVerificado=true
```

---

## Módulos y permisos

| Módulo          | MÉDICO | RECEPCIONISTA | ENFERMERO |
|-----------------|--------|---------------|-----------|
| Dashboard       | ✅     | ✅            | ✅        |
| Pacientes CRUD  | ✅     | ✅            | Solo lectura |
| Citas CRUD      | ✅ (propias) | ✅      | Solo lectura |
| Horarios        | Lectura | Lectura      | —         |
| Exportar PDF    | ✅     | ✅            | ❌        |
| Consulta pública| Sin sesión (CAPTCHA) | — | — |

---

*SENA · Centro Industrial de Mantenimiento y Manufactura – CIMM · Regional Boyacá · 2026*
