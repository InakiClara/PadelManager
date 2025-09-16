package dao;
import database.DatabaseConnection;
import models.CanchaHorario;
import models.Cancha;

import java.sql.*;
import java.util.Vector;

public class CanchaDAO {
    public void altaCancha(Cancha nuevaCancha){
        String consultaCancha = "INSERT INTO Cancha (id, precio, esTechada, estaDisponible) VALUES (?, ?, ?, ?)";
        String consultaHorario = "INSERT INTO CanchaHorario (idCancha, horario) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement psCancha = null;
        PreparedStatement psHorario = null;
        try {
            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false);

            psCancha = conn.prepareStatement(consultaCancha);
            psCancha.setInt(1, nuevaCancha.getId());
            psCancha.setDouble(2, nuevaCancha.getPrecio());
            psCancha.setBoolean(3, nuevaCancha.isEsTechada());
            psCancha.setBoolean(4, nuevaCancha.isEstaDispoonible());
            psCancha.executeUpdate();
            psHorario = conn.prepareStatement(consultaHorario);
            Vector<java.sql.Time> listaHorarios = nuevaCancha.getHorario().getHorarios();//Procesamiento por lotes
            for(java.sql.Time horario: listaHorarios){
                psHorario.setInt(1, nuevaCancha.getId());
                psHorario.setTime(2, horario);
                psHorario.addBatch();
            }
            //Enviar las sentencias en una sola operación
            psHorario.executeBatch();
            conn.commit();
            System.out.println("Cancha y horarios creados correctamente");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
                }
            }
            throw new RuntimeException("Error al dar de alta la cancha: " + e.getMessage(), e);
        } finally{ //Cierre de recursos
            try{
                if(psCancha != null) {
                    psCancha.close();
                }
                if(psHorario != null){
                    psHorario.close();
                }
                if(conn != null){conn.setAutoCommit(true);}
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());;
            }
        }
    }
    public void actualizarCancha(Cancha canchaActualizada){
        String consultaCancha = "UPDATE Cancha SET precio = ?, esTechada = ?, estaDisponible = ? WHERE id = ?";
        String eliminarHorarios = "DELETE FROM CanchaHorario WHERE idCancha = ?";
        String insertarHorario = "INSERT INTO CanchaHorario (idCancha, horario) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement psCancha = null;
        PreparedStatement psEliminarHorarios = null;
        PreparedStatement psInsertarHorario = null;
        try{
            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false);

            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false);

            //Actualizar datos de la cancha
            psCancha = conn.prepareStatement(consultaCancha);
            psCancha.setDouble(1, canchaActualizada.getPrecio());
            psCancha.setBoolean(2, canchaActualizada.isEsTechada());
            psCancha.setBoolean(3, canchaActualizada.isEstaDispoonible());
            psCancha.setInt(4, canchaActualizada.getId());
            psCancha.executeUpdate();

            //Eliminar horarios antiguos
            psEliminarHorarios = conn.prepareStatement(eliminarHorarios);
            psEliminarHorarios.setInt(1, canchaActualizada.getId());
            psEliminarHorarios.executeUpdate();

            //Insertar nuevos horarios
            psInsertarHorario = conn.prepareStatement(insertarHorario);
            Vector<java.sql.Time> listaHorarios = canchaActualizada.getHorario().getHorarios();//Procesamiento por lotes
            for(java.sql.Time horario: listaHorarios){
                psInsertarHorario.setInt(1, canchaActualizada.getId());
                psInsertarHorario.setTime(2, horario);
                psInsertarHorario.addBatch();
            }
            psInsertarHorario.executeBatch();

            conn.commit();
            System.out.println("Cancha actualizada correctamente");

        }catch(SQLException e){
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
                }
            }
            throw new RuntimeException("Error al actualizar la cancha: " + e.getMessage(), e);
        }finally {//Cierre de recursos
            try {
                if (psCancha != null) psCancha.close();
                if (psInsertarHorario != null) psInsertarHorario.close();
                if (psEliminarHorarios != null) psEliminarHorarios.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException closeEx) {
                System.err.println("Error al cerrar recursos: " + closeEx.getMessage());
            }
        }
    }
    public Vector<Cancha> listarCancha() {
        String consultaCanchas = "SELECT * FROM Cancha;";
        String consultaHorarios = "SELECT horario FROM CanchaHorario WHERE idCancha = ?";

        Vector<Cancha> listaCanchas = new Vector<>();

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            PreparedStatement psCanchas = conn.prepareStatement(consultaCanchas);
            ResultSet rsCanchas = psCanchas.executeQuery();

            while (rsCanchas.next()) {
                int id = rsCanchas.getInt("id");
                double precio = rsCanchas.getDouble("precio");
                boolean esTechada = rsCanchas.getBoolean("esTechada");
                boolean estaDisponible = rsCanchas.getBoolean("estaDisponible");

                PreparedStatement psHorarios = conn.prepareStatement(consultaHorarios);
                psHorarios.setInt(1, id);
                ResultSet rsHorarios = psHorarios.executeQuery();

                Vector<Time> horarios = new Vector<>();
                while (rsHorarios.next()) {
                    horarios.add(rsHorarios.getTime("horario"));
                }

                rsHorarios.close();
                psHorarios.close();

                CanchaHorario canchaHorario = new CanchaHorario(id, horarios);
                Cancha cancha = new Cancha(id, esTechada, precio, estaDisponible, canchaHorario);
                listaCanchas.add(cancha);

                System.out.printf("-ID: %d | Precio: %.2f | Techada: %s | Disponible: %s%n",
                        id, precio, esTechada ? "Sí" : "No", estaDisponible ? "Sí" : "No");
                System.out.println("  Horarios:");
                for (Time h : horarios) {
                    System.out.println("    " + h.toString());
                }
            }

            rsCanchas.close();
            psCanchas.close();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar canchas: " + e.getMessage(), e);
        }

        return listaCanchas;
    }


    public void desactivarCancha(Cancha canchaDesactivar){
        String consultaReservas = "SELECT COUNT(*) FROM Reserva WHERE idCancha = ? AND estaActiva = TRUE";
        String eliminarHorarios = "DELETE FROM CanchaHorario WHERE idCancha = ?";
        String eliminarCancha = "DELETE FROM Cancha WHERE id = ?";

        Connection conn = null;
        PreparedStatement psReservas = null;
        PreparedStatement psEliminarHorarios = null;
        PreparedStatement psEliminarCancha = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false); // Para asegurar atomicidad

            // Verificar reservas activas
            psReservas = conn.prepareStatement(consultaReservas);
            psReservas.setInt(1, canchaDesactivar.getId());
            rs = psReservas.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("ERROR: No se puede eliminar la cancha. Tiene reservas activas.");
                conn.rollback();
                return;
            }
            // Eliminar horarios asociados
            psEliminarHorarios = conn.prepareStatement(eliminarHorarios);
            psEliminarHorarios.setInt(1, canchaDesactivar.getId());
            psEliminarHorarios.executeUpdate();

            // Eliminar cancha
            psEliminarCancha = conn.prepareStatement(eliminarCancha);
            psEliminarCancha.setInt(1, canchaDesactivar.getId());
            psEliminarCancha.executeUpdate();

            conn.commit();
            System.out.println("Cancha y sus horarios eliminados correctamente (ID: " + canchaDesactivar.getId() + ")");
        } catch(SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
                }
            }
            throw new RuntimeException("Error al eliminar la cancha: " + e.getMessage(), e);
        }finally {
            try {
                if (rs != null) rs.close();
                if (psReservas != null) psReservas.close();
                if (psEliminarHorarios != null) psEliminarHorarios.close();
                if (psEliminarCancha != null) psEliminarCancha.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    public Vector<Cancha> busquedaAvanzada(Double minPrecio, Double maxPrecio,
                                           Boolean esTechada, Boolean disponible,
                                           Date fecha, Time hora) {
        Vector<Cancha> listaCanchas = new Vector<>();
        StringBuilder consulta = new StringBuilder(
                "SELECT c.* FROM Cancha c " +
                        "JOIN CanchaHorario ch ON c.id = ch.idCancha " +
                        "WHERE 1=1 "
        );


        if (minPrecio != null) consulta.append("AND c.precio >= ? ");
        if (maxPrecio != null) consulta.append("AND c.precio <= ? ");
        if (esTechada != null) consulta.append("AND c.esTechada = ? ");
        if (disponible != null) consulta.append("AND c.estaDisponible = ? ");
        if (hora != null) consulta.append("AND ch.horario = ? ");


        try (Connection conn = DatabaseConnection.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta.toString())) {

            int index = 1;
            if (minPrecio != null) ps.setDouble(index++, minPrecio);
            if (maxPrecio != null) ps.setDouble(index++, maxPrecio);
            if (esTechada != null) ps.setBoolean(index++, esTechada);
            if (disponible != null) ps.setBoolean(index++, disponible);
            if (hora != null) ps.setTime(index++, hora);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                double precio = rs.getDouble("precio");
                boolean techada = rs.getBoolean("esTechada");
                boolean estaDisponible = rs.getBoolean("estaDisponible");

                // Traer horarios de esa cancha
                PreparedStatement psHorarios = conn.prepareStatement(
                        "SELECT horario FROM CanchaHorario WHERE idCancha = ?");
                psHorarios.setInt(1, id);
                ResultSet rsHorarios = psHorarios.executeQuery();
                Vector<Time> horarios = new Vector<>();
                while (rsHorarios.next()) {
                    horarios.add(rsHorarios.getTime("horario"));
                }
                rsHorarios.close();
                psHorarios.close();

                CanchaHorario canchaHorario = new CanchaHorario(id, horarios);
                Cancha cancha = new Cancha(id, techada, precio, estaDisponible, canchaHorario);
                listaCanchas.add(cancha);
            }

            if (listaCanchas.isEmpty()) {
                System.out.println("No se encontraron canchas con esos criterios.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en búsqueda avanzada: " + e.getMessage(), e);
        }

        return listaCanchas;
    }

    public void bloquearCanchaPorMantenimiento(Cancha canchaBloquear){
        String consulta = "UPDATE Cancha SET estaDisponible = ? WHERE id = ?";
        PreparedStatement ps = null;

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            ps = conn.prepareStatement(consulta);
            ps.setBoolean(1, false);
            ps.setInt(2, canchaBloquear.getId());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cancha bloqueada por mantenimiento (ID: " + canchaBloquear.getId() + ")");
            } else {
                System.out.println("No se encontró ninguna cancha con ese ID.");
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
        String consulta = "UPDATE Cancha SET estaDisponible = ? WHERE id = ?";
        PreparedStatement ps = null;

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            ps = conn.prepareStatement(consulta);
            ps.setBoolean(1, true);
            ps.setInt(2, canchaDesbloquear.getId());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cancha desbloqueada (ID: " + canchaDesbloquear.getId() + ")");
            } else {
                System.out.println("No se encontró ninguna cancha con ese ID.");
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
