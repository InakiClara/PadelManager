package models;

import java.util.Vector;
import models.CanchaHorario;

public class Cancha {
    private int id;
    private boolean esTechada;
    private double precio;
    private boolean estaDispoonible;
    private CanchaHorario horario;


    public Cancha(int id, boolean esT, double p, boolean esD, CanchaHorario h) {
        this.id = id;
        this.esTechada = esT;
        this.precio = p;
        this.estaDispoonible =  esD;
        this.horario = h;
    }

    public CanchaHorario getHorario() {
        return horario;
    }

    public void setHorario(CanchaHorario horario) {
        this.horario = horario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEsTechada() {
        return esTechada;
    }

    public void setEsTechada(boolean esTechada) {
        this.esTechada = esTechada;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isEstaDispoonible() {
        return estaDispoonible;
    }

    public void setEstaDispoonible(boolean estaDispoonible) {
        this.estaDispoonible = estaDispoonible;
    }
}
