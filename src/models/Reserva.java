package models;

import java.sql.Time;
import java.util.Date;
import java.util.Vector;


public class Reserva {

    private int id;
    private String cedulaUsuario;
    private int idCancha;
    private Date fecha;
    private Time horarioInicio;
    private Time horarioFinal;
    private Time horaCancelacion;
    private MetodoPago metodoPago;
    private boolean estaPagada;
    private boolean estaActiva;
    Vector <models.Usuario> usuarios;
    Vector <models.Cancha> canchas;

    public Reserva(String cedulaUsuario, int idCancha, Date fecha, Time horarioInicio, Time horarioFinal, Time horaCancelacion, MetodoPago metodoPago, boolean estaPagada, boolean estaActiva){
        this.cedulaUsuario = cedulaUsuario;
        this.idCancha = idCancha;
        this.fecha = fecha;
        this.horarioInicio = horarioInicio;
        this.horarioFinal = horarioFinal;
        this.horaCancelacion = horaCancelacion;
        this.metodoPago = metodoPago;
        this.estaPagada = estaPagada;
        this.estaActiva = estaActiva;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public int getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(int idCancha) {
        this.idCancha = idCancha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Time getHorarioInicio() {
        return horarioInicio;
    }
    public void setHorarioInicio(Time horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public Time getHorarioFinal() {
        return horarioFinal;
    }

    public void setHorarioFinal(Time horarioFinal) {
        this.horarioFinal = horarioFinal;
    }

    public Time getHoraCancelacion() {
        return horaCancelacion;
    }

    public void setHoraCancelacion(Time horaCancelacion) {
        this.horaCancelacion = horaCancelacion;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public boolean isEstaPagada() {
        return estaPagada;
    }

    public void setEstaPagada(boolean estaPagada) {
        this.estaPagada = estaPagada;
    }

    public boolean isEstaActiva() {
        return estaActiva;
    }

    public void setEstaActiva(boolean estaActiva) {
        this.estaActiva = estaActiva;
    }

    public Vector<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Vector<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Vector<Cancha> getCanchas() {
        return canchas;
    }

    public void setCanchas(Vector<Cancha> canchas) {
        this.canchas = canchas;
    }

    @Override
    public String toString() {
        return "ID Reserva: " + id +
                ", Cédula Usuario: " + cedulaUsuario +
                ", ID Cancha: " + idCancha +
                ", Fecha: " + fecha +
                ", Horario Inicio: " + horarioInicio +
                ", Horario Final: " + horarioFinal +
                ", Hora Cancelación: " + (horaCancelacion != null ? horaCancelacion : "No cancelada") +
                ", Método de Pago: " + metodoPago +
                ", Pagada: " + (estaPagada ? "Sí" : "No") +
                ", Activa: " + (estaActiva ? "Sí" : "No");
    }


}

