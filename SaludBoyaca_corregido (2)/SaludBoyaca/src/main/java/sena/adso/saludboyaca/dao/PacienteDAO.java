package sena.adso.saludboyaca.dao;

import sena.adso.saludboyaca.dto.Paciente;
import sena.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM pacientes ORDER BY apellidos, nombres";
        List<Paciente> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarTodos pacientes: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    public Paciente buscarPorId(int id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
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
            System.err.println("Error buscarPorId paciente: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return null;
    }

    public Paciente buscarPorDocumento(String documento) {
        String sql = "SELECT * FROM pacientes WHERE documento = ?";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error buscarPorDocumento: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return null;
    }

    public boolean insertar(Paciente p) {
        String sql = "INSERT INTO pacientes (nombres, apellidos, tipo_documento, documento, "
                + "fecha_nacimiento, genero, telefono, email, eps, vereda_barrio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getTipoDocumento());
            ps.setString(4, p.getDocumento());
            ps.setDate(5, p.getFechaNacimiento());
            ps.setString(6, p.getGenero());
            ps.setString(7, p.getTelefono());
            ps.setString(8, p.getEmail());
            ps.setString(9, p.getEps());
            ps.setString(10, p.getVeredaBarrio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertar paciente: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    public boolean actualizar(Paciente p) {
        String sql = "UPDATE pacientes SET nombres=?, apellidos=?, tipo_documento=?, "
                + "documento=?, fecha_nacimiento=?, genero=?, telefono=?, email=?, "
                + "eps=?, vereda_barrio=? WHERE id=?";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getTipoDocumento());
            ps.setString(4, p.getDocumento());
            ps.setDate(5, p.getFechaNacimiento());
            ps.setString(6, p.getGenero());
            ps.setString(7, p.getTelefono());
            ps.setString(8, p.getEmail());
            ps.setString(9, p.getEps());
            ps.setString(10, p.getVeredaBarrio());
            ps.setInt(11, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizar paciente: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminar paciente: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM pacientes";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error contarTotal pacientes: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return 0;
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNombres(rs.getString("nombres"));
        p.setApellidos(rs.getString("apellidos"));
        p.setTipoDocumento(rs.getString("tipo_documento"));
        p.setDocumento(rs.getString("documento"));
        p.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        p.setGenero(rs.getString("genero"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setEps(rs.getString("eps"));
        p.setVeredaBarrio(rs.getString("vereda_barrio"));
        return p;
    }
}
