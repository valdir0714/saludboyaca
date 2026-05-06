package sena.adso.saludboyaca.dao;

import sena.adso.saludboyaca.dto.Usuario;
import sena.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario validarLogin(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND activo = 1";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error validarLogin: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return null;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error buscarPorId: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return null;
    }

    public List<Usuario> listarMedicos() {
        return listarPorRol("MEDICO");
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios ORDER BY apellidos, nombres";
        List<Usuario> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarTodos: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    private List<Usuario> listarPorRol(String rol) {
        String sql = "SELECT * FROM usuarios WHERE rol = ? AND activo = 1 ORDER BY apellidos, nombres";
        List<Usuario> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rol);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarPorRol: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    public List<Usuario> listarMedicosPorEspecialidad(int idEspecialidad) {
        String sql = "SELECT u.* FROM usuarios u "
                + "JOIN especialidades e ON e.nombre = u.especialidad "
                + "WHERE e.id = ? AND u.rol = 'MEDICO' AND u.activo = 1 "
                + "ORDER BY u.apellidos";
        List<Usuario> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idEspecialidad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarMedicosPorEspecialidad: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombres(rs.getString("nombres"));
        u.setApellidos(rs.getString("apellidos"));
        u.setDocumento(rs.getString("documento"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setEspecialidad(rs.getString("especialidad"));
        u.setLangPreferido(rs.getString("lang_preferido"));
        u.setActivo(rs.getBoolean("activo"));
        return u;
    }
}
