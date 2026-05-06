package sena.adso.saludboyaca.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sena.adso.saludboyaca.dao.CitaDAO;
import sena.adso.saludboyaca.dao.PacienteDAO;

import java.io.IOException;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String rol = (String) session.getAttribute("usuarioRol");
        int idMedico = (int) session.getAttribute("usuarioId");

        CitaDAO citaDAO = new CitaDAO();
        PacienteDAO pacienteDAO = new PacienteDAO();

        request.setAttribute("citasHoy", citaDAO.contarCitasHoy(rol.equals("MEDICO") ? idMedico : 0));
        request.setAttribute("citasPendientes", citaDAO.  contarPendientes(rol.equals("MEDICO") ? idMedico : 0));
        request.setAttribute("citasMes", citaDAO.contarMes(rol.equals("MEDICO") ? idMedico : 0));
        request.setAttribute("totalPacientes", pacienteDAO.contarTotal());
        request.setAttribute("proximasCitas", citaDAO.listarProximas(rol.equals("MEDICO") ? idMedico : 0, 10));

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
