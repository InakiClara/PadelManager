package models;

public class Administrador extends Usuario {

    public Administrador() {
    }

    public Administrador(String cedula, String nombre, String apellido, String correo, String contraseniaCuenta) {
        super(nombre, apellido, correo, cedula, contraseniaCuenta);
    }
}
