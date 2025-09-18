package models;

import java.util.Vector;
import models.CanchaHorario;

public class Cancha {
    private int id;
    private boolean esTechada;
    private double precio;
    private boolean estaDisponible;
    private int numero;              // <-- nuevo atributo
    private CanchaHorario horario;

    public Cancha(int id, boolean esT, double p, boolean esD, int numero, CanchaHorario h) {
        this.id = id;
        this.esTechada = esT;
        this.precio = p;
        this.estaDisponible = esD;
        this.numero = numero;        // <-- inicializar número
        this.horario = h;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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

    public boolean isEstaDisponible() {
        return estaDispoonible;
    }

    public void setEstaDisponible(boolean estaDispoonible) {
        this.estaDispoonible = estaDispoonible;
    }
}
