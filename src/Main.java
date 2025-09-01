
//import flujos.Menu;
import dao.ReservaDAO;
import java.sql.SQLException;

import database.DatabaseConnection;

import flujos.Menu;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.getInstancia().getConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        Menu menu = new Menu();
        menu.mostrarMenu();
    }
}