package sena.adso.saludboyaca.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sena.adso.saludboyaca.dao.*;
import sena.adso.saludboyaca.dto.Cita;
import sena.adso.saludboyaca.util.PDFGenerator;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(name = "CitaServlet", urlPatterns = {"/citas"})
public class CitaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        HttpSession sess = request.getSession(false);
        String rol = (String) sess.getAttribute("usuarioRol");
        int idUsuario = (int) sess.getAttribute("usuarioId");

        CitaDAO citaDAO = new CitaDAO();
        EspecialidadDAO espDAO = new EspecialidadDAO();
        PacienteDAO pacDAO = new PacienteDAO();
        UsuarioDAO usuDAO = new UsuarioDAO();

        switch (accion) {
            case "nuevo":
                request.setAttribute("especialidades", espDAO.listarTodas());
                request.setAttribute("pacientes", pacDAO.listarTodos());
                request.setAttribute("medicos", usuDAO.listarMedicos());
                request.getRequestDispatcher("/WEB-INF/views/citas/formulario.jsp").forward(request, response);
                break;

            case "editar":
                if ("ENFERMERO".equals(rol)) {
                    response.sendRedirect(request.getContextPath() + "/citas");
                    return;
                }
                int idEdit = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("cita", citaDAO.buscarPorId(idEdit));
                request.setAttribute("especialidades", espDAO.listarTodas());
                request.setAttribute("pacientes", pacDAO.listarTodos());
                request.setAttribute("medicos", usuDAO.listarMedicos());
                request.getRequestDispatcher("/WEB-INF/views/citas/formulario.jsp").forward(request, response);
                break;

            case "detalle":
                int idDet = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("cita", citaDAO.buscarPorId(idDet));
                request.getRequestDispatcher("/WEB-INF/views/citas/detalle.jsp").forward(request, response);
                break;

            case "cambiarEstado":
                if ("ENFERMERO".equals(rol)) {
                    response.sendRedirect(request.getContextPath() + "/citas");
                    return;
                }
                citaDAO.cambiarEstado(Integer.parseInt(request.getParameter("id")), request.getParameter("estado"));
                response.sendRedirect(request.getContextPath() + "/citas?msg=actualizado");
                break;

            case "cancelar":
                if ("ENFERMERO".equals(rol)) {
                    response.sendRedirect(request.getContextPath() + "/citas");
                    return;
                }
                citaDAO.cambiarEstado(Integer.parseInt(request.getParameter("id")), "CANCELADA");
                response.sendRedirect(request.getContextPath() + "/citas?msg=cancelado");
                break;

            case "pdf":
                int idPdf = Integer.parseInt(request.getParameter("id"));
                Cita citaPdf = citaDAO.buscarPorId(idPdf);
                if (citaPdf != null) {
                    String lang = (String) sess.getAttribute("lang");
                    if (lang == null) {
                        lang = "es";
                    }
                    PDFGenerator.generarComprobante(response, citaPdf, lang);
                } else {
                    response.sendRedirect(request.getContextPath() + "/citas");
                }
                break;

            default:
                java.util.List<Cita> citas = "MEDICO".equals(rol)
                        ? citaDAO.listarPorMedico(idUsuario) : citaDAO.listarTodas();
                request.setAttribute("citas", citas);
                request.getRequestDispatcher("/WEB-INF/views/citas/lista.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession sess = request.getSession(false);
        String rol = (String) sess.getAttribute("usuarioRol");
        int idUsuario = (int) sess.getAttribute("usuarioId");

        if ("ENFERMERO".equals(rol)) {
            response.sendRedirect(request.getContextPath() + "/citas");
            return;
        }

        String lang = (String) sess.getAttribute("lang");
        if (lang == null) {
            lang = "es";
        }
        ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));
        CitaDAO dao = new CitaDAO();
        String idParam = request.getParameter("id");

        try {
            Cita c = new Cita();
            if (idParam != null && !idParam.isEmpty()) {
                c.setId(Integer.parseInt(idParam));
            }
            c.setIdPaciente(Integer.parseInt(request.getParameter("idPaciente")));
            c.setIdMedico(Integer.parseInt(request.getParameter("idMedico")));
            c.setIdEspecialidad(Integer.parseInt(request.getParameter("idEspecialidad")));
            c.setFechaCita(Date.valueOf(request.getParameter("fechaCita")));
            c.setHoraCita(Time.valueOf(request.getParameter("horaCita")));
            c.setMotivo(request.getParameter("motivo"));
            c.setObservaciones(request.getParameter("observaciones"));
            c.setEstado(request.getParameter("estado") != null ? request.getParameter("estado") : "PROGRAMADA");
            c.setIdRegistradoPor(idUsuario);

            if (!dao.estaDisponible(c.getIdMedico(), c.getFechaCita(), c.getHoraCita(), c.getId())) {
                request.setAttribute("error", rb.getString("error.cita.no.disponible"));
                request.setAttribute("cita", c);
                request.setAttribute("especialidades", new EspecialidadDAO().listarTodas());
                request.setAttribute("pacientes", new PacienteDAO().listarTodos());
                request.setAttribute("medicos", new UsuarioDAO().listarMedicos());
                request.getRequestDispatcher("/WEB-INF/views/citas/formulario.jsp").forward(request, response);
                return;
            }
            boolean ok = c.getId() == 0 ? dao.insertar(c) : dao.actualizar(c);
            response.sendRedirect(request.getContextPath() + "/citas?msg=" + (ok ? "guardado" : "error"));
        } catch (Exception e) {
            System.err.println("Error cita: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/citas?msg=error");
        }
    }
}
