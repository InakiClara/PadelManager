package models;

import java.sql.Time;
import java.util.Vector;

public class CanchaHorario {
    private Vector<Time> horarios;
    private int numeroCancha;
    public CanchaHorario(int numeroCancha, Vector<Time> h){
        this.horarios = h;
        this.numeroCancha = numeroCancha;
    }

    public Vector<Time> getHorarios() {
        return horarios;
    }

    public void setHorarios(Vector<Time> horarios) {
        this.horarios = horarios;
    }

    public int getNumeroCancha() {
        return numeroCancha;
    }

    public void setNumeroCancha(int numeroCancha) {
        this.numeroCancha = numeroCancha;
    }


}
