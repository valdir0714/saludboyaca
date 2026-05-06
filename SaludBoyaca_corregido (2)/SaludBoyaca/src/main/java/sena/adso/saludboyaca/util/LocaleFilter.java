package sena.adso.saludboyaca.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(urlPatterns = {"/*"})
public class LocaleFilter implements Filter {

    private static final List<String> IDIOMAS_VALIDOS = Arrays.asList("es", "en", "it");
    private static final String IDIOMA_POR_DEFECTO = "es";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(true);

        String langParam = req.getParameter("lang");
        if (langParam != null && IDIOMAS_VALIDOS.contains(langParam)) {
            session.setAttribute("lang", langParam);
        }

        if (session.getAttribute("lang") == null) {
            session.setAttribute("lang", IDIOMA_POR_DEFECTO);
        }

        chain.doFilter(request, response);
    }
}
