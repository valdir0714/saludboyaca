package sena.adso.saludboyaca.dao;

import sena.adso.saludboyaca.model.Conexion;

import java.sql.*;

public class OTPTokenDAO {

    public boolean insertar(int idUsuario, String codigo, Timestamp expira) {
        String sql = "INSERT INTO otp_tokens (id_usuario, codigo, expira_en, usado) VALUES (?,?,?,0)";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setString(2, codigo);
            ps.setTimestamp(3, expira);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertar OTP: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    public boolean esValido(int idUsuario, String codigo) {
        String sql = "SELECT id FROM otp_tokens WHERE id_usuario=? AND codigo=? "
                + "AND expira_en > NOW() AND usado=0 ORDER BY id DESC LIMIT 1";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setString(2, codigo);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error validar OTP: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    public boolean marcarUsado(int idUsuario, String codigo) {
        String sql = "UPDATE otp_tokens SET usado=1 WHERE id_usuario=? AND codigo=?";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setString(2, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marcarUsado OTP: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }
}
