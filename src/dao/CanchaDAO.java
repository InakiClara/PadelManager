package dao;
import database.DatabaseConnection;
import models.CanchaHorario;
import models.Cancha;

import java.sql.*;
import java.util.Vector;

public class CanchaDAO {
    public void altaCancha(Cancha nuevaCancha) {
        String consultaCancha = "INSERT INTO Cancha (numero, precio, esTechada, estaDisponible) VALUES (?, ?, ?, ?, ?)";
        String consultaHorario = "INSERT INTO CanchaHorario (numero, horario) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement psCancha = null;
        PreparedStatement psHorario = null;

        try {
            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false);

            psCancha = conn.prepareStatement(consultaCancha);
            psCancha.setInt(1, nuevaCancha.getNumero());
            psCancha.setDouble(2, nuevaCancha.getPrecio());
            psCancha.setBoolean(3, nuevaCancha.isEsTechada());
            psCancha.setBoolean(4, nuevaCancha.isEstaDisponible());
            psCancha.executeUpdate();

            psHorario = conn.prepareStatement(consultaHorario);
            Vector<Time> listaHorarios = nuevaCancha.getHorario().getHorarios();
            for (Time horario : listaHorarios) {
                psHorario.setInt(1, nuevaCancha.getNumero());
                psHorario.setTime(2, horario);
                psHorario.addBatch();
            }
            psHorario.executeBatch();
            conn.commit();
            System.out.println("Cancha y horarios creados correctamente");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Error rollback: " + ex.getMessage()); }
            }
            throw new RuntimeException("No se pudo registrar la cancha: " + e.getMessage(), e);

        } finally {
            try {
                if (psCancha != null) psCancha.close();
                if (psHorario != null) psHorario.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    public void actualizarCancha(Cancha canchaActualizada) {
        String consultaCancha = "UPDATE Cancha SET precio = ?, esTechada = ?, estaDisponible = ? WHERE numero = ?";
        String eliminarHorarios = "DELETE FROM CanchaHorario WHERE numero = ?";
        String insertarHorario = "INSERT INTO CanchaHorario (numero, horario) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement psCancha = null, psEliminarHorarios = null, psInsertarHorario = null;

        try {
            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false);

            psCancha = conn.prepareStatement(consultaCancha);
            psCancha.setInt(1, canchaActualizada.getNumero());
            psCancha.setDouble(2, canchaActualizada.getPrecio());
            psCancha.setBoolean(3, canchaActualizada.isEsTechada());
            psCancha.setBoolean(4, canchaActualizada.isEstaDisponible());
            psCancha.executeUpdate();

            psEliminarHorarios = conn.prepareStatement(eliminarHorarios);
            psEliminarHorarios.setInt(1, canchaActualizada.getNumero());
            psEliminarHorarios.executeUpdate();

            psInsertarHorario = conn.prepareStatement(insertarHorario);
            Vector<Time> listaHorarios = canchaActualizada.getHorario().getHorarios();
            for (Time horario : listaHorarios) {
                psInsertarHorario.setInt(1, canchaActualizada.getNumero());
                psInsertarHorario.setTime(2, horario);
                psInsertarHorario.addBatch();
            }
            psInsertarHorario.executeBatch();

            conn.commit();
            System.out.println("Cancha actualizada correctamente");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Error rollback: " + ex.getMessage()); }
            }
            throw new RuntimeException("No se pudo actualizar la cancha: " + e.getMessage(), e);

        } finally {
            try {
                if (psCancha != null) psCancha.close();
                if (psEliminarHorarios != null) psEliminarHorarios.close();
                if (psInsertarHorario != null) psInsertarHorario.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    public Vector<Cancha> listarCancha() {
        String consultaCanchas = "SELECT * FROM Cancha";
        String consultaHorarios = "SELECT horario FROM CanchaHorario WHERE numero = ?";
        Vector<Cancha> listaCanchas = new Vector<>();

        try (Connection conn = DatabaseConnection.getInstancia().getConnection();
             PreparedStatement psCanchas = conn.prepareStatement(consultaCanchas);
             ResultSet rsCanchas = psCanchas.executeQuery()) {

            while (rsCanchas.next()) {
                int numero = rsCanchas.getInt("numero");
                double precio = rsCanchas.getDouble("precio");
                boolean esTechada = rsCanchas.getBoolean("esTechada");
                boolean estaDisponible = rsCanchas.getBoolean("estaDisponible");

                PreparedStatement psHorarios = conn.prepareStatement(consultaHorarios);
                psHorarios.setInt(1, numero);
                ResultSet rsHorarios = psHorarios.executeQuery();

                Vector<Time> horarios = new Vector<>();
                while (rsHorarios.next()) {
                    horarios.add(rsHorarios.getTime("horario"));
                }
                rsHorarios.close();
                psHorarios.close();

                CanchaHorario canchaHorario = new CanchaHorario(numero, horarios);
                Cancha cancha = new Cancha(numero, esTechada, precio, estaDisponible, canchaHorario);
                listaCanchas.add(cancha);

                System.out.printf("-Numero: %d | Precio: %.2f | Techada: %s | Disponible: %s%n",
                         numero, precio, esTechada ? "Sí" : "No", estaDisponible ? "Sí" : "No");
                System.out.println("  Horarios:");
                for (Time h : horarios) System.out.println("    " + h);
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudieron obtener las canchas: " + e.getMessage(), e);
        }

        return listaCanchas;
    }


    public void desactivarCancha(Cancha canchaDesactivar){
        String consultaReservas = "SELECT COUNT(*) FROM Reserva WHERE numero = ? AND estaActiva = TRUE";
        String eliminarHorarios = "DELETE FROM CanchaHorario WHERE numero = ?";
        String eliminarCancha = "DELETE FROM Cancha WHERE id = ?";

        Connection conn = null;
        PreparedStatement psReservas = null;
        PreparedStatement psEliminarHorarios = null;
        PreparedStatement psEliminarCancha = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false);

            psReservas = conn.prepareStatement(consultaReservas);
            psReservas.setInt(1, canchaDesactivar.getNumero());
            rs = psReservas.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("No se puede eliminar la cancha. Tiene reservas activas.");
                conn.rollback();
                return;
            }

            psEliminarHorarios = conn.prepareStatement(eliminarHorarios);
            psEliminarHorarios.setInt(1, canchaDesactivar.getNumero());
            psEliminarHorarios.executeUpdate();

            psEliminarCancha = conn.prepareStatement(eliminarCancha);
            psEliminarCancha.setInt(1, canchaDesactivar.getNumero());
            psEliminarCancha.executeUpdate();

            conn.commit();
            System.out.println("Cancha y sus horarios eliminados correctamente (Numero: " + canchaDesactivar.getNumero());

        } catch(SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Error rollback: " + ex.getMessage()); }
            }
            throw new RuntimeException("No se pudo eliminar la cancha: " + e.getMessage(), e);

        } finally {
            try {
                if (rs != null) rs.close();
                if (psReservas != null) psReservas.close();
                if (psEliminarHorarios != null) psEliminarHorarios.close();
                if (psEliminarCancha != null) psEliminarCancha.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


    public Vector<Cancha> busquedaAvanzada(Double minPrecio, Double maxPrecio,
                                           Boolean esTechada, Boolean disponible,
                                           Date fecha, Time hora) {

        Vector<Cancha> listaCanchas = new Vector<>();
        StringBuilder consulta = new StringBuilder(
                "SELECT c.numero, c.numero, c.precio, c.esTechada, c.estaDisponible, " +
                        "GROUP_CONCAT(ch.horario SEPARATOR ',') AS horarios " +
                        "FROM Cancha c " +
                        "JOIN CanchaHorario ch ON c.numero = ch.numero WHERE 1=1 "
        );

        if (minPrecio != null) consulta.append("AND c.precio >= ? ");
        if (maxPrecio != null) consulta.append("AND c.precio <= ? ");
        if (esTechada != null) consulta.append("AND c.esTechada = ? ");
        if (disponible != null) consulta.append("AND c.estaDisponible = ? ");
        if (hora != null) consulta.append("AND ch.horario = ? ");
        consulta.append("GROUP BY c.numero");

        try (Connection conn = DatabaseConnection.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta.toString())) {

            int index = 1;
            if (minPrecio != null) ps.setDouble(index++, minPrecio);
            if (maxPrecio != null) ps.setDouble(index++, maxPrecio);
            if (esTechada != null) ps.setBoolean(index++, esTechada);
            if (disponible != null) ps.setBoolean(index++, disponible);
            if (hora != null) ps.setTime(index++, hora);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int numero = rs.getInt("numero");
                    double precio = rs.getDouble("precio");
                    boolean techada = rs.getBoolean("esTechada");
                    boolean estaDisponible = rs.getBoolean("estaDisponible");

                    String horariosStr = rs.getString("horarios");
                    Vector<Time> horarios = new Vector<>();
                    if (horariosStr != null && !horariosStr.isEmpty()) {
                        for (String parte : horariosStr.split(",")) {
                            horarios.add(Time.valueOf(parte.trim()));
                        }
                    }

                    CanchaHorario canchaHorario = new CanchaHorario(numero, horarios);
                    Cancha cancha = new Cancha(numero, techada, precio, estaDisponible, canchaHorario);
                    listaCanchas.add(cancha);

                    System.out.printf("-Numero: %d | Precio: %.2f | Techada: %s | Disponible: %s%n",
                            numero, precio, techada ? "Sí" : "No", estaDisponible ? "Sí" : "No");
                    System.out.println("  Horarios:");
                    for (Time h : horarios) System.out.println("    " + h);
                }
            }

            if (listaCanchas.isEmpty()) {
                System.out.println("No se encontraron canchas con esos criterios.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo realizar la búsqueda avanzada: " + e.getMessage(), e);
        }

        return listaCanchas;
    }


    public void bloquearCanchaPorMantenimiento(Cancha canchaBloquear){
        String consulta = "UPDATE Cancha SET estaDisponible = ? WHERE numero = ?";
        PreparedStatement ps = null;

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            ps = conn.prepareStatement(consulta);
            ps.setBoolean(1, false);
            ps.setInt(2, canchaBloquear.getNumero());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cancha bloqueada por mantenimiento (Numero: " + canchaBloquear.getNumero() + ")");
            } else {
                System.out.println("No se encontró ninguna cancha con ese Numero.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al bloquear la cancha: " + e.getMessage(), e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }


    public void desbloquearCancha(Cancha canchaDesbloquear){
        String consulta = "UPDATE Cancha SET estaDisponible = ? WHERE numero = ?";
        PreparedStatement ps = null;

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            ps = conn.prepareStatement(consulta);
            ps.setBoolean(1, true);
            ps.setInt(2, canchaDesbloquear.getNumero());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cancha desbloqueada (Numero: " + canchaDesbloquear.getNumero() + ")");
            } else {
                System.out.println("No se encontró ninguna cancha con ese Numero.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al desbloquear la cancha: " + e.getMessage(), e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

}
