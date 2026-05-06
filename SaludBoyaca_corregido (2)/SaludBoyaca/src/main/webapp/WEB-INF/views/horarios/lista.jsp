<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
        <title><fmt:message key="horario.titulo"/> — SaludBoyacá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body style="background:var(--color-fondo);">
        <%@ include file="../templates/header.jsp" %>
        <div class="container-fluid py-4 px-4">
            <h2 class="mb-4" style="color:var(--color-titulo);"><i class="fas fa-clock me-2"></i><fmt:message key="horario.titulo"/></h2>
            <div class="card shadow-sm border-0">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead style="background:var(--color-primario);color:white;">
                                <tr>
                                    <th><fmt:message key="horario.medico"/></th>
                                    <th><fmt:message key="horario.dia"/></th>
                                    <th><fmt:message key="horario.inicio"/></th>
                                    <th><fmt:message key="horario.fin"/></th>
                                    <th><fmt:message key="horario.max.citas"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="h" items="${horarios}">
                                    <tr>
                                        <td>${h.nombreMedico}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${h.diaSemana==1}"><fmt:message key="horario.lunes"/></c:when>
                                                <c:when test="${h.diaSemana==2}"><fmt:message key="horario.martes"/></c:when>
                                                <c:when test="${h.diaSemana==3}"><fmt:message key="horario.miercoles"/></c:when>
                                                <c:when test="${h.diaSemana==4}"><fmt:message key="horario.jueves"/></c:when>
                                                <c:otherwise><fmt:message key="horario.viernes"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${h.horaInicio.toString().substring(0,5)}</td>
                                        <td>${h.horaFin.toString().substring(0,5)}</td>
                                        <td>${h.maxCitas}</td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty horarios}">
                                    <tr><td colspan="5" class="text-center text-muted py-3">—</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <script>
            const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
            [...tooltipTriggerList].map(el => new bootstrap.Tooltip(el));
        </script>
    </body>
</html>