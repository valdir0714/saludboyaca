package sena.adso.saludboyaca.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sena.adso.saludboyaca.util.CaptchaGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@WebServlet(name = "CaptchaServlet", urlPatterns = {"/captcha-img"})
public class CaptchaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        // Si no hay captcha en sesión, generar uno nuevo
        String texto = (String) session.getAttribute("captchaConsulta");
        if (texto == null) {
            texto = CaptchaGenerator.generarTexto();
            session.setAttribute("captchaConsulta", texto);
        }
        BufferedImage img = CaptchaGenerator.generarImagen(texto);
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        ImageIO.write(img, "PNG", response.getOutputStream());
    }
}
