package dao;

import database.DatabaseConnection;
import models.Jugador;

import java.sql.PreparedStatement;

public class JugadorDAO {
    public void altaJugador(Jugador nuevoJugador) {
        if (!validarContrasenia(nuevoJugador.getContraseniaCuenta())) {
            System.out.println("La contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return;
        }

        String consultaUsuario = "INSERT INTO Usuario (cedula, nombre, apellido, correo, telefono, contraseniaCuenta) VALUES (?, ?, ?, ?, ?, ?)";
        String consultaJugador = "INSERT INTO Jugador (cedula, fechaNacimiento, categoria, genero, incumplePago, estaBaneado) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
                PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaJugador);
        ) {

            psUsuario.setString(1, nuevoJugador.getCedula());
            psUsuario.setString(2, nuevoJugador.getNombre());
            psUsuario.setString(3, nuevoJugador.getApellido());
            psUsuario.setString(4, nuevoJugador.getCorreo());
            psUsuario.setString(5, nuevoJugador.getTelefono());
            psUsuario.setString(6, nuevoJugador.getContraseniaCuenta());
            psUsuario.executeUpdate();


            psJugador.setString(1, nuevoJugador.getCedula());
            psJugador.setDate(2, new java.sql.Date(nuevoJugador.getFechaNacimiento().getTime()));
            psJugador.setString(3, nuevoJugador.getCategoria());
            psJugador.setString(4, nuevoJugador.getGenero());
            psJugador.setBoolean(5, nuevoJugador.isIncumplePago());
            psJugador.setBoolean(6, nuevoJugador.isEstaBaneado());
            psJugador.executeUpdate();

            System.out.println("Jugador creado correctamente");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void actualizarJugador(Jugador jugador) {
        if (!validarContrasenia(jugador.getContraseniaCuenta())) {
            System.out.println("La contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return;
        }
        String consultaJugador = "UPDATE Jugador SET fechaNacimiento = ?, categoria = ?, genero = ?, incumplePago = ?, estaBaneado = ? WHERE cedula = ?";
        String consultaUsuario = "UPDATE Usuario SET nombre = ?, apellido = ?, correo = ?, telefono = ?, contraseniaCuenta = ? WHERE cedula = ?";
        try (
                PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaJugador);
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
        ) {
            psJugador.setDate(1, new java.sql.Date(jugador.getFechaNacimiento().getTime()));
            psJugador.setString(2, jugador.getCategoria());
            psJugador.setString(3, jugador.getGenero());
            psJugador.setBoolean(4, jugador.isIncumplePago());
            psJugador.setBoolean(5, jugador.isEstaBaneado());
            psJugador.setString(6, jugador.getCedula());
            psJugador.executeUpdate();

            psUsuario.setString(1, jugador.getNombre());
            psUsuario.setString(2, jugador.getApellido());
            psUsuario.setString(3, jugador.getCorreo());
            psUsuario.setString(4, jugador.getTelefono());
            psUsuario.setString(5, jugador.getContraseniaCuenta());
            psUsuario.setString(6, jugador.getCedula());
            psUsuario.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminarJugador(String cedula){
        String consultaJugador = "DELETE FROM Jugador WHERE cedula = ?";
        String consultaUsuario = "DELETE FROM Usuario WHERE cedula = ?";
        try (
                PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaJugador);
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
        ) {
            psJugador.setString(1, cedula);
            psJugador.executeUpdate();

            psUsuario.setString(1, cedula);
            psUsuario.executeUpdate();
            System.out.println("Jugador eliminado correctamente");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validarContrasenia(String contrasenia) {
        return contrasenia.length() >= 8 && contrasenia.matches(".*[A-Z].*");
    }

}
