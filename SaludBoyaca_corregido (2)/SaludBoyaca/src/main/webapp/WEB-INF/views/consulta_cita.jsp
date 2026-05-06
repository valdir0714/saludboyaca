<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
        <title><fmt:message key="consulta.titulo"/> — SaludBoyacá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body style="background:var(--color-fondo);">
        <!-- Header simple -->
        <nav class="navbar navbar-saludboyaca">
            <div class="container-fluid">
                <span class="navbar-brand fw-bold"><i class="fas fa-heartbeat me-2"></i>SaludBoyacá</span>
                <div class="d-flex align-items-center gap-3">
                    <div class="lang-selector">
                        <a href="?lang=es" class="text-white ${sessionScope.lang=='es'?'fw-bold':'opacity-75'}">🇨🇴 ES</a>
                        <span class="text-white mx-1">|</span>
                        <a href="?lang=en" class="text-white ${sessionScope.lang=='en'?'fw-bold':'opacity-75'}">🇺🇸 EN</a>
                        <span class="text-white mx-1">|</span>
                        <a href="?lang=it" class="text-white ${sessionScope.lang=='it'?'fw-bold':'opacity-75'}">🇮🇹 IT</a>
                    </div>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-sm btn-outline-light">
                        <i class="fas fa-sign-in-alt me-1"></i>Login
                    </a>
                </div>
            </div>
        </nav>

        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-12 col-md-7 col-lg-6">
                    <div class="card shadow border-0">
                        <div class="card-header py-3" style="background:var(--color-primario);">
                            <h5 class="text-white mb-0 text-center">
                                <i class="fas fa-search me-2"></i><fmt:message key="consulta.titulo"/>
                            </h5>
                        </div>
                        <div class="card-body p-4">
                            <p class="text-muted"><fmt:message key="consulta.instruccion"/></p>
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger py-2">${error}</div>
                            </c:if>
                            <form action="${pageContext.request.contextPath}/consulta-cita" method="post">
                                <div class="mb-3">
                                    <label class="form-label fw-semibold"><fmt:message key="consulta.documento"/> *</label>
                                    <input type="text" name="documento" class="form-control" required
                                           value="${documento}" placeholder="Ej: 3901234567">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-semibold"><fmt:message key="consulta.captcha"/> *</label>
                                    <div class="d-flex align-items-center gap-2">
                                        <img src="${pageContext.request.contextPath}/captcha-img?t=${System.currentTimeMillis()}"
                                             alt="captcha" class="border rounded" style="height:50px;"
                                             id="captchaImg">
                                        <button type="button" class="btn btn-sm btn-outline-secondary"
                                                onclick="document.getElementById('captchaImg').src = '${pageContext.request.contextPath}/captcha-img?t=' + Date.now()">
                                            <i class="fas fa-sync-alt"></i>
                                        </button>
                                    </div>
                                    <input type="text" name="captcha" class="form-control mt-2"
                                           maxlength="5" required placeholder="Ingrese el código">
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-saludboyaca">
                                        <i class="fas fa-search me-2"></i><fmt:message key="consulta.buscar"/>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Resultados -->
                    <c:if test="${not empty citas or not empty noEncontrado}">
                        <div class="card shadow border-0 mt-4">
                            <div class="card-header" style="background:var(--color-sena);color:white;">
                                <i class="fas fa-list me-2"></i>Resultados
                            </div>
                            <div class="card-body p-0">
                                <c:if test="${not empty noEncontrado}">
                                    <p class="text-muted text-center py-3">${noEncontrado}</p>
                                </c:if>
                                <c:if test="${not empty citas}">
                                    <div class="table-responsive">
                                        <table class="table table-hover mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th><fmt:message key="cita.fecha"/></th>
                                            <th><fmt:message key="cita.hora"/></th>
                                            <th><fmt:message key="cita.medico"/></th>
                                            <th><fmt:message key="cita.especialidad"/></th>
                                            <th><fmt:message key="cita.estado"/></th>
                                            <th></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="c" items="${citas}">
                                                <tr>
                                                    <td>${c.fechaCita}</td>
                                                    <td>${c.horaCita.toString().substring(0,5)}</td>
                                                    <td>${c.nombreMedico}</td>
                                                    <td>${c.nombreEspecialidad}</td>
                                                    <td><span class="badge-estado-${c.estado.toLowerCase()}">${c.estado}</span></td>
                                                    <td>
                                                <c:if test="${c.estado == 'PROGRAMADA' or c.estado == 'CONFIRMADA'}">
                                                    <form action="${pageContext.request.contextPath}/consulta-cita" method="post" class="d-inline">
                                                        <input type="hidden" name="accion" value="pdf">
                                                        <input type="hidden" name="id" value="${c.id}">
                                                        <button type="submit" class="btn btn-sm btn-success">
                                                            <i class="fas fa-file-pdf"></i>
                                                        </button>
                                                    </form>
                                                </c:if>
                                                </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
        <footer class="text-center py-2 small text-muted"><fmt:message key="app.footer"/></footer>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
  [...tooltipTriggerList].map(el => new bootstrap.Tooltip(el));
</script>
    </body>
</html>