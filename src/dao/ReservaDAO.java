package dao;
import models.Reserva;

import database.DatabaseConnection;

import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Vector;

public class ReservaDAO {

    public void crearReserva(Reserva nuevaReserva) {
        String consulta = "INSERT INTO Reserva (id, cedulaUsuario, idCancha, fecha, horarioInicio, horarioFinal, horaCancelacion, metodoPago, estaPagada, estaActiva) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
            ps.setInt(1, nuevaReserva.getId());
            ps.setString(2, nuevaReserva.getCedulaUsuario());
            ps.setInt(3, nuevaReserva.getIdCancha());
            ps.setDate(4, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            ps.setTime(5, nuevaReserva.getHorarioInicio());
            ps.setTime(6, nuevaReserva.getHorarioFinal());
            ps.setTime(7, nuevaReserva.getHoraCancelacion());
            ps.setString(8, nuevaReserva.getMetodoPago());
            ps.setBoolean(9, nuevaReserva.isEstaPagada());
            ps.setBoolean(10, nuevaReserva.isEstaActiva());
            ps.executeUpdate();
            System.out.println("Reserva creada correctamente");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void actualizarReserva(Reserva nuevaReserva) {
        String consulta = "UPDATE Reserva SET cedulaUsuario = ?, idCancha= ?, fecha= ?, horarioInicio= ?, horarioFinal= ?, horaCancelacion= ?, metodoPago= ?, estaPagada= ?, estaActiva= ? WHERE id = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
            ps.setInt(1, nuevaReserva.getId());
            ps.setString(2, nuevaReserva.getCedulaUsuario());
            ps.setInt(3, nuevaReserva.getIdCancha());
            ps.setDate(4, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            ps.setTime(5, nuevaReserva.getHorarioInicio());
            ps.setTime(6, nuevaReserva.getHorarioFinal());
            ps.setTime(7, nuevaReserva.getHoraCancelacion());
            ps.setString(8, nuevaReserva.getMetodoPago());
            ps.setBoolean(9, nuevaReserva.isEstaPagada());
            ps.setBoolean(10, nuevaReserva.isEstaActiva());
            ps.executeUpdate();
            System.out.println("Reserva modificado correctamente");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void cancelarReserva(Reserva nuevaReserva) {
        String consulta = "DELETE FROM Reserva WHERE id = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
            ps.setInt(1, nuevaReserva.getId());
            ps.executeUpdate();
            System.out.println("Reserva cancelada correctamente");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Vector<Reserva> listarReservasPorUsuario(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();

        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String cedula = rs.getString("cedulaUsuario");
                    int idCancha1 = rs.getInt("idCancha");
                    Date fecha = rs.getDate("fecha");
                    Time horarioInicio = rs.getTime("horarioInicio");
                    Time horarioFinal = rs.getTime("horarioFinal");
                    Time horaCancelacion = rs.getTime("horaCancelacion");
                    String metodoPago = rs.getString("metodoPago");
                    boolean estaPagada = rs.getBoolean("estaPagada");
                    boolean estaActiva = rs.getBoolean("estaActiva");

                    reservas.add(new Reserva(id, cedula, idCancha1, fecha, horarioInicio, horarioFinal, horaCancelacion, metodoPago, estaPagada, estaActiva));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorCancha(int idCancha) {
        Vector<Reserva> reservas = new Vector<>();

        String consulta = "SELECT * FROM Reserva WHERE idCancha = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, idCancha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String cedula = rs.getString("cedulaUsuario");
                    int idCancha1 = rs.getInt("idCancha");
                    Date fecha = rs.getDate("fecha");
                    Time horarioInicio = rs.getTime("horarioInicio");
                    Time horarioFinal = rs.getTime("horarioFinal");
                    Time horaCancelacion = rs.getTime("horaCancelacion");
                    String metodoPago = rs.getString("metodoPago");
                    boolean estaPagada = rs.getBoolean("estaPagada");
                    boolean estaActiva = rs.getBoolean("estaActiva");

                    reservas.add(new Reserva(id, cedula, idCancha1, fecha, horarioInicio, horarioFinal, horaCancelacion, metodoPago, estaPagada, estaActiva));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }




}
