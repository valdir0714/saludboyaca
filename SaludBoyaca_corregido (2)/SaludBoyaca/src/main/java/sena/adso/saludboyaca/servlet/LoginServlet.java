package sena.adso.saludboyaca.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sena.adso.saludboyaca.dao.UsuarioDAO;
import sena.adso.saludboyaca.dto.Usuario;
import sena.adso.saludboyaca.util.OTPService;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null
                && session.getAttribute("usuario") != null
                && Boolean.TRUE.equals(session.getAttribute("otpVerificado"))) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.validarLogin(username, password);

        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "es";
        }
        ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));

        if (usuario != null) {
            String otp = OTPService.generarOTP();
            long timestamp = java.time.Instant.now().toEpochMilli();

            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("usuarioNombre", usuario.getNombreCompleto());
            session.setAttribute("usuarioRol", usuario.getRol());
            session.setAttribute("otpCodigo", otp);
            session.setAttribute("otpTimestamp", timestamp);
            session.setAttribute("otpEmail", usuario.getEmail());
            session.setAttribute("otpVerificado", false);

            try {
                String asunto = rb.getString("otp.email.asunto");
                String cuerpo = MessageFormat.format(rb.getString("otp.email.cuerpo"), otp);
                OTPService.enviarOTP(usuario.getEmail(), otp, asunto, cuerpo);
                response.sendRedirect(request.getContextPath() + "/otp");

            } catch (Exception ex) {

                // ── Limpiar sesión ──────────────────────────────────────────
                session.removeAttribute("usuario");
                session.removeAttribute("usuarioId");
                session.removeAttribute("usuarioNombre");
                session.removeAttribute("usuarioRol");
                session.removeAttribute("otpCodigo");
                session.removeAttribute("otpTimestamp");
                session.removeAttribute("otpEmail");
                session.removeAttribute("otpVerificado");

                // ── Construir mensaje de error con causa real ───────────────
                String causaRaiz = ex.getMessage();
                // Si la excepción tiene causa anidada (ej: AuthenticationFailedException)
                if (ex.getCause() != null && ex.getCause().getMessage() != null) {
                    causaRaiz = ex.getCause().getMessage();
                }
                // Si hay doble anidamiento
                if (ex.getCause() != null && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getMessage() != null) {
                    causaRaiz = ex.getCause().getCause().getMessage();
                }

                String errorMsg = "Error al enviar OTP: " + causaRaiz
                        + " | Remitente: " + System.getenv("EMAIL_FROM")
                        + " | Pass configurada: " + (System.getenv("EMAIL_PASS") != null ? "SÍ" : "NO (usando fallback)");

                System.err.println("[LoginServlet] " + errorMsg);
                request.setAttribute("error", errorMsg);
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }

        } else {
            request.setAttribute("error", rb.getString("login.error.credenciales"));
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
