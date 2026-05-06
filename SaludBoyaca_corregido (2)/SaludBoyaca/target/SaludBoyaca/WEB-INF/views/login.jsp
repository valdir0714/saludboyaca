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
        <title><fmt:message key="login.titulo"/> — SaludBoyacá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body class="login-wrapper">
        <div class="container min-vh-100 d-flex align-items-center justify-content-center">
            <div class="col-12 col-sm-9 col-md-6 col-lg-4">

                <!-- Selector de idioma -->
                <div class="text-center mb-3">
                    <a href="?lang=es" class="text-white text-decoration-none me-2 ${sessionScope.lang=='es'?'fw-bold':'opacity-75'}">🇨🇴 ES</a>
                    <span class="text-white opacity-50">|</span>
                    <a href="?lang=en" class="text-white text-decoration-none mx-2 ${sessionScope.lang=='en'?'fw-bold':'opacity-75'}">🇺🇸 EN</a>
                    <span class="text-white opacity-50">|</span>
                    <a href="?lang=it" class="text-white text-decoration-none ms-2 ${sessionScope.lang=='it'?'fw-bold':'opacity-75'}">🇮🇹 IT</a>
                </div>

                <div class="card shadow-lg border-0 login-card">
                    <div class="card-header text-center py-4 login-card-header">
                        <i class="fas fa-heartbeat fa-3x mb-2"></i>
                        <h4 class="mb-0">SaludBoyacá</h4>
                        <small class="opacity-75"><fmt:message key="app.institucion"/></small>
                    </div>
                    <div class="card-body p-4">
                        <h5 class="text-center mb-4" style="color:var(--color-primario);">
                            <fmt:message key="login.titulo"/>
                        </h5>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger py-2"><i class="fas fa-exclamation-circle me-2"></i>${error}</div>
                        </c:if>
                        <form action="${pageContext.request.contextPath}/login" method="post">
                            <div class="mb-3">
                                <label class="form-label fw-semibold"><fmt:message key="login.usuario"/></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <input type="text" name="username" class="form-control" required autofocus
                                           placeholder="<fmt:message key='login.usuario'/>">
                                </div>
                            </div>
                            <div class="mb-4">
                                <label class="form-label fw-semibold"><fmt:message key="login.contrasena"/></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                    <input type="password" name="password" class="form-control" required
                                           placeholder="••••••••">
                                </div>
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-saludboyaca btn-lg">
                                    <i class="fas fa-sign-in-alt me-2"></i><fmt:message key="login.ingresar"/>
                                </button>
                            </div>
                        </form>
                    </div>
                    <div class="card-footer text-center py-3 bg-transparent">
                        <a href="${pageContext.request.contextPath}/consulta-cita" class="text-muted text-decoration-none small">
                            <i class="fas fa-search me-1"></i><fmt:message key="login.consulta"/>
                        </a>
                    </div>
                </div>

                <p class="text-center text-white opacity-75 mt-3 small"><fmt:message key="app.footer"/></p>
            </div>
        </div>
            <script>
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
  [...tooltipTriggerList].map(el => new bootstrap.Tooltip(el));
</script>
    </body>
</html>