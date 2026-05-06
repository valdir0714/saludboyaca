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
        <div class="container py-4" style="max-width:760px;">
            <h2 class="mb-4" style="color:var(--color-titulo);">
                <i class="fas fa-user${empty paciente ? '-plus' : '-edit'} me-2"></i>
                <c:choose>
                    <c:when test="${empty paciente}"><fmt:message key="paciente.nuevo"/></c:when>
                    <c:otherwise><fmt:message key="paciente.editar"/></c:otherwise>
                </c:choose>
            </h2>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <form action="${pageContext.request.contextPath}/pacientes" method="post">
                        <input type="hidden" name="id" value="${paciente.id}">

                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label"><fmt:message key="paciente.nombres"/> *</label>
                                <input type="text" name="nombres" class="form-control" required
                                       value="${paciente.nombres}" placeholder="Nombres completos">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label"><fmt:message key="paciente.apellidos"/> *</label>
                                <input type="text" name="apellidos" class="form-control" required
                                       value="${paciente.apellidos}" placeholder="Apellidos completos">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label"><fmt:message key="paciente.tipo.documento"/> *</label>
                                <select name="tipoDocumento" class="form-select" required>
                                    <option value="CC"  ${paciente.tipoDocumento == 'CC'  ? 'selected' : ''}>CC — Cédula</option>
                                    <option value="TI"  ${paciente.tipoDocumento == 'TI'  ? 'selected' : ''}>TI — Tarjeta de identidad</option>
                                    <option value="RC"  ${paciente.tipoDocumento == 'RC'  ? 'selected' : ''}>RC — Registro civil</option>
                                    <option value="CE"  ${paciente.tipoDocumento == 'CE'  ? 'selected' : ''}>CE — Cédula extranjería</option>
                                    <option value="PP"  ${paciente.tipoDocumento == 'PP'  ? 'selected' : ''}>PP — Pasaporte</option>
                                </select>
                            </div>
                            <div class="col-md-8">
                                <label class="form-label"><fmt:message key="paciente.documento"/> *</label>
                                <input type="text" name="documento" class="form-control" required
                                       value="${paciente.documento}" placeholder="Número de documento">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label"><fmt:message key="paciente.nacimiento"/> *</label>
                                <input type="date" name="fechaNacimiento" class="form-control" required
                                       value="${paciente.fechaNacimiento}">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label"><fmt:message key="paciente.genero"/> *</label>
                                <select name="genero" class="form-select" required>
                                    <option value="M"    ${paciente.genero == 'M'    ? 'selected' : ''}><fmt:message key="paciente.genero.m"/></option>
                                    <option value="F"    ${paciente.genero == 'F'    ? 'selected' : ''}><fmt:message key="paciente.genero.f"/></option>
                                    <option value="OTRO" ${paciente.genero == 'OTRO' ? 'selected' : ''}><fmt:message key="paciente.genero.otro"/></option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label"><fmt:message key="paciente.eps"/> *</label>
                                <input type="text" name="eps" class="form-control" required
                                       value="${paciente.eps}" placeholder="EPS o aseguradora">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label"><fmt:message key="paciente.telefono"/></label>
                                <input type="tel" name="telefono" class="form-control"
                                       value="${paciente.telefono}" placeholder="3XX XXX XXXX">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label"><fmt:message key="paciente.email"/></label>
                                <input type="email" name="email" class="form-control"
                                       value="${paciente.email}" placeholder="correo@ejemplo.com">
                            </div>
                            <div class="col-12">
                                <label class="form-label"><fmt:message key="paciente.vereda"/></label>
                                <input type="text" name="veredaBarrio" class="form-control"
                                       value="${paciente.veredaBarrio}" placeholder="Vereda o barrio">
                            </div>
                        </div>

                        <div class="d-flex gap-2 mt-4">
                            <button type="submit" class="btn btn-saludboyaca">
                                <i class="fas fa-save me-1"></i><fmt:message key="paciente.guardar"/>
                            </button>
                            <a href="${pageContext.request.contextPath}/pacientes" class="btn btn-outline-secondary">
                                <i class="fas fa-arrow-left me-1"></i><fmt:message key="paciente.cancelar"/>
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.querySelectorAll('form').forEach(f => {
                f.addEventListener('submit', () => {
                    const btn = f.querySelector('button[type="submit"]');
                    if (btn) {
                        btn.classList.add('btn-loading');
                        btn.innerHTML = btn.innerHTML.replace(/<i[^>]*><\/i>/, ''); // Oculta ícono si existe
                    }
                });
            });
        </script>
        <script>
            const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
            [...tooltipTriggerList].map(el => new bootstrap.Tooltip(el));
        </script>
    </body>
</html>
