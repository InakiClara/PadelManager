package models;
import java.util.ArrayList;

public class Usuario {
    private String cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private ArrayList<String> telefonos;
    private String contraseniaCuenta;

    public Usuario() {
    }
    public Usuario(String cedula, String nombre, String apellido, String correo, String contraseniaCuenta) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contraseniaCuenta = contraseniaCuenta;
        this.telefonos = new ArrayList<>();
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public ArrayList<String> getTelefonos() {
        return telefonos;
    }

    public void agregarTelefono(String telefono) {
        this.telefonos.add(telefono);
    }

    public void eliminarTelefono(String telefono) {
        this.telefonos.remove(telefono);
    }

    public String getContraseniaCuenta() {
        return contraseniaCuenta;
    }

    public void setContraseniaCuenta(String contraseniaCuenta) {
        this.contraseniaCuenta = contraseniaCuenta;
    }

}
