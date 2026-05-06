<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
        <title><fmt:message key="cita.titulo"/> — SaludBoyacá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body style="background:var(--color-fondo);">
        <%@ include file="../templates/header.jsp" %>
        <div class="container py-4" style="max-width:680px;">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 style="color:var(--color-titulo);">
                    <i class="fas fa-calendar-check me-2"></i>
                    <fmt:message key="cita.titulo"/> #${cita.id}
                </h2>
                <a href="${pageContext.request.contextPath}/citas" class="btn btn-outline-secondary btn-sm">
                    <i class="fas fa-arrow-left me-1"></i><fmt:message key="paciente.cancelar"/>
                </a>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-header" style="background:var(--color-primario);color:white;">
                    <i class="fas fa-info-circle me-2"></i>Detalle de la cita
                    <span class="float-end">
                        <span class="badge-estado-${cita.estado.toLowerCase()}">${cita.estado}</span>
                    </span>
                </div>
                <div class="card-body">
                    <table class="table table-borderless mb-0">
                        <tbody>
                            <tr>
                                <th style="color:var(--color-primario);width:40%;"><fmt:message key="cita.paciente"/></th>
                                <td>${cita.nombrePaciente}</td>
                            </tr>
                            <tr>
                                <th style="color:var(--color-primario);"><fmt:message key="paciente.documento"/></th>
                                <td>${cita.documentoPaciente}</td>
                            </tr>
                            <tr>
                                <th style="color:var(--color-primario);"><fmt:message key="cita.medico"/></th>
                                <td>Dr(a). ${cita.nombreMedico}</td>
                            </tr>
                            <tr>
                                <th style="color:var(--color-primario);"><fmt:message key="cita.especialidad"/></th>
                                <td>${cita.nombreEspecialidad}</td>
                            </tr>
                            <tr>
                                <th style="color:var(--color-primario);"><fmt:message key="cita.fecha"/></th>
                                <td>${cita.fechaCita}</td>
                            </tr>
                            <tr>
                                <th style="color:var(--color-primario);"><fmt:message key="cita.hora"/></th>
                                <td>${cita.horaCita.toString().substring(0,5)}</td>
                            </tr>
                            <c:if test="${not empty cita.motivo}">
                            <tr>
                                <th style="color:var(--color-primario);"><fmt:message key="cita.motivo"/></th>
                                <td>${cita.motivo}</td>
                            </tr>
                            </c:if>
                            <c:if test="${not empty cita.observaciones}">
                            <tr>
                                <th style="color:var(--color-primario);"><fmt:message key="cita.observaciones"/></th>
                                <td>${cita.observaciones}</td>
                            </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Acciones -->
            <div class="d-flex gap-2 mt-3 flex-wrap">
                <!-- PDF disponible para médico y recepcionista -->
                <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
                    <a href="${pageContext.request.contextPath}/citas?accion=pdf&id=${cita.id}"
                       class="btn btn-success">
                        <i class="fas fa-file-pdf me-1"></i><fmt:message key="cita.descargar"/>
                    </a>
                </c:if>

                <!-- Cambios de estado para médico y recepcionista -->
                <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
                    <c:if test="${cita.estado == 'PROGRAMADA'}">
                        <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=CONFIRMADA"
                           class="btn btn-outline-success">
                            <i class="fas fa-check me-1"></i><fmt:message key="cita.estado.confirmada"/>
                        </a>
                    </c:if>
                    <c:if test="${cita.estado == 'CONFIRMADA'}">
                        <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=ATENDIDA"
                           class="btn btn-outline-info">
                            <i class="fas fa-user-check me-1"></i><fmt:message key="cita.estado.atendida"/>
                        </a>
                    </c:if>
                    <c:if test="${cita.estado != 'CANCELADA' and cita.estado != 'ATENDIDA'}">
                        <a href="${pageContext.request.contextPath}/citas?accion=cancelar&id=${cita.id}"
                           class="btn btn-outline-danger"
                           onclick="return confirm('¿Cancelar esta cita?')">
                            <i class="fas fa-times me-1"></i><fmt:message key="cita.estado.cancelada"/>
                        </a>
                    </c:if>
                </c:if>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
  [...tooltipTriggerList].map(el => new bootstrap.Tooltip(el));
</script>
    </body>
</html>
