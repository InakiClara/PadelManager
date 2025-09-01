
//import flujos.Menu;
import dao.ReservaDAO;
import java.sql.SQLException;

import database.DatabaseConnection;

import models.Reserva;

import java.util.Vector;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.getInstancia().getConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        // Crear DAO y listar reservas
        ReservaDAO dao = new ReservaDAO();
        Vector<Reserva> reservas = dao.listarReservasPorUsuario("12345678");

        // Mostrar reservas
        for (Reserva r : reservas) {
            System.out.println("Reserva ID: " + r.getId() + ", Fecha: " + r.getFecha() +
                    ", Inicio: " + r.getHorarioInicio() +
                    ", Final: " + r.getHorarioFinal());
        }
    }
}