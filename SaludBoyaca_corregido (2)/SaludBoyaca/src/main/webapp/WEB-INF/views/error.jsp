<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Error — SaludBoyacá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body style="background:var(--color-fondo);">
        <div class="container min-vh-100 d-flex align-items-center justify-content-center">
            <div class="text-center">
                <i class="fas fa-exclamation-triangle fa-4x mb-3" style="color:var(--color-primario);"></i>
                <h2 style="color:var(--color-titulo);"><fmt:message key="error.servidor"/></h2>
                <p class="text-muted">${pageContext.errorData.statusCode}</p>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-saludboyaca">
                    <i class="fas fa-home me-1"></i>Volver al inicio
                </a>
            </div>
        </div>
                    <script>
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
  [...tooltipTriggerList].map(el => new bootstrap.Tooltip(el));
</script>
    </body>
</html>