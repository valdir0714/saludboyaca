# ════════════════════════════════════════════════════════
# ETAPA 1: COMPILACIÓN — Maven 3.8 + JDK 17
# ════════════════════════════════════════════════════════
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app

# Copiar pom.xml primero para aprovechar caché de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Compilar el proyecto
COPY src ./src
RUN mvn clean package -DskipTests

# ════════════════════════════════════════════════════════
# ETAPA 2: EJECUCIÓN — Tomcat 10 + JDK 17
# ════════════════════════════════════════════════════════
FROM tomcat:10.1-jdk17-openjdk

# Limpiar apps de ejemplo de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar WAR compilado como ROOT (ruta: http://host:8080/)
COPY --from=build /app/target/saludboyaca.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

# Variables de entorno — sobreescribir en docker run o en el hosting
ENV DB_URL="jdbc:mysql://HOST:3306/saludboyaca?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8"
ENV DB_USER="root"
ENV DB_PASS=""
ENV EMAIL_FROM="cristiannalvarez95@gmail.com"
ENV EMAIL_PASS="edpl qtng sltq vnth"

CMD ["catalina.sh", "run"]
