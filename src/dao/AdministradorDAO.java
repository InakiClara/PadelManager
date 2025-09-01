package dao;
import database.DatabaseConnection;
import models.Administrador;

import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Vector;

public class AdministradorDAO {

        public void crearAdministrador(Administrador nuevoAdministrador) {
            String consulta = "INSERT INTO Administrador (cedula, nombre, apellido, correo, contraseniaCuenta) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
                ps.setString(1, nuevoAdministrador.getCedula());
                ps.setString(2, nuevoAdministrador.getNombre());
                ps.setString(3, nuevoAdministrador.getApellido());
                ps.setString(4, nuevoAdministrador.getCorreo());
                ps.setString(5, nuevoAdministrador.getContraseniaCuenta());
                ps.executeUpdate();
                System.out.println("Administrador creado correctamente");
            } catch (Exception e) {
                throw new RuntimeException("Error al crear administrador", e);
            }
        }

        public void modificarAdministrador(Administrador nuevoAdministrador) {
            String consulta = "UPDATE Administrador SET nombre = ?, apellido = ?, correo = ?, contraseniaCuenta = ? WHERE cedula = ?";
            try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
                ps.setString(1, nuevoAdministrador.getNombre());
                ps.setString(2, nuevoAdministrador.getApellido());
                ps.setString(3, nuevoAdministrador.getCorreo());
                ps.setString(4, nuevoAdministrador.getContraseniaCuenta());
                ps.setString(5, nuevoAdministrador.getCedula());
                ps.executeUpdate();
                System.out.println("Administrador modificado correctamente");
            } catch (Exception e) {
                throw new RuntimeException("Error al modificar administrador", e);
            }
        }

        public void eliminarAdministrador(Administrador nuevoAdministrador) {
            String consulta = "DELETE FROM Administrador WHERE cedula = ?";
            try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
                ps.setString(1, nuevoAdministrador.getCedula());
                ps.executeUpdate();
                System.out.println("Administrador eliminado correctamente");
            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar administrador", e);
            }
        }


        public Vector<Administrador> listarAdministradores() {
            Vector<Administrador> administradores = new Vector<>();

            String consulta = "SELECT * FROM Administrador";

            try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
                ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                String contraseniaCuenta = rs.getString("contraseniaCuenta");

                Administrador admin = new Administrador(cedula, nombre, apellido, correo, contraseniaCuenta);
                administradores.add(admin);
                }

            } catch (SQLException e) {
                throw new RuntimeException("Error al listar administradores", e);
            }

        return administradores;
    }


}
