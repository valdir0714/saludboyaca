package sena.adso.saludboyaca.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sena.adso.saludboyaca.util.OTPService;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(name = "OTPServlet", urlPatterns = {"/otp"})
public class OTPServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("otpCodigo") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String email = (String) session.getAttribute("otpEmail");
        request.setAttribute("emailMasked", enmascararEmail(email));
        request.getRequestDispatcher("/WEB-INF/views/otp_verificacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String codigoIngresado = request.getParameter("otpCodigo");
        String codigoSesion = (String) session.getAttribute("otpCodigo");
        Long timestamp = (Long) session.getAttribute("otpTimestamp");

        String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "es";
        }
        ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));

        if (timestamp != null && OTPService.esValido(codigoIngresado, codigoSesion, timestamp)) {
            session.setAttribute("otpVerificado", true);
            session.removeAttribute("otpCodigo");
            session.removeAttribute("otpTimestamp");
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", rb.getString("otp.error"));
            String email = (String) session.getAttribute("otpEmail");
            request.setAttribute("emailMasked", enmascararEmail(email));
            request.getRequestDispatcher("/WEB-INF/views/otp_verificacion.jsp").forward(request, response);
        }
    }

    private String enmascararEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        String[] partes = email.split("@");
        String local = partes[0];
        String dominio = partes[1];
        if (local.length() <= 3) {
            return local + "***@" + dominio;
        }
        return local.substring(0, 3) + "***@" + dominio;
    }
}
