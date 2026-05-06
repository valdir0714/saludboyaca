package sena.adso.saludboyaca.dao;

import sena.adso.saludboyaca.dto.Horario;
import sena.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioDAO {

    public List<Horario> listarPorMedico(int idMedico) {
        String sql = "SELECT h.*, CONCAT(u.nombres, ' ', u.apellidos) AS nombre_medico "
                + "FROM horarios h JOIN usuarios u ON u.id = h.id_medico "
                + "WHERE h.id_medico = ? ORDER BY h.dia_semana, h.hora_inicio";
        List<Horario> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idMedico);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarPorMedico: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    public List<Horario> listarTodos() {
        String sql = "SELECT h.*, CONCAT(u.nombres, ' ', u.apellidos) AS nombre_medico "
                + "FROM horarios h JOIN usuarios u ON u.id = h.id_medico "
                + "ORDER BY u.apellidos, h.dia_semana, h.hora_inicio";
        List<Horario> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarTodos horarios: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return lista;
    }

    /**
     * Devuelve lista de horas disponibles (en formato HH:mm) para un médico en
     * una fecha. Genera slots cada 30 minutos dentro del horario del día,
     * excluyendo los que ya tienen cita.
     */
    public List<String> horasDisponibles(int idMedico, java.sql.Date fecha) {
        // Obtener día de semana (1=Lun...5=Vie) desde la fecha
        String sqlHorario = "SELECT hora_inicio, hora_fin, max_citas FROM horarios "
                + "WHERE id_medico = ? AND dia_semana = DAYOFWEEK(?) - 1";
        // DAYOFWEEK: 1=Dom, 2=Lun ... ajustamos: 2->1, 3->2 ...
        // Mejor usar: MOD(DAYOFWEEK(?) + 5, 7) + 1 para 1=Lun
        sqlHorario = "SELECT hora_inicio, hora_fin, max_citas FROM horarios "
                + "WHERE id_medico = ? AND dia_semana = MOD(DAYOFWEEK(?) + 5, 7) + 1";

        String sqlCitas = "SELECT hora_cita FROM citas WHERE id_medico = ? AND fecha_cita = ? "
                + "AND estado != 'CANCELADA'";

        List<String> disponibles = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Conexion.getConnection();

            PreparedStatement psH = conn.prepareStatement(sqlHorario);
            psH.setInt(1, idMedico);
            psH.setDate(2, fecha);
            ResultSet rsH = psH.executeQuery();

            if (!rsH.next()) {
                return disponibles;
            }

            Time inicio = rsH.getTime("hora_inicio");
            Time fin = rsH.getTime("hora_fin");

            // Horas ocupadas
            PreparedStatement psC = conn.prepareStatement(sqlCitas);
            psC.setInt(1, idMedico);
            psC.setDate(2, fecha);
            ResultSet rsC = psC.executeQuery();
            List<String> ocupadas = new ArrayList<>();
            while (rsC.next()) {
                ocupadas.add(rsC.getTime("hora_cita").toString().substring(0, 5));
            }

            // Generar slots cada 30 min
            long t = inicio.getTime();
            long tFin = fin.getTime();
            while (t < tFin) {
                Time slot = new Time(t);
                String slotStr = slot.toString().substring(0, 5);
                if (!ocupadas.contains(slotStr)) {
                    disponibles.add(slotStr);
                }
                t += 30 * 60 * 1000L;
            }
        } catch (SQLException e) {
            System.err.println("Error horasDisponibles: " + e.getMessage());
        } finally {
            Conexion.closeConnection(conn);
        }
        return disponibles;
    }

    private Horario mapear(ResultSet rs) throws SQLException {
        Horario h = new Horario();
        h.setId(rs.getInt("id"));
        h.setIdMedico(rs.getInt("id_medico"));
        h.setNombreMedico(rs.getString("nombre_medico"));
        h.setDiaSemana(rs.getInt("dia_semana"));
        h.setHoraInicio(rs.getTime("hora_inicio"));
        h.setHoraFin(rs.getTime("hora_fin"));
        h.setMaxCitas(rs.getInt("max_citas"));
        return h;
    }
}
