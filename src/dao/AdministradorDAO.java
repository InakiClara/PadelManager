package dao;
import database.DatabaseConnection;
import models.Administrador;
import models.Usuario;

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
        if (!validarContrasenia(nuevoAdministrador.getContraseniaCuenta())) {
            System.out.println("La contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return;
        }

        String consultaUsuario = "INSERT INTO Usuario (cedula, nombre, apellido, correo, telefono, contraseniaCuenta) VALUES (?, ?, ?, ?, ?, ?)";
        String consultaAdmin = "INSERT INTO Administrador (cedula) VALUES (?)";

        try (
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
                PreparedStatement psAdmin = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaAdmin)
        ) {
            psUsuario.setString(1, nuevoAdministrador.getCedula());
            psUsuario.setString(2, nuevoAdministrador.getNombre());
            psUsuario.setString(3, nuevoAdministrador.getApellido());
            psUsuario.setString(4, nuevoAdministrador.getCorreo());
            psUsuario.setString(5, nuevoAdministrador.getTelefono());
            psUsuario.setString(6, nuevoAdministrador.getContraseniaCuenta());
            psUsuario.executeUpdate();

            psAdmin.setString(1, nuevoAdministrador.getCedula());
            psAdmin.executeUpdate();

            System.out.println("Administrador creado correctamente");

        } catch (Exception e) {
            throw new RuntimeException("Error al crear administrador", e);
        }
    }

    public void modificarAdministrador(Administrador nuevoAdministrador) {
        if (!validarContrasenia(nuevoAdministrador.getContraseniaCuenta())) {
            System.out.println("La contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return;
        }

        String consulta = "UPDATE Usuario SET nombre = ?, apellido = ?, correo = ?, telefono = ?, contraseniaCuenta = ? WHERE cedula = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, nuevoAdministrador.getNombre());
            ps.setString(2, nuevoAdministrador.getApellido());
            ps.setString(3, nuevoAdministrador.getCorreo());
            ps.setString(4, nuevoAdministrador.getTelefono());
            ps.setString(5, nuevoAdministrador.getContraseniaCuenta());
            ps.setString(6, nuevoAdministrador.getCedula());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Administrador modificado correctamente");
            } else {
                System.out.println("No se encontró un administrador con esa cédula");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar administrador", e);
        }
    }


    public void eliminarAdministrador(Administrador administrador) {
        String consultaAdministrador = "DELETE FROM Administrador WHERE cedula = ?";
        String consultaUsuario = "DELETE FROM Usuario WHERE cedula = ?";

        try (
                PreparedStatement psAdministrador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaAdministrador);
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario)
        ) {
            psAdministrador.setString(1, administrador.getCedula());
            psAdministrador.executeUpdate();

            psUsuario.setString(1, administrador.getCedula());
            psUsuario.executeUpdate();

            System.out.println("Administrador eliminado correctamente");

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar administrador", e);
        }
    }

    public Vector<Administrador> listarAdministradores() {
        Vector<Administrador> administradores = new Vector<>();

        String consulta = "SELECT u.cedula, u.nombre, u.apellido, u.correo, u.telefono, u.contraseniaCuenta FROM Usuario u JOIN Administrador a ON u.cedula = a.cedula";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                String telefono = rs.getString("telefono");
                String contraseniaCuenta = rs.getString("contraseniaCuenta");

                Administrador admin = new Administrador(cedula, nombre, apellido, correo, telefono, contraseniaCuenta);
                administradores.add(admin);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar administradores", e);
        }

        return administradores;
    }

    public boolean inicioSesion(String cedula, String contrasenia) {
        String consulta = "SELECT * FROM Usuario u INNER JOIN Administrador a ON u.cedula = a.cedula WHERE u.cedula = ? AND u.contraseniaCuenta = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedula);
            ps.setString(2, contrasenia);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    System.out.println("Inicio de sesión exitoso");
                    return true;
                } else {
                    System.out.println("Cédula o contraseña incorrecta");
                    return false;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar sesión", e);
        }
    }

    public boolean cambiarContrasenia(String cedula, String contraseniaActual, String nuevaContrasenia) {

        if (!validarContrasenia(nuevaContrasenia)) {
            System.out.println("La nueva contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return false;
        }

        String consultaVerificacion = "SELECT * FROM Usuario WHERE cedula = ? AND contraseniaCuenta = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaVerificacion)) {

            ps.setString(1, cedula);
            ps.setString(2, contraseniaActual);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("La contraseña actual es incorrecta.");
                    return false;
                }
            }

            String consultaActualizar = "UPDATE Usuario SET contraseniaCuenta = ? WHERE cedula = ?";
            try (PreparedStatement psUpdate = DatabaseConnection.getInstancia()
                    .getConnection()
                    .prepareStatement(consultaActualizar)) {

                psUpdate.setString(1, nuevaContrasenia);
                psUpdate.setString(2, cedula);

                int filas = psUpdate.executeUpdate();
                if (filas > 0) {
                    System.out.println("Contraseña actualizada correctamente.");
                    return true;
                } else {
                    System.out.println("No se pudo actualizar la contraseña.");
                    return false;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar contraseña", e);
        }
    }

    private boolean validarContrasenia(String contrasenia) {
        return contrasenia.length() >= 8 && contrasenia.matches(".*[A-Z].*");
    }

    public Vector<Usuario> listarUsuarios(String criterio) {
        Vector<Usuario> usuarios = new Vector<>();
        String consulta = "SELECT cedula, nombre, apellido, telefono, correo FROM Usuario WHERE cedula LIKE ? OR nombre LIKE ? OR apellido LIKE ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {

            String busqueda = "%" + criterio + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            ps.setString(3, busqueda);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setCedula(rs.getString("cedula"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setCorreo(rs.getString("correo"));

                    usuarios.add(usuario);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }

        return usuarios;
    }

    public void desactivarUsuarioPorCedula(String cedula) {
        String eliminarJugador = "DELETE FROM Jugador WHERE cedula = ?";
        String eliminarUsuario = "DELETE FROM Usuario WHERE cedula = ?";

        try (PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(eliminarJugador);
             PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(eliminarUsuario)) {

            // Primero eliminamos al jugador
            psJugador.setString(1, cedula);
            psJugador.executeUpdate();

            // Luego eliminamos al usuario
            psUsuario.setString(1, cedula);
            psUsuario.executeUpdate();

             System.out.println("Usuario y jugador eliminados correctamente.");


        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar usuario y jugador", e);
        }
    }

}
