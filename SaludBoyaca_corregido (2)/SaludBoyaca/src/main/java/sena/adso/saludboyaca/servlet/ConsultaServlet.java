package sena.adso.saludboyaca.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sena.adso.saludboyaca.dao.CitaDAO;
import sena.adso.saludboyaca.dto.Cita;
import sena.adso.saludboyaca.util.CaptchaGenerator;
import sena.adso.saludboyaca.util.PDFGenerator;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(name = "ConsultaServlet", urlPatterns = {"/consulta-cita"})
public class ConsultaServlet extends HttpServlet {

    /** GET → muestra formulario con CAPTCHA nuevo */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String captchaTexto = CaptchaGenerator.generarTexto();
        request.getSession(true).setAttribute("captchaConsulta", captchaTexto);
        request.getRequestDispatcher("/WEB-INF/views/consulta_cita.jsp").forward(request, response);
    }

    /** POST → valida CAPTCHA, busca citas, o genera PDF */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        // Acción PDF solicitada desde la lista de resultados
        if ("pdf".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Cita cita = new CitaDAO().buscarPorId(id);
            if (cita != null) {
                HttpSession sess = request.getSession(false);
                String lang = sess != null ? (String) sess.getAttribute("lang") : "es";
                if (lang == null) lang = "es";
                PDFGenerator.generarComprobante(response, cita, lang);
            } else {
                response.sendRedirect(request.getContextPath() + "/consulta-cita");
            }
            return;
        }

        // Búsqueda normal con CAPTCHA
        HttpSession session = request.getSession(true);
        String lang = (String) session.getAttribute("lang");
        if (lang == null) lang = "es";
        ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));

        String captchaIngresado  = request.getParameter("captcha");
        String captchaGuardado   = (String) session.getAttribute("captchaConsulta");
        String documento         = request.getParameter("documento");

        // Regenerar CAPTCHA para el siguiente intento
        String nuevoCaptcha = CaptchaGenerator.generarTexto();
        session.setAttribute("captchaConsulta", nuevoCaptcha);

        if (!CaptchaGenerator.validar(captchaIngresado, captchaGuardado)) {
            request.setAttribute("error", rb.getString("consulta.captcha.error"));
            request.setAttribute("documento", documento);
            request.getRequestDispatcher("/WEB-INF/views/consulta_cita.jsp").forward(request, response);
            return;
        }

        // CAPTCHA válido → buscar citas
        request.setAttribute("documento", documento);
        List<Cita> citas = new CitaDAO().listarPorPacienteDoc(documento);
        if (citas.isEmpty()) {
            request.setAttribute("noEncontrado", rb.getString("consulta.no.encontrado"));
        } else {
            request.setAttribute("citas", citas);
        }
        request.getRequestDispatcher("/WEB-INF/views/consulta_cita.jsp").forward(request, response);
    }
}
