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
        <title><fmt:message key="otp.titulo"/> — SaludBoyacá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body class="login-wrapper">
        <div class="container min-vh-100 d-flex align-items-center justify-content-center">
            <div class="col-12 col-sm-9 col-md-6 col-lg-4">
                <div class="text-center mb-3">
                    <a href="?lang=es" class="text-white text-decoration-none me-2 ${sessionScope.lang=='es'?'fw-bold':'opacity-75'}">🇨🇴 ES</a>
                    <span class="text-white opacity-50">|</span>
                    <a href="?lang=en" class="text-white text-decoration-none mx-2 ${sessionScope.lang=='en'?'fw-bold':'opacity-75'}">🇺🇸 EN</a>
                    <span class="text-white opacity-50">|</span>
                    <a href="?lang=it" class="text-white text-decoration-none ms-2 ${sessionScope.lang=='it'?'fw-bold':'opacity-75'}">🇮🇹 IT</a>
                </div>
                <div class="card shadow-lg border-0 otp-card">
                    <div class="card-header text-center py-4 otp-card-header">
                        <i class="fas fa-shield-alt fa-3x mb-2"></i>
                        <h4 class="mb-0"><fmt:message key="otp.titulo"/></h4>
                    </div>
                    <div class="card-body p-4">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger py-2"><i class="fas fa-exclamation-circle me-2"></i>${error}</div>
                        </c:if>
                        <p class="text-center text-muted mb-4">
                            <fmt:message key="otp.instruccion"><fmt:param value="${emailMasked}"/></fmt:message>
                        </p>
                        <div class="text-center mb-3">
                            <i class="fas fa-clock me-1 text-muted"></i>
                            <span id="otpTimer" class="text-muted">05:00</span>
                        </div>
                        <form action="${pageContext.request.contextPath}/otp" method="post">
                            <div class="mb-4">
                                <label class="form-label fw-bold text-center d-block"><fmt:message key="otp.campo"/></label>
                                <input type="text" name="otpCodigo" class="form-control otp-input"
                                       maxlength="6" pattern="[0-9]{6}" placeholder="000000"
                                       required autofocus autocomplete="one-time-code" inputmode="numeric">
                            </div>
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-otp btn-lg">
                                    <i class="fas fa-check-circle me-2"></i><fmt:message key="otp.verificar"/>
                                </button>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-secondary">
                                    <i class="fas fa-redo me-1"></i><fmt:message key="otp.reenviar"/>
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
                <p class="text-center text-white opacity-75 mt-3 small"><fmt:message key="app.footer"/></p>
            </div>
        </div>
        <script>
            (function(){var s=300,el=document.getElementById('otpTimer'),iv=setInterval(function(){s--;if(s<=0){clearInterval(iv);el.textContent='00:00';el.classList.add('danger');return;}var m=Math.floor(s/60),sc=s%60;el.textContent=(m<10?'0':'')+m+':'+(sc<10?'0':'')+sc;if(s<60)el.classList.add('danger');},1000);})();
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
  [...tooltipTriggerList].map(el => new bootstrap.Tooltip(el));
</script>
    </body>
</html>
