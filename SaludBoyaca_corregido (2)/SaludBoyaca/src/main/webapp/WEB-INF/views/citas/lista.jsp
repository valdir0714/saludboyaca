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
        <div class="container-fluid py-4 px-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2 style="color:var(--color-titulo);"><i class="fas fa-calendar-check me-2"></i><fmt:message key="cita.titulo"/></h2>
                <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
                    <a href="${pageContext.request.contextPath}/citas?accion=nuevo" class="btn btn-saludboyaca">
                        <i class="fas fa-plus me-1"></i><fmt:message key="cita.nueva"/>
                    </a>
                </c:if>
            </div>
            <c:if test="${not empty param.msg}">
                <div class="alert alert-${param.msg == 'error' ? 'danger' : 'success'} alert-dismissible fade show py-2">
                    <fmt:message key="cita.${param.msg == 'guardado' ? 'guardada' : (param.msg == 'cancelado' ? 'cancelada' : 'actualizada')}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <div class="card shadow-sm border-0">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead style="background:var(--color-primario);color:white;">
                                <tr>
                                    <th><fmt:message key="cita.fecha"/></th>
                            <th><fmt:message key="cita.hora"/></th>
                            <th><fmt:message key="cita.paciente"/></th>
                            <th><fmt:message key="cita.medico"/></th>
                            <th><fmt:message key="cita.especialidad"/></th>
                            <th><fmt:message key="cita.estado"/></th>
                            <th class="text-center"><fmt:message key="cita.acciones"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="c" items="${citas}">
                                <tr>
                                    <td>${c.fechaCita}</td>
                                    <td>${c.horaCita.toString().substring(0,5)}</td>
                                    <td>${c.nombrePaciente}</td>
                                    <td>${c.nombreMedico}</td>
                                    <td>${c.nombreEspecialidad}</td>
                                    <td><span class="badge-estado-${c.estado.toLowerCase()} rounded-pill">${c.estado}</span></td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/citas?accion=detalle&id=${c.id}"
                                           class="btn btn-sm btn-info text-white me-1" title="<fmt:message key='cita.ver.detalle'/>">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
                                    <a href="${pageContext.request.contextPath}/citas?accion=editar&id=${c.id}"
                                       class="btn btn-sm btn-saludboyaca me-1"><i class="fas fa-edit"></i></a>
                                    <c:if test="${c.estado != 'CANCELADA' and c.estado != 'ATENDIDA'}">
                                        <a href="${pageContext.request.contextPath}/citas?accion=cancelar&id=${c.id}"
                                           class="btn btn-sm btn-danger me-1"
                                           onclick="return confirm('¿Cancelar esta cita?')">
                                            <i class="fas fa-times"></i>
                                        </a>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/citas?accion=pdf&id=${c.id}"
                                       class="btn btn-sm btn-success" title="<fmt:message key='cita.descargar'/>">
                                        <i class="fas fa-file-pdf"></i>
                                    </a>
                                </c:if>
                                </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty citas}">
                                <tr><td colspan="7" class="text-center text-muted py-3">—</td></tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
  [...tooltipTriggerList].map(el => new bootstrap.Tooltip(el));
</script>
    </body>
</html>