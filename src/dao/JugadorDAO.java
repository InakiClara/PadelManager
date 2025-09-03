package dao;

import database.DatabaseConnection;
import models.Jugador;

import java.sql.PreparedStatement;

public class JugadorDAO {
    public void altaJugador(Jugador nuevoJugador) {
        String consultaUsuario = "INSERT INTO Usuario (cedula, nombre, apellido, correo, contraseniaCuenta, telefono) VALUES (?, ?, ?, ?, ?, ?)";
        String consultaJugador = "INSERT INTO Jugador (cedula, fechaNacimiento, categoria, genero, incumplePago, estaBaneado) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
                PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaJugador);
        ) {

            psUsuario.setString(1, nuevoJugador.getCedula());
            psUsuario.setString(2, nuevoJugador.getNombre());
            psUsuario.setString(3, nuevoJugador.getApellido());
            psUsuario.setString(4, nuevoJugador.getCorreo());
            psUsuario.setString(5, nuevoJugador.getContraseniaCuenta());
            psUsuario.setString(6, nuevoJugador.getTelefono());
            psUsuario.executeUpdate();


            psJugador.setString(1, nuevoJugador.getCedula());
            psJugador.setDate(2, nuevoJugador.getFechaNacimiento());
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
        String consultaJugador = "UPDATE Jugador SET categoria = ?, genero = ?, incumplePago = ?, estaBaneado = ? WHERE cedula = ?";
        String consultaUsuario = "UPDATE Usuario SET nombre = ?, apellido = ?, correo = ?, contraseniaCuenta = ?, telefono = ? WHERE cedula = ?";
        try (
                PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaJugador);
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
        ) {
            psJugador.setString(1, jugador.getCategoria());
            psJugador.setString(2, jugador.getGenero());
            psJugador.setBoolean(3, jugador.isIncumplePago());
            psJugador.setBoolean(4, jugador.isEstaBaneado());
            psJugador.setString(5, jugador.getCedula());
            psJugador.executeUpdate();


            psUsuario.setString(1, jugador.getNombre());
            psUsuario.setString(2, jugador.getApellido());
            psUsuario.setString(3, jugador.getCorreo());
            psUsuario.setString(4, jugador.getContraseniaCuenta());
            psUsuario.setString(5, jugador.getTelefono());
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
}
