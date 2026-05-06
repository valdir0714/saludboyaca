package sena.adso.saludboyaca.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sena.adso.saludboyaca.dao.PacienteDAO;
import sena.adso.saludboyaca.dto.Paciente;

import java.io.IOException;
import java.sql.Date;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(name = "PacienteServlet", urlPatterns = {"/pacientes"})
public class PacienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        PacienteDAO dao = new PacienteDAO();

        switch (accion) {
            case "nuevo":
                request.getRequestDispatcher("/WEB-INF/views/pacientes/formulario.jsp").forward(request, response);
                break;
            case "editar":
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("paciente", dao.buscarPorId(id));
                request.getRequestDispatcher("/WEB-INF/views/pacientes/formulario.jsp").forward(request, response);
                break;
            case "eliminar":
                int idElim = Integer.parseInt(request.getParameter("id"));
                HttpSession sess = request.getSession(false);
                String rol = (String) sess.getAttribute("usuarioRol");
                if ("MEDICO".equals(rol) || "RECEPCIONISTA".equals(rol)) {
                    dao.eliminar(idElim);
                }
                response.sendRedirect(request.getContextPath() + "/pacientes?msg=eliminado");
                break;
            default:
                request.setAttribute("pacientes", dao.listarTodos());
                request.getRequestDispatcher("/WEB-INF/views/pacientes/lista.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession sess = request.getSession(false);
        String rol = (String) sess.getAttribute("usuarioRol");

        // Solo MEDICO y RECEPCIONISTA pueden guardar
        if ("ENFERMERO".equals(rol)) {
            response.sendRedirect(request.getContextPath() + "/pacientes");
            return;
        }

        String lang = (String) sess.getAttribute("lang");
        if (lang == null) {
            lang = "es";
        }
        ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));

        PacienteDAO dao = new PacienteDAO();
        String idParam = request.getParameter("id");

        Paciente p = new Paciente();
        if (idParam != null && !idParam.isEmpty()) {
            p.setId(Integer.parseInt(idParam));
        }
        p.setNombres(request.getParameter("nombres"));
        p.setApellidos(request.getParameter("apellidos"));
        p.setTipoDocumento(request.getParameter("tipoDocumento"));
        p.setDocumento(request.getParameter("documento"));
        p.setFechaNacimiento(Date.valueOf(request.getParameter("fechaNacimiento")));
        p.setGenero(request.getParameter("genero"));
        p.setTelefono(request.getParameter("telefono"));
        p.setEmail(request.getParameter("email"));
        p.setEps(request.getParameter("eps"));
        p.setVeredaBarrio(request.getParameter("veredaBarrio"));

        boolean ok;
        if (p.getId() == 0) {
            ok = dao.insertar(p);
        } else {
            ok = dao.actualizar(p);
        }
        response.sendRedirect(request.getContextPath() + "/pacientes?msg=" + (ok ? "guardado" : "error"));
    }
}
