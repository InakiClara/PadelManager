package models;

import java.time.LocalDate;

public class Jugador extends Usuario {
    private LocalDate fechaNacimiento;
    private String categoria;
    private String genero;
    private boolean incumplePago;
    private boolean estaBaneado;

    public Jugador(String cedula, String nombre, String apellido, String correo, String contraseniaCuenta, LocalDate fechaNacimiento, String categoria, String genero, boolean incumplePago, boolean estaBaneado) {
        super(cedula, nombre, apellido, correo, contraseniaCuenta);
        this.fechaNacimiento = fechaNacimiento;
        this.categoria = categoria;
        this.genero = genero;
        this.incumplePago = incumplePago;
        this.estaBaneado = estaBaneado;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isIncumplePago() {
        return incumplePago;
    }

    public void setIncumplePago(boolean incumplePago) {
        this.incumplePago = incumplePago;
    }

    public boolean isEstaBaneado() {
        return estaBaneado;
    }

    public void setEstaBaneado(boolean estaBaneado) {
        this.estaBaneado = estaBaneado;
    }
}
