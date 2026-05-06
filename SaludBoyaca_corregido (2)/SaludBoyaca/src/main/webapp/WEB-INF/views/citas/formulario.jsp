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
    
    <div class="container py-4" style="max-width:760px;">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 style="color:var(--color-titulo);">
                <i class="fas fa-calendar-plus me-2"></i>
                <c:choose>
                    <c:when test="${empty cita}"><fmt:message key="cita.nueva"/></c:when>
                    <c:otherwise><fmt:message key="cita.titulo"/> #${cita.id}</c:otherwise>
                </c:choose>
            </h2>
            <a href="${pageContext.request.contextPath}/citas" class="btn btn-outline-secondary btn-sm">
                <i class="fas fa-arrow-left me-1"></i><fmt:message key="paciente.cancelar"/>
            </a>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <!-- Asegúrate de que tu CitaServlet maneje la acción 'guardar' o use el ID para distinguir -->
                <form action="${pageContext.request.contextPath}/citas" method="post">
                    <input type="hidden" name="id" value="${cita.id}">
                    
                    <div class="row g-3">
                        <!-- Paciente -->
                        <div class="col-md-6">
                            <label class="form-label"><fmt:message key="cita.paciente"/> *</label>
                            <select name="idPaciente" class="form-select" required>
                                <option value="" disabled selected><fmt:message key="cita.seleccione.paciente"/></option>
                                <c:forEach var="p" items="${pacientes}">
                                    <option value="${p.id}" ${cita.idPaciente == p.id ? 'selected' : ''}>
                                        ${p.nombres} ${p.apellidos} (${p.documento})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Especialidad -->
                        <div class="col-md-6">
                            <label class="form-label"><fmt:message key="cita.especialidad"/> *</label>
                            <select name="idEspecialidad" class="form-select" required> 
                                <!-- Nota: Para cargar médicos dinámicamente, usualmente se usa AJAX o recarga. 
                                     Aquí simplificado para estructura visual. Si usas JS, quita el onchange -->
                                <option value="" disabled selected><fmt:message key="cita.seleccione.especialidad"/></option>
                                <c:forEach var="e" items="${especialidades}">
                                    <option value="${e.id}" ${cita.idEspecialidad == e.id ? 'selected' : ''}>
                                        ${e.nombre}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Médico -->
                        <div class="col-md-6">
                            <label class="form-label"><fmt:message key="cita.medico"/> *</label>
                            <select name="idMedico" class="form-select" required>
                                <option value="" disabled selected><fmt:message key="cita.seleccione.medico"/></option>
                                <c:forEach var="m" items="${medicos}">
                                    <option value="${m.id}" ${cita.idMedico == m.id ? 'selected' : ''}>
                                        Dr(a). ${m.nombres} ${m.apellidos}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Fecha -->
                        <div class="col-md-6">
                            <label class="form-label"><fmt:message key="cita.fecha"/> *</label>
                            <input type="date" name="fechaCita" class="form-control" required 
                                   value="${cita.fechaCita}">
                        </div>

                        <!-- Hora -->
                        <div class="col-md-6">
                            <label class="form-label"><fmt:message key="cita.hora"/> *</label>
                            <select name="horaCita" class="form-select" required>
                                <option value="" disabled selected><fmt:message key="cita.seleccione.hora"/></option>
                                <!-- Idealmente esto se carga dinámicamente según médico y fecha -->
                                <option value="08:00:00" ${cita.horaCita == '08:00:00' ? 'selected' : ''}>08:00 AM</option>
                                <option value="09:00:00" ${cita.horaCita == '09:00:00' ? 'selected' : ''}>09:00 AM</option>
                                <option value="10:00:00" ${cita.horaCita == '10:00:00' ? 'selected' : ''}>10:00 AM</option>
                                <option value="11:00:00" ${cita.horaCita == '11:00:00' ? 'selected' : ''}>11:00 AM</option>
                                <option value="14:00:00" ${cita.horaCita == '14:00:00' ? 'selected' : ''}>02:00 PM</option>
                                <option value="15:00:00" ${cita.horaCita == '15:00:00' ? 'selected' : ''}>03:00 PM</option>
                            </select>
                        </div>

                        <!-- Motivo -->
                        <div class="col-12">
                            <label class="form-label"><fmt:message key="cita.motivo"/></label>
                            <textarea name="motivo" class="form-control" rows="3" placeholder="Describa brevemente el motivo de la consulta">${cita.motivo}</textarea>
                        </div>
                    </div>

                    <div class="d-flex gap-2 mt-4">
                        <button type="submit" class="btn btn-saludboyaca">
                            <i class="fas fa-save me-1"></i><fmt:message key="paciente.guardar"/>
                        </button>
                        <a href="${pageContext.request.contextPath}/citas" class="btn btn-outline-secondary">
                            <i class="fas fa-times me-1"></i><fmt:message key="paciente.cancelar"/>
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
    if(btn) {
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