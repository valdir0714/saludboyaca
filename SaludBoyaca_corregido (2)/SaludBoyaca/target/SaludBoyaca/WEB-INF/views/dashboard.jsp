<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title><fmt:message key="nav.dashboard"/> — SaludBoyacá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body style="background:var(--color-fondo);">
        <%@ include file="templates/header.jsp" %>
        <div class="container-fluid py-4 px-4">
            <h2 class="mb-1" style="color:var(--color-titulo);">
                <fmt:message key="dashboard.bienvenida"><fmt:param value="${sessionScope.usuarioNombre}"/></fmt:message>
            </h2>
            <p class="text-muted mb-4"><fmt:message key="app.institucion"/></p>

        <!-- Tarjetas métricas -->
        <div class="row g-3 mb-4">
            <div class="col-6 col-md-3">
                <div class="card-stat border-start-azul">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-label"><fmt:message key="dashboard.citas.hoy"/></div>
                            <div class="stat-number">${citasHoy}</div>
                        </div>
                        <i class="fas fa-calendar-day fa-2x text-primary opacity-50"></i>
                    </div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="card-stat border-start-ambar">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-label"><fmt:message key="dashboard.citas.pendientes"/></div>
                            <div class="stat-number" style="color:#F39C12;">${citasPendientes}</div>
                        </div>
                        <i class="fas fa-clock fa-2x opacity-50" style="color:#F39C12;"></i>
                    </div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="card-stat border-start-verde">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-label"><fmt:message key="dashboard.citas.mes"/></div>
                            <div class="stat-number" style="color:var(--color-sena);">${citasMes}</div>
                        </div>
                        <i class="fas fa-calendar-alt fa-2x opacity-50" style="color:var(--color-sena);"></i>
                    </div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="card-stat border-start-celeste">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-label"><fmt:message key="dashboard.pacientes.total"/></div>
                            <div class="stat-number" style="color:var(--color-acento);">${totalPacientes}</div>
                        </div>
                        <i class="fas fa-users fa-2x opacity-50" style="color:var(--color-acento);"></i>
                    </div>
                </div>
            </div>
        </div>

        <!-- Accesos rápidos -->
        <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
            <div class="mb-4">
                <a href="${pageContext.request.contextPath}/citas?accion=nuevo" class="btn btn-saludboyaca me-2">
                    <i class="fas fa-plus me-1"></i><fmt:message key="cita.nueva"/>
                </a>
                <a href="${pageContext.request.contextPath}/pacientes?accion=nuevo" class="btn btn-outline-success">
                    <i class="fas fa-user-plus me-1"></i><fmt:message key="paciente.nuevo"/>
                </a>
            </div>
        </c:if>

        <!-- Próximas citas -->
        <div class="card shadow-sm border-0">
            <div class="card-header" style="background:var(--color-primario);color:white;">
                <i class="fas fa-list-ul me-2"></i><fmt:message key="dashboard.proximas"/>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                            <tr>
                                <th><fmt:message key="cita.hora"/></th>
                        <th><fmt:message key="cita.fecha"/></th>
                        <th><fmt:message key="cita.paciente"/></th>
                        <th><fmt:message key="cita.especialidad"/></th>
                        <th><fmt:message key="cita.estado"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="cita" items="${proximasCitas}">
                            <tr>
                                <td>${cita.horaCita}</td>
                                <td>${cita.fechaCita}</td>
                                <td>${cita.nombrePaciente}</td>
                                <td>${cita.nombreEspecialidad}</td>
                                <td><span class="badge-estado-${cita.estado.toLowerCase()}">${cita.estado}</span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty proximasCitas}">
                            <tr><td colspan="5" class="text-center text-muted py-3">—</td></tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <footer class="text-center py-2 small text-muted"><fmt:message key="app.footer"/></footer>
    <script>
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
  [...tooltipTriggerList].map(el => new bootstrap.Tooltip(el));
</script>
</body>
</html>