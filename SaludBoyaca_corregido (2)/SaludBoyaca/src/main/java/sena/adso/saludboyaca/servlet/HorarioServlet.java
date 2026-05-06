package sena.adso.saludboyaca.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sena.adso.saludboyaca.dao.HorarioDAO;

import java.io.IOException;

@WebServlet(name = "HorarioServlet", urlPatterns = {"/horarios"})
public class HorarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HorarioDAO dao = new HorarioDAO();
        request.setAttribute("horarios", dao.listarTodos());
        request.getRequestDispatcher("/WEB-INF/views/horarios/lista.jsp").forward(request, response);
    }
}
