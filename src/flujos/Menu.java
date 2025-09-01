package flujos;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import dao.ReservaDAO;
import dao.UsuarioDAO;
import models.Reserva;
import models.Usuario;
public class Menu {
/*
    private final Scanner scanner = new Scanner(System.in);
    private final UsuarioDAO usuarioDAO;
    private final ReservaDAO reservaDAO;

    public Menu() {
        this.usuarioDAO = new UsuarioDAO();
        this.reservaDAO = new ReservaDAO();
    }


    public void mostrarMenu() {
        int opcion = 0;

        do {
            try {
                System.out.println("=============================");
                System.out.println("Gestion de reserva");
                System.out.println("-----------------------------");
                System.out.println("1. Crear Reserva");
                System.out.println("2. Listar Usuario");
                System.out.println("3. Modificar Usuario");
                System.out.println("4. Eliminar Usuario");
                System.out.println("13. Salir");
                System.out.println("============================");
                System.out.print("Opcion: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        crearUsuario();
                        break;
                    case 2:
                        listarUsuarios();
                        break;
                    case 3:
                        modificarUsuario();
                        break;
                    case 4:
                        eliminarUsuario();
                        break;
                    case 5:
                        listarPalabra();
                        break;
                    case 6:
                        asignarAnimal();
                        break;
                    case 7:
                        eliminarAnimal();
                        break;
                    case 8:
                        listarAnimal();
                        break;
                    case 9:
                        cargarDatosDePrueba();
                        break;
                    case 10:
                        exportarUsuarios();
                        break;
                    case 11:
                        exportarAnimales();
                        break;
                    case 12:
                        exportarAnimalesDueno();
                        break;
                    case 13:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opcion incorrecta");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        } while (opcion != 13);
    }*/
}
