<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
        <title><fmt:message key="paciente.titulo"/> — SaludBoyacá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body style="background:var(--color-fondo);">
        <%@ include file="../templates/header.jsp" %>
        <div class="container-fluid py-4 px-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2 style="color:var(--color-titulo);"><i class="fas fa-users me-2"></i><fmt:message key="paciente.titulo"/></h2>
                    <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
                    <a href="${pageContext.request.contextPath}/pacientes?accion=nuevo" class="btn btn-saludboyaca">
                        <i class="fas fa-plus me-1"></i><fmt:message key="paciente.nuevo"/>
                    </a>
                </c:if>
            </div>
            <c:if test="${not empty param.msg}">
                <div class="alert alert-${param.msg == 'error' ? 'danger' : 'success'} alert-dismissible fade show py-2">
                    <fmt:message key="paciente.${param.msg}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <div class="card shadow-sm border-0">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead style="background:var(--color-primario);color:white;">
                                <tr>
                                    <th>#</th>
                                    <th><fmt:message key="paciente.nombres"/></th>
                                    <th><fmt:message key="paciente.apellidos"/></th>
                                    <th><fmt:message key="paciente.documento"/></th>
                                    <th><fmt:message key="paciente.eps"/></th>
                                    <th><fmt:message key="paciente.telefono"/></th>
                                    <th class="text-center"><fmt:message key="cita.acciones"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="p" items="${pacientes}">
                                    <tr>
                                        <td>${p.id}</td>
                                        <td>${p.nombres}</td>
                                        <td>${p.apellidos}</td>
                                        <td>${p.documento}</td>
                                        <td>${p.eps}</td>
                                        <td>${p.telefono}</td>
                                        <td class="text-center">
                                            <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
                                                <a href="${pageContext.request.contextPath}/pacientes?accion=editar&id=${p.id}"
                                                   class="btn btn-sm btn-saludboyaca me-1">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/pacientes?accion=eliminar&id=${p.id}"
                                                   class="btn btn-sm btn-danger"
                                                   onclick="return confirm('<fmt:message key='paciente.confirmar'/>')">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty pacientes}">
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