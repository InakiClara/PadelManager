package flujos;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import dao.AdministradorDAO;
import dao.ReservaDAO;
import dao.UsuarioDAO;
import models.Reserva;
import models.Usuario;
import models.Administrador;
public class Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private final UsuarioDAO usuarioDAO;
    private final ReservaDAO reservaDAO;
    private final AdministradorDAO administradorDAO;

    public Menu() {
        this.usuarioDAO = new UsuarioDAO();
        this.reservaDAO = new ReservaDAO();
        this.administradorDAO = new AdministradorDAO();
    }


    public void mostrarMenu() {
        int opcion = 0;

        do {
            try {
                System.out.println("=============================");
                System.out.println("Menu de Padel Manager");
                System.out.println("-----------------------------");
                System.out.println("1. Crear Reserva");
                System.out.println("2. Cancelar Reserva");
                System.out.println("3. Modificar Reserva");
                System.out.println("4. Listar Reservas por Usuario");
                System.out.println("5. Listar Reservas por cancha");
                System.out.println("6. Crear Administrador");
                System.out.println("7. Eliminar Administrador");
                System.out.println("8. Modificar Administrador");
                System.out.println("9. Listar Administradores");
                System.out.println("10. Inicio seccion");
                System.out.println("11. Cambiar contraseña");
                System.out.println("12. Listar usuarios");
                System.out.println("13. Eliminar usuarios/jugadores");
                System.out.println("14. Salir");
                System.out.println("============================");
                System.out.print("Opcion: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        crearReserva();
                        break;
                    case 2:
                        cancelarReserva();
                        break;
                    case 3:
                        modificarReserva();
                        break;
                    case 4:
                        listarReservasPorUsuario();
                        break;
                    case 5:
                        listarReservaPorCancha();
                        break;
                    case 6:
                        crearAdministrador();
                        break;
                    case 7:
                        eliminarAdministrador();
                        break;
                    case 8:
                        modificarAdministrador();
                        break;
                    case 9:
                        listarAdministradores();
                        break;
                    case 10:
                        iniciarSesion();
                        break;
                    case 11:
                        cambiarContrasenia();
                        break;
                    case 12:
                        listarUsuarios();
                        break;
                    case 13:
                        eliminarUsuario();
                    case 14:
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
    }

    private void listarAdministradores() {
        Vector<Administrador> administradores = administradorDAO.listarAdministradores();

        if (administradores.isEmpty()) {
            System.out.println("Listado no encontrado");
        } else {
            for (Administrador a : administradores) {
                System.out.println(a);
            }
        }
    }

    private void modificarAdministrador() {
        listarAdministradores();

        System.out.print("Cedula del Administrador: ");
        String cedula = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Correo: ");
        String correo = scanner.nextLine();

        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();

        System.out.print("Contraseña: ");
        String contrasenia = scanner.nextLine();

        Administrador admin = new Administrador(cedula, nombre, apellido, correo, telefono, contrasenia);

        administradorDAO.modificarAdministrador(admin);
    }


    private void crearAdministrador() {

        System.out.print("Cédula: ");
        String cedula = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Correo: ");
        String correo = scanner.nextLine();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        System.out.print("Contraseña: ");
        String contrasenia = scanner.nextLine();

        Administrador admin = new Administrador(cedula, nombre, apellido, correo, telefono, contrasenia);

        administradorDAO.crearAdministrador(admin);

    }

    private void eliminarAdministrador() {
        listarAdministradores();

        System.out.print("Cédula del Administrador a eliminar: ");
        String cedula = scanner.nextLine();

        Administrador admin = new Administrador();
        admin.setCedula(cedula);

        administradorDAO.eliminarAdministrador(admin);

    }

    private void crearReserva() {
        try {
            System.out.print("Cédula del jugador: ");
            String cedulaUsuario = scanner.nextLine();

            System.out.print("ID de la cancha: ");
            int idCancha = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Fecha (dd/MM/yyyy): ");
            String fechaStr = scanner.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = sdf.parse(fechaStr);

            System.out.print("Horario inicio (HH:mm): ");
            String horaInicioStr = scanner.nextLine();
            Time horarioInicio = Time.valueOf(horaInicioStr + ":00");

            System.out.print("Horario fin (HH:mm): ");
            String horaFinStr = scanner.nextLine();
            Time horarioFin = Time.valueOf(horaFinStr + ":00");

            System.out.print("Método de pago: ");
            String metodoPago = scanner.nextLine();

            Reserva reserva = new Reserva(cedulaUsuario, idCancha, fecha, horarioInicio, horarioFin, null, metodoPago, false, true);
            reservaDAO.crearReserva(reserva);

        } catch (ParseException e) {
            System.out.println("Formato de fecha incorrecto. Use dd/MM/yyyy");
        }
    }


    private void cancelarReserva() {
        System.out.print("ID de la reserva: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        reservaDAO.cancelarReserva(id);
    }

    private void modificarReserva() {
        System.out.print("ID de la reserva: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nueva fecha (dd/MM/yyyy): ");
        String fechaStr = scanner.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = sdf.parse(fechaStr);
        } catch (ParseException e) {
            System.out.println("Formato de fecha incorrecto.");
            return;
        }

        System.out.print("Nuevo horario inicio (HH:mm): ");
        String horaInicioStr = scanner.nextLine();
        Time horarioInicio = Time.valueOf(horaInicioStr + ":00");

        System.out.print("Nuevo horario fin (HH:mm): ");
        String horaFinStr = scanner.nextLine();
        Time horarioFin = Time.valueOf(horaFinStr + ":00");

        Reserva reserva = new Reserva("", 0, fecha, horarioInicio, horarioFin, null, "", false, true);
        reservaDAO.actualizarReserva(reserva);

    }

    private void listarReservasPorUsuario() {
        System.out.print("Cédula del usuario: ");
        String cedulaUsuario = scanner.nextLine();

        Vector<Reserva> reservas = reservaDAO.listarReservasPorUsuario(cedulaUsuario);

        if (reservas.isEmpty()) {
            System.out.println("No se encontraron reservas para este usuario.");
        } else {
            for (Reserva r : reservas) {
                System.out.println(r);
            }
        }
    }

    private void listarReservaPorCancha() {
        System.out.print("ID de la cancha: ");
        int idCancha = scanner.nextInt();
        scanner.nextLine();

        Vector<Reserva> reservas = reservaDAO.listarReservasPorCancha(idCancha);

        if (reservas.isEmpty()) {
            System.out.println("No se encontraron reservas para esta cancha.");
        } else {
            for (Reserva r : reservas) {
                System.out.println(r);
            }
        }
    }

    private void iniciarSesion() {
        System.out.print("Ingrese cédula: ");
        String cedula = scanner.nextLine();

        System.out.print("Ingrese contraseña: ");
        String contrasenia = scanner.nextLine();

        administradorDAO.inicioSesion(cedula, contrasenia);
    }

    private void cambiarContrasenia() {
        System.out.print("Ingrese su cédula: ");
        String cedula = scanner.nextLine();

        System.out.print("Ingrese su contraseña actual: ");
        String actual = scanner.nextLine();

        System.out.print("Ingrese la nueva contraseña: ");
        String nueva = scanner.nextLine();

        administradorDAO.cambiarContrasenia(cedula, actual, nueva);
    }

    private void listarUsuarios() {
        System.out.print("Ingrese criterio de búsqueda (cédula, nombre o apellido): ");
        String criterio = scanner.nextLine();

        Vector<Usuario> usuarios = administradorDAO.listarUsuarios(criterio);

        if (usuarios.isEmpty()) {
            System.out.println("No se encontraron usuarios que coincidan con el criterio.");
        } else {
            System.out.println("\n Usuarios encontrados: ");
            for (Usuario u : usuarios) {
                System.out.println("Cédula: " + u.getCedula());
                System.out.println("Nombre: " + u.getNombre());
                System.out.println("Apellido: " + u.getApellido());
                System.out.println("Teléfono: " + u.getTelefono());
                System.out.println("Correo: " + u.getCorreo());
                System.out.println("------------------------");
            }
        }
    }

    private void eliminarUsuario() {
        System.out.print("Ingrese la cédula del usuario a eliminar: ");
        String cedula = scanner.nextLine();

        System.out.print("¿Está seguro de eliminar este usuario/jugador? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            administradorDAO.desactivarUsuarioPorCedula(cedula);
        } else {
            System.out.println("Operación cancelada.");
        }
    }



}
