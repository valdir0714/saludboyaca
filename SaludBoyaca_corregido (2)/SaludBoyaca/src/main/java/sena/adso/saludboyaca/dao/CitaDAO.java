package sena.adso.saludboyaca.dao;

import sena.adso.saludboyaca.dto.Cita;
import sena.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    private static final String SELECT_BASE
            = "SELECT c.*, "
            + "CONCAT(p.nombres,' ',p.apellidos) AS nombre_paciente, p.documento AS doc_paciente, "
            + "CONCAT(u.nombres,' ',u.apellidos) AS nombre_medico, "
            + "e.nombre AS nombre_especialidad "
            + "FROM citas c "
            + "JOIN pacientes    p ON p.id = c.id_paciente "
            + "JOIN usuarios     u ON u.id = c.id_medico "
            + "JOIN especialidades e ON e.id = c.id_especialidad ";

    public List<Cita> listarTodas() {
        return listarConFiltro("", 0);
    }

    public List<Cita> listarPorMedico(int idMedico) {
        return listarConFiltro("WHERE c.id_medico = ? ", idMedico);
    }

    private List<Cita> listarConFiltro(String where, int param) {
        String sql = SELECT_BASE + where + "ORDER BY c.fecha_cita DESC, c.hora_cita";
        List<Cita> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            if (param > 0) {
                ps.setInt(1, param);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarCitas: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    public List<Cita> listarProximas(int idMedico, int limite) {
        String filtroMedico = idMedico > 0 ? "AND c.id_medico = ? " : "";
        String sql = SELECT_BASE
                + "WHERE c.fecha_cita >= CURDATE() AND c.estado != 'CANCELADA' "
                + filtroMedico
                + "ORDER BY c.fecha_cita, c.hora_cita LIMIT " + limite;
        List<Cita> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            if (idMedico > 0) {
                ps.setInt(1, idMedico);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarProximas: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    public List<Cita> listarPorPacienteDoc(String documento) {
        String sql = SELECT_BASE
                + "WHERE p.documento = ? AND c.estado != 'CANCELADA' "
                + "ORDER BY c.fecha_cita DESC";
        List<Cita> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarPorDoc: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    public Cita buscarPorId(int id) {
        String sql = SELECT_BASE + "WHERE c.id = ?";
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
            System.err.println("Error buscarCitaPorId: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return null;
    }

    public boolean insertar(Cita c) {
        String sql = "INSERT INTO citas (id_paciente, id_medico, id_especialidad, "
                + "fecha_cita, hora_cita, motivo, estado, observaciones, id_registrado_por) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, c.getIdPaciente());
            ps.setInt(2, c.getIdMedico());
            ps.setInt(3, c.getIdEspecialidad());
            ps.setDate(4, c.getFechaCita());
            ps.setTime(5, c.getHoraCita());
            ps.setString(6, c.getMotivo());
            ps.setString(7, c.getEstado() != null ? c.getEstado() : "PROGRAMADA");
            ps.setString(8, c.getObservaciones());
            ps.setInt(9, c.getIdRegistradoPor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertar cita: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    public boolean actualizar(Cita c) {
        String sql = "UPDATE citas SET id_paciente=?, id_medico=?, id_especialidad=?, "
                + "fecha_cita=?, hora_cita=?, motivo=?, observaciones=? WHERE id=?";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, c.getIdPaciente());
            ps.setInt(2, c.getIdMedico());
            ps.setInt(3, c.getIdEspecialidad());
            ps.setDate(4, c.getFechaCita());
            ps.setTime(5, c.getHoraCita());
            ps.setString(6, c.getMotivo());
            ps.setString(7, c.getObservaciones());
            ps.setInt(8, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizar cita: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    public boolean cambiarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE citas SET estado = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error cambiarEstado: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    public boolean eliminar(int id) {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM citas WHERE id = ?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminar cita: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    public boolean estaDisponible(int idMedico, Date fecha, Time hora, int excluirId) {
        String sql = "SELECT COUNT(*) FROM citas "
                + "WHERE id_medico=? AND fecha_cita=? AND hora_cita=? "
                + "AND estado != 'CANCELADA' AND id != ?";
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idMedico);
            ps.setDate(2, fecha);
            ps.setTime(3, hora);
            ps.setInt(4, excluirId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            System.err.println("Error estaDisponible: " + e.getMessage());
            return false;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    // ── Contadores para Dashboard ────────────────────────────────────
    public int contarCitasHoy(int idMedico) {
        String filtro = idMedico > 0 ? "AND id_medico = ?" : "";
        String sql = "SELECT COUNT(*) FROM citas WHERE fecha_cita = CURDATE() " + filtro;
        return contarConFiltro(sql, idMedico);
    }

    public int contarPendientes(int idMedico) {
        String filtro = idMedico > 0 ? "AND id_medico = ?" : "";
        String sql = "SELECT COUNT(*) FROM citas WHERE estado IN ('PROGRAMADA','CONFIRMADA') "
                + "AND fecha_cita >= CURDATE() " + filtro;
        return contarConFiltro(sql, idMedico);
    }

    public int contarMes(int idMedico) {
        String filtro = idMedico > 0 ? "AND id_medico = ?" : "";
        String sql = "SELECT COUNT(*) FROM citas "
                + "WHERE MONTH(fecha_cita)=MONTH(CURDATE()) AND YEAR(fecha_cita)=YEAR(CURDATE()) " + filtro;
        return contarConFiltro(sql, idMedico);
    }

    private int contarConFiltro(String sql, int param) {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            if (param > 0) {
                ps.setInt(1, param);
            }
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.err.println("Error contarCitas: " + e.getMessage());
            return 0;
        } finally {
            Conexion.closeConnection(conn);
        }
    }

    private Cita mapear(ResultSet rs) throws SQLException {
        Cita c = new Cita();
        c.setId(rs.getInt("id"));
        c.setIdPaciente(rs.getInt("id_paciente"));
        c.setNombrePaciente(rs.getString("nombre_paciente"));
        c.setDocumentoPaciente(rs.getString("doc_paciente"));
        c.setIdMedico(rs.getInt("id_medico"));
        c.setNombreMedico(rs.getString("nombre_medico"));
        c.setIdEspecialidad(rs.getInt("id_especialidad"));
        c.setNombreEspecialidad(rs.getString("nombre_especialidad"));
        c.setFechaCita(rs.getDate("fecha_cita"));
        c.setHoraCita(rs.getTime("hora_cita"));
        c.setMotivo(rs.getString("motivo"));
        c.setEstado(rs.getString("estado"));
        c.setObservaciones(rs.getString("observaciones"));
        c.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        c.setIdRegistradoPor(rs.getInt("id_registrado_por"));
        return c;
    }
}
