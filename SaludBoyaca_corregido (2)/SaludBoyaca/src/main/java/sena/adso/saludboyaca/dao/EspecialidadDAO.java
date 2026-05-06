package sena.adso.saludboyaca.dao;

import sena.adso.saludboyaca.dto.Especialidad;
import sena.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAO {

    public List<Especialidad> listarTodas() {
        String sql = "SELECT * FROM especialidades ORDER BY nombre";
        List<Especialidad> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Especialidad e = new Especialidad();
                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));
                e.setDescripcion(rs.getString("descripcion"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.err.println("Error listarTodas especialidades: " + ex.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    public Especialidad buscarPorId(int id) {
        String sql = "SELECT * FROM especialidades WHERE id = ?";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Especialidad e = new Especialidad();
                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));
                e.setDescripcion(rs.getString("descripcion"));
                return e;
            }
        } catch (SQLException ex) {
            System.err.println("Error buscarPorId especialidad: " + ex.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return null;
    }
}