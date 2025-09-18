package models;

import java.util.Vector;
import models.CanchaHorario;

public class Cancha {
    private int numeroCancha;
    private boolean esTechada;
    private double precio;
    private boolean estaDisponible;
    private CanchaHorario horario;

    public Cancha(int numeroCancha, boolean esT, double p, boolean esD, CanchaHorario h) {
        this.esTechada = esT;
        this.precio = p;
        this.estaDisponible = esD;
        this.numeroCancha = numeroCancha;
        this.horario = h;
    }

    public int getNumero() {
        return numeroCancha;
    }

    public void setNumeroCancha(int numeroCancha) {
        this.numeroCancha = numeroCancha;
    }

    public CanchaHorario getHorario() {
        return horario;
    }

    public void setHorario(CanchaHorario horario) {
        this.horario = horario;
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

    public boolean isEstaDisponible() {
        return estaDisponible;
    }

    public void setEstaDisponible(boolean estaDisponible) {
        this.estaDisponible = estaDisponible;
    }
}
