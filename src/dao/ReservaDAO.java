package dao;
import models.Administrador;
import models.MetodoPago;
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

        long inicioMillis = nuevaReserva.getHorarioInicio().getTime();
        long finMillis = inicioMillis + (90 * 60 * 1000);
        java.sql.Time horarioFinalCalculado = new java.sql.Time(finMillis);

        String validar = "SELECT COUNT(*) FROM Reserva WHERE numero = ? AND fecha = ? AND (horarioInicio < ? AND horarioFinal > ?)";
        String consulta = "INSERT INTO Reserva (cedulaUsuario, numero, fecha, horarioInicio, horarioFinal, horaCancelacion, metodoPago, estaPagada, estaActiva) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Validar conflictos de horario
        try (PreparedStatement psValidar = DatabaseConnection.getInstancia()
                .getConnection().prepareStatement(validar)) {

            psValidar.setInt(1, nuevaReserva.getNumero());
            psValidar.setDate(2, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            psValidar.setTime(3, horarioFinalCalculado);
            psValidar.setTime(4, nuevaReserva.getHorarioInicio());

            try (ResultSet rs = psValidar.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Error: ya existe una reserva en ese horario.");
                    return;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar reserva", e);
        }

        // Insertar reserva y obtener el ID generado
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nuevaReserva.getCedulaUsuario());
            ps.setInt(2, nuevaReserva.getNumero());
            ps.setDate(3, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            ps.setTime(4, nuevaReserva.getHorarioInicio());
            ps.setTime(5, horarioFinalCalculado);
            ps.setTime(6, nuevaReserva.getHoraCancelacion());
            ps.setString(7, nuevaReserva.getMetodoPago().getValue());
            ps.setBoolean(8, nuevaReserva.isEstaPagada());
            ps.setBoolean(9, nuevaReserva.isEstaActiva());

            ps.executeUpdate();

            // Obtener ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    nuevaReserva.setId(idGenerado);
                    System.out.println("Reserva creada correctamente. ID: " + idGenerado);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la reserva", e);
        }
    }



    public void actualizarReserva(Reserva nuevaReserva) {
        //Calcular automáticamente el horarioFinal como 1 hora 30 minutos después del inicio
        long inicioMillis = nuevaReserva.getHorarioInicio().getTime();
        long finMillis = inicioMillis + (90 * 60 * 1000); // 90 minutos en milisegundos
        java.sql.Time horarioFinalCalculado = new java.sql.Time(finMillis);
        //verificar que no hay mas reservas en ese horario
        String validar = "SELECT COUNT(*) FROM Reserva WHERE idCancha = ? AND fecha = ? AND (horarioInicio < ? AND horarioFinal > ?) AND id <> ?";

        String consulta = "UPDATE Reserva SET cedulaUsuario = ?, numero=?, fecha= ?, horarioInicio= ?, horarioFinal= ?, horaCancelacion= ?, metodoPago= ?, estaPagada= ?, estaActiva= ? WHERE id = ?";

        try (PreparedStatement psValidar = DatabaseConnection.getInstancia()
                .getConnection().prepareStatement(validar)) {

            psValidar.setInt(1, nuevaReserva.getNumero());
            psValidar.setDate(2, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            psValidar.setTime(3, horarioFinalCalculado);             //fin calculado
            psValidar.setTime(4, nuevaReserva.getHorarioInicio());   //inicio dado
            psValidar.setInt(5, nuevaReserva.getNumero());               //excluirse a sí misma

            try (ResultSet rs = psValidar.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("ya existe una reserva en ese horario.");
                    return;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar reserva", e);
        }

        // Si no hay conflicto, actualizar la reserva con el horarioFinal calculado
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConnection().prepareStatement(consulta)) {

            ps.setString(1, nuevaReserva.getCedulaUsuario());
            ps.setInt(2, nuevaReserva.getNumero());
            ps.setDate(3, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            ps.setTime(4, nuevaReserva.getHorarioInicio());
            ps.setTime(5, horarioFinalCalculado); // Se guarda el fin fijo de 1h30m
            ps.setTime(6, nuevaReserva.getHoraCancelacion());
            ps.setString(7, nuevaReserva.getMetodoPago().getValue());
            ps.setBoolean(8, nuevaReserva.isEstaPagada());
            ps.setBoolean(9, nuevaReserva.isEstaActiva());
            ps.setInt(10, nuevaReserva.getNumero());

            ps.executeUpdate();
            System.out.println("Reserva modificada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la reserva", e);
        }
    }



    public void cancelarReserva(int id) {
        String consulta = "UPDATE Reserva SET estaActiva = false, horaCancelacion = CURRENT_TIME WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Reserva cancelada correctamente");
            } else {
                System.out.println("No se encontró la reserva con el ID proporcionado");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Vector<Reserva> listarReservasPorUsuario(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();

        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ? AND estaActiva = true";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cedula = rs.getString("cedulaUsuario");
                    int numero = rs.getInt("numero");
                    Date fecha = rs.getDate("fecha");
                    Time horarioInicio = rs.getTime("horarioInicio");
                    Time horarioFinal = rs.getTime("horarioFinal");
                    Time horaCancelacion = rs.getTime("horaCancelacion");
                    String metodoPagoStr = rs.getString("metodoPago");
                    MetodoPago metodoPagoEnum = MetodoPago.fromString(metodoPagoStr);
                    boolean estaPagada = rs.getBoolean("estaPagada");
                    boolean estaActiva = rs.getBoolean("estaActiva");

                    Reserva reserva = new Reserva(
                            cedula, numero, fecha, horarioInicio, horarioFinal, horaCancelacion,
                            metodoPagoEnum, estaPagada, estaActiva
                    );
                    reserva.setId(rs.getInt("id"));
                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorCancha(int numero) {
        Vector<Reserva> reservas = new Vector<>();

        String consulta = "SELECT * FROM Reserva WHERE numero = ? AND estaActiva = true";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cedula = rs.getString("cedulaUsuario");
                    numero = rs.getInt("numero");
                    Date fecha = rs.getDate("fecha");
                    Time horarioInicio = rs.getTime("horarioInicio");
                    Time horarioFinal = rs.getTime("horarioFinal");
                    Time horaCancelacion = rs.getTime("horaCancelacion");
                    String metodoPagoStr = rs.getString("metodoPago");
                    MetodoPago metodoPagoEnum = MetodoPago.fromString(metodoPagoStr);
                    boolean estaPagada = rs.getBoolean("estaPagada");
                    boolean estaActiva = rs.getBoolean("estaActiva");

                    Reserva reserva = new Reserva(
                            cedula, numero, fecha, horarioInicio, horarioFinal, horaCancelacion,
                            metodoPagoEnum, estaPagada, estaActiva
                    );
                    reserva.setId(rs.getInt("id"));
                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorFecha(Date fecha) {
        Vector<Reserva> reservas = new Vector<>();

        String consulta = "SELECT * FROM Reserva WHERE fecha = ? AND estaActiva = true";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cedula = rs.getString("cedulaUsuario");
                    int numero = rs.getInt("numero");
                    Time horarioInicio = rs.getTime("horarioInicio");
                    Time horarioFinal = rs.getTime("horarioFinal");
                    Time horaCancelacion = rs.getTime("horaCancelacion");
                    String metodoPagoStr = rs.getString("metodoPago");
                    MetodoPago metodoPagoEnum = MetodoPago.fromString(metodoPagoStr);
                    boolean estaPagada = rs.getBoolean("estaPagada");
                    boolean estaActiva = rs.getBoolean("estaActiva");

                    Reserva reserva = new Reserva(
                            cedula, numero, fecha, horarioInicio, horarioFinal, horaCancelacion,
                            metodoPagoEnum, estaPagada, estaActiva
                    );
                    reserva.setId(rs.getInt("id"));
                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorFechaJugador(Date fecha, String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();

        String consulta = "SELECT * FROM Reserva r JOIN Usuario u ON r.cedulaUsuario = u.cedula " +
                "WHERE r.fecha = ? AND r.cedulaUsuario = ? AND r.estaActiva = true";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            ps.setString(2, cedulaUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cedula = rs.getString("cedulaUsuario");
                    int numero = rs.getInt("numero");
                    Time horarioInicio = rs.getTime("horarioInicio");
                    Time horarioFinal = rs.getTime("horarioFinal");
                    Time horaCancelacion = rs.getTime("horaCancelacion");
                    String metodoPagoStr = rs.getString("metodoPago");
                    MetodoPago metodoPagoEnum = MetodoPago.fromString(metodoPagoStr);
                    boolean estaPagada = rs.getBoolean("estaPagada");
                    boolean estaActiva = rs.getBoolean("estaActiva");

                    Reserva reserva = new Reserva(
                            cedula, numero, fecha, horarioInicio, horarioFinal, horaCancelacion,
                            metodoPagoEnum, estaPagada, estaActiva
                    );
                    reserva.setId(rs.getInt("id"));
                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public boolean obtenerEstadoPago(int id) {
        String consulta = "SELECT estaPagada FROM Reserva WHERE id = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean pagada = rs.getBoolean("estaPagada");

                    if (pagada) {
                        System.out.println("La reserva ha sido pagada");
                    } else {
                        System.out.println("La reserva aún no ha sido pagada");
                    }

                    return pagada;
                } else {
                    System.out.println("No se encontró la reserva con el ID proporcionado");
                    return false;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el estado de pago", e);
        }
    }

    public void pagarReserva(int id) {
        if (obtenerEstadoPago(id)) { // Consultaremos si la reserva está paga, en caso que esté, no hará nada.
            return;
        }

        String consulta = "UPDATE Reserva SET estaPagada = TRUE WHERE id = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("La reserva ha sido pagada exitosamente.");
            } else {
                System.out.println("No se encontró la reserva para pagar.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al pagar la reserva", e);
        }
    }

    public void totalReservas() {
        String consulta = "SELECT COUNT(*) AS total FROM Reserva";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int totalReservas = rs.getInt("total");
                System.out.println("Total de reservas: " + totalReservas);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el total de reservas", e);
        }
    }

    public void totalIngresos(java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        String consulta = " SELECT COALESCE(SUM(c.precio), 0) AS total FROM Reserva r JOIN Cancha c ON r.idCancha = c.id WHERE r.fecha BETWEEN ? AND ?";


        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConnection().prepareStatement(consulta)) {

            ps.setDate(1, fechaInicio);  // ESto indica desde que fecha
            ps.setDate(2, fechaFin);     // Y esto hasta que fecha, sirve para filtrar por dia/mes/año, así depende como lo usemos ¡VER EN EL MOMENTO!

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double totalIngresos = rs.getDouble("total");
                    System.out.println("Total de ingresos: " + totalIngresos);
                }
            }

        }  catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            throw new RuntimeException("Error al obtener el total de ingresos", e);
        }

    }

}


