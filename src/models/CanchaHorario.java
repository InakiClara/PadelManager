package models;

import java.sql.Time;
import java.util.Vector;

public class CanchaHorario {
    private Vector<Time> horarios;
    private int idCancha;
    public CanchaHorario(int id, Vector<Time> h){
        this.horarios = h;
        this.idCancha = id;
    }

    public Vector<Time> getHorarios() {
        return horarios;
    }

    public void setHorarios(Vector<Time> horarios) {
        this.horarios = horarios;
    }

    public int getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(int idCancha) {
        this.idCancha = idCancha;
    }


}
