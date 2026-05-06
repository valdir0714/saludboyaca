<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>
<nav class="navbar navbar-expand-lg navbar-saludboyaca">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/dashboard">
            <i class="fas fa-heartbeat me-2"></i><fmt:message key="app.nombre"/>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navMain">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navMain">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/dashboard">
                        <i class="fas fa-tachometer-alt me-1"></i><fmt:message key="nav.dashboard"/>
                    </a>
                </li>
                <c:if test="${sessionScope.usuarioRol == 'MEDICO' or sessionScope.usuarioRol == 'RECEPCIONISTA' or sessionScope.usuarioRol == 'ENFERMERO'}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/pacientes">
                            <i class="fas fa-users me-1"></i><fmt:message key="nav.pacientes"/>
                        </a>
                    </li>
                </c:if>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/citas">
                        <i class="fas fa-calendar-check me-1"></i><fmt:message key="nav.citas"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/horarios">
                        <i class="fas fa-clock me-1"></i><fmt:message key="nav.horarios"/>
                    </a>
                </li>
            </ul>
            <div class="d-flex align-items-center gap-3">
                <%-- Selector de idioma --%>
                <div class="lang-selector">
                    <a href="?lang=es" class="${sessionScope.lang == 'es' ? 'active' : ''}">🇨🇴 ES</a>
                    <span class="sep">|</span>
                    <a href="?lang=en" class="${sessionScope.lang == 'en' ? 'active' : ''}">🇺🇸 EN</a>
                    <span class="sep">|</span>
                    <a href="?lang=it" class="${sessionScope.lang == 'it' ? 'active' : ''}">🇮🇹 IT</a>
                </div>
                <%-- Usuario --%>
                <span class="navbar-text" style="font-size:.85rem; opacity:.9;">
                    <i class="fas fa-user-md me-1"></i>${sessionScope.usuarioNombre}
                    <span class="badge bg-light text-dark ms-1" style="font-size:.7rem;">
                        ${sessionScope.usuarioRol}
                    </span>
                </span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-light">
                    <i class="fas fa-sign-out-alt me-1"></i><fmt:message key="nav.salir"/>
                </a>
            </div>
        </div>
    </div>
</nav>
