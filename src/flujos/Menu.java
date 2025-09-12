package flujos;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import dao.*;
import models.Reserva;
import models.Usuario;
import models.Administrador;
import models.*;
public class Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private final UsuarioDAO usuarioDAO;
    private final ReservaDAO reservaDAO;
    private final AdministradorDAO administradorDAO;
    private final CanchaDAO canchaDAO;
    private final JugadorDAO jugadorDAO;

    public Menu() {
        this.usuarioDAO = new UsuarioDAO();
        this.reservaDAO = new ReservaDAO();
        this.administradorDAO = new AdministradorDAO();
        this.canchaDAO = new CanchaDAO();
        this.jugadorDAO = new JugadorDAO();
    }


    public void mostrarMenu() {
        int opcion = 0;

        do {
            try {
                System.out.println("=============================");
                System.out.println("Menu de Padel Manager");
                System.out.println("-----------------------------");

                // ---------- Reservas ----------
                System.out.println("-----------Reservas---------");
                System.out.println("1. Crear Reserva");
                System.out.println("2. Cancelar Reserva");
                System.out.println("3. Modificar Reserva");
                System.out.println("4. Listar Reservas por Usuario");
                System.out.println("5. Listar Reservas por Cancha");
                System.out.println("6. Listar Reservas por Fecha");
                System.out.println("7. Listar Reservas por Fecha y Jugador");

                // ---------- Administrador ----------
                System.out.println("-------Administrador--------");
                System.out.println("8. Crear Administrador");
                System.out.println("9. Eliminar Administrador");
                System.out.println("10. Modificar Administrador");
                System.out.println("11. Listar Administradores");

                // ---------- Usuarios ----------
                System.out.println("-----------Usuarios---------");
                System.out.println("12. Inicio de Sesión");
                System.out.println("13. Cambiar Contraseña");
                System.out.println("14. Listar Usuarios");
                System.out.println("15. Eliminar Usuarios/Jugadores");
                System.out.println("16. Cantidad de usuarios nuevos por mes");


                // ---------- Cancha ----------
                System.out.println("-----------Cancha---------");
                System.out.println("16. Crear Cancha");
                System.out.println("17. Listar Canchas");
                System.out.println("18. Desactivar Cancha");
                System.out.println("19. Actualizar Cancha");

                // ---------- Jugador ----------
                System.out.println("-----------Jugador---------");
                System.out.println("20. Crear Jugador");
                System.out.println("21. Modificar Jugador");

                // ---------- Salir ----------
                System.out.println("30. Salir");
                System.out.println("============================");
                System.out.print("Opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    // Reservas
                    case 1: crearReserva(); break;
                    case 2: cancelarReserva(); break;
                    case 3: modificarReserva(); break;
                    case 4: listarReservasPorUsuario(); break;
                    case 5: listarReservaPorCancha(); break;
                    case 6: listarReservasPorFecha(); break;
                    case 7: listarReservasPorFechaJugador(); break;

                    // Administrador
                    case 8: crearAdministrador(); break;
                    case 9: eliminarAdministrador(); break;
                    case 10: modificarAdministrador(); break;
                    case 11: listarAdministradores(); break;

                    // Usuarios
                    case 12: iniciarSesion(); break;
                    case 13: cambiarContrasenia(); break;
                    case 14: listarUsuarios(); break;
                    case 15: eliminarUsuario(); break;
                    case 16: mostrarCantidadUsuariosPorMes(); break;


                    // Cancha
                    case 17: crearCancha(); break;
                    case 18: canchaDAO.listarCancha(); break;
                    case 19: desactivarCancha(); break;
                    case 20: actualizarCancha(); break;

                    // Jugador
                    case 21: crearJugador(); break;
                    case 22: modificarJugador(); break;

                    // Salir
                    case 30: System.out.println("Saliendo..."); break;
                    default: System.out.println("Opción incorrecta"); break;
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        } while (opcion != 30);
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
            System.out.println("Usuarios disponibles:");
            listarUsuarios(); // Mostrar usuarios para no tener que memorizar cédulas

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

            long millisInicio = horarioInicio.getTime();
            long millisFin = millisInicio + (90 * 60 * 1000); // 90 minutos
            Time horarioFin = new Time(millisFin);

            System.out.print("Método de pago: ");
            String metodoPago = scanner.nextLine();

            Reserva reserva = new Reserva(cedulaUsuario, idCancha, fecha, horarioInicio, horarioFin, null, metodoPago, false, true);
            reservaDAO.crearReserva(reserva);

            System.out.println("Reserva creada: " + reserva);

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
        try {
            System.out.print("ID de la reserva: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Usuarios disponibles:");
            listarUsuarios();  // Mostrar todos los usuarios antes de pedir cédula

            System.out.print("Cédula del jugador: ");
            String cedulaUsuario = scanner.nextLine();

            System.out.print("Nueva fecha (dd/MM/yyyy): ");
            String fechaStr = scanner.nextLine();
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);

            System.out.print("Nuevo horario inicio (HH:mm): ");
            String horaInicioStr = scanner.nextLine();
            Time horarioInicio = Time.valueOf(horaInicioStr + ":00");

            long millisInicio = horarioInicio.getTime();
            long millisFin = millisInicio + (90 * 60 * 1000);
            Time horarioFin = new Time(millisFin);

            System.out.println("Canchas disponibles:");
            canchaDAO.listarCancha();  // Mostrar todas las canchas

            System.out.print("Ingrese nuevo ID de la cancha: ");
            int idCancha = scanner.nextInt();
            scanner.nextLine();

            Reserva reserva = new Reserva(cedulaUsuario, idCancha, fecha, horarioInicio, horarioFin, null, "", false, true);
            reserva.setId(id);

            reservaDAO.actualizarReserva(reserva);
            System.out.println("Reserva modificada correctamente: " + reserva);

        } catch (Exception e) {
            System.out.println("Error al modificar la reserva: " + e.getMessage());
        }
    }





    private void listarReservasPorUsuario() {
        // Mostrar todos los usuarios antes de pedir cédula
        System.out.println("Usuarios disponibles:");
        listarUsuarios();  // Llama a la función ya existente

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

        usuarioDAO.inicioSesion(cedula, contrasenia);
    }

    private void cambiarContrasenia() {
        System.out.print("Ingrese su cédula: ");
        String cedula = scanner.nextLine();

        System.out.print("Ingrese su contraseña actual: ");
        String actual = scanner.nextLine();

        System.out.print("Ingrese la nueva contraseña: ");
        String nueva = scanner.nextLine();

        usuarioDAO.cambiarContrasenia(cedula, actual, nueva);
    }

    private void listarUsuarios() {
        System.out.print("Ingrese criterio de búsqueda (cédula, nombre o apellido): ");
        String criterio = scanner.nextLine();

        Vector<Usuario> usuarios = usuarioDAO.listarUsuarios(criterio);

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
        System.out.println("Usuarios disponibles:");
        listarUsuarios(); // Mostrar usuarios antes de pedir cédula

        System.out.print("Ingrese la cédula del usuario a eliminar: ");
        String cedula = scanner.nextLine();

        System.out.print("¿Está seguro de eliminar este usuario/jugador? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            usuarioDAO.desactivarUsuarioPorCedula(cedula);
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void crearCancha() {
        System.out.println("Ingrese ID:");
        int id = scanner.nextInt();

        System.out.print("Precio por hora: ");
        double precio = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Es techada (true/false): ");
        boolean esTechada = scanner.nextBoolean();
        scanner.nextLine();

        System.out.print("Está disponible (true/false): ");
        boolean estaDisponible = scanner.nextBoolean();
        scanner.nextLine();

        System.out.print("Número de horarios disponibles: ");
        int numHorarios = scanner.nextInt();
        scanner.nextLine();

        Vector<Time> horarios = new Vector<>();
        for (int i = 0; i < numHorarios; i++) {
            System.out.print("Horario " + (i + 1) + " (HH:mm): ");
            String horaStr = scanner.nextLine();
            Time horario = Time.valueOf(horaStr + ":00");
            horarios.add(horario);
        }
        CanchaHorario canchaHorario = new CanchaHorario(id, horarios);

        Cancha nuevaCancha = new Cancha(id, esTechada, precio, estaDisponible, canchaHorario);

        canchaDAO.altaCancha(nuevaCancha);
    }
    private void desactivarCancha() {
        System.out.print("Ingrese el ID de la cancha a desactivar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("¿Está seguro de desactivar esta cancha? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S") || confirmacion.equalsIgnoreCase("s")) {
            Cancha canchaDesactivar = new Cancha(id, false, 0.0, false, null);
            canchaDesactivar.setId(id);
            canchaDAO.desactivarCancha(canchaDesactivar);
        } else {
            System.out.println("Operación cancelada.");
        }
    }
    private void actualizarCancha() {
        System.out.println("Canchas disponibles:");
        canchaDAO.listarCancha();

        System.out.print("Ingrese el ID de la cancha a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nuevo precio por hora: ");
        double precio = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("¿Es techada? (true/false): ");
        boolean esTechada = scanner.nextBoolean();
        scanner.nextLine();

        System.out.print("¿Está disponible? (true/false): ");
        boolean estaDisponible = scanner.nextBoolean();
        scanner.nextLine();

        System.out.print("Número de horarios disponibles: ");
        int numHorarios = scanner.nextInt();
        scanner.nextLine();

        Vector<Time> horarios = new Vector<>();
        for (int i = 0; i < numHorarios; i++) {
            System.out.print("Horario " + (i + 1) + " (HH:mm): ");
            String horaStr = scanner.nextLine();
            Time horario = Time.valueOf(horaStr + ":00");
            horarios.add(horario);
        }

        CanchaHorario canchaHorario = new CanchaHorario(id, horarios);
        Cancha canchaActualizar = new Cancha(id, esTechada, precio, estaDisponible, canchaHorario);
        canchaActualizar.setId(id);

        canchaDAO.actualizarCancha(canchaActualizar);
    }


    private void listarReservasPorFecha() {
        System.out.print("Ingrese la fecha para filtrar (formato: yyyy-MM-dd): ");
        String fechaString = scanner.nextLine();

        java.sql.Date fecha = java.sql.Date.valueOf(fechaString);

        Vector<Reserva> reservas = reservaDAO.listarReservasPorFecha(fecha);

        if (reservas.isEmpty()) {
            System.out.println("No se encontraron reservas para esta fecha.");
        } else {
            for (Reserva r : reservas) {
                System.out.println(r);
            }
        }
    }

    private void listarReservasPorFechaJugador() {
        System.out.print("Ingrese la fecha para filtrar (formato: yyyy-MM-dd): ");
        String fechaString = scanner.nextLine();
        java.sql.Date fecha = java.sql.Date.valueOf(fechaString);

        // Mostrar todos los usuarios antes de pedir cédula
        System.out.println("Usuarios disponibles:");
        listarUsuarios();

        System.out.print("Cedula del usuario: ");
        String cedula = scanner.nextLine();

        Vector<Reserva> reservas = reservaDAO.listarReservasPorFechaJugador(fecha, cedula);

        if (reservas.isEmpty()) {
            System.out.println("No se encontraron reservas para esta fecha y usuario.");
        } else {
            for (Reserva r : reservas) {
                System.out.println(r);
            }
        }
    }


    private void crearJugador() {
        try {
            System.out.print("Cedula: ");
            String cedula = scanner.nextLine();

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();

            System.out.print("Correo: ");
            String correo = scanner.nextLine();

            System.out.print("Contraseña: ");
            String contrasenia = scanner.nextLine();

            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();

            System.out.print("Fecha de nacimiento (dd/MM/yyyy): ");
            String fechaStr = scanner.nextLine();
            Date fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);

            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            System.out.print("Genero: ");
            String genero = scanner.nextLine();

            System.out.print("¿Incumple pago?: ");
            boolean incumplePago = Boolean.parseBoolean(scanner.nextLine());

            System.out.print("¿Está baneado?: ");
            boolean estaBaneado = Boolean.parseBoolean(scanner.nextLine());

            Jugador nuevoJugador = new Jugador(cedula, nombre, apellido, correo, telefono, contrasenia,
                    fechaNacimiento, categoria, genero, incumplePago, estaBaneado);

            jugadorDAO.altaJugador(nuevoJugador);
        } catch (Exception e) {
            System.out.println("Error al crear jugador: " + e.getMessage());
        }
    }

    private void modificarJugador() {
        System.out.println("Jugadores disponibles:");
        listarUsuarios(); // Listar usuarios antes de pedir cédula

        try {
            System.out.print("Cédula del jugador a modificar: ");
            String cedula = scanner.nextLine();

            System.out.print("Nuevo nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Nuevo apellido: ");
            String apellido = scanner.nextLine();

            System.out.print("Nuevo correo: ");
            String correo = scanner.nextLine();

            System.out.print("Nueva contraseña: ");
            String contrasenia = scanner.nextLine();

            System.out.print("Nuevo teléfono: ");
            String telefono = scanner.nextLine();

            System.out.print("Fecha de nacimiento (dd/MM/yyyy): ");
            String fechaStr = scanner.nextLine();
            Date fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);

            System.out.print("Nueva categoría: ");
            String categoria = scanner.nextLine();

            System.out.print("Nuevo genero: ");
            String genero = scanner.nextLine();

            System.out.print("¿Incumple pago? (true/false): ");
            boolean incumplePago = Boolean.parseBoolean(scanner.nextLine());

            System.out.print("¿Está baneado? (true/false): ");
            boolean estaBaneado = Boolean.parseBoolean(scanner.nextLine());

            Jugador jugador = new Jugador(cedula, nombre, apellido, correo, telefono, contrasenia,
                    fechaNacimiento, categoria, genero, incumplePago, estaBaneado);

            jugadorDAO.actualizarJugador(jugador);
            System.out.println("Jugador actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar jugador: " + e.getMessage());
        }
    }


    private void mostrarCantidadUsuariosPorMes() {
        try {
            System.out.print("Ingrese el mes (1-12): ");
            int mes = scanner.nextInt();
            System.out.print("Ingrese el año (yyyy): ");
            int anio = scanner.nextInt();
            scanner.nextLine();

            int total = usuarioDAO.contarUsuariosPorMes(mes, anio);
            System.out.println("Cantidad de usuarios ingresados en " + mes + "/" + anio + ": " + total);
        } catch (Exception e) {
            System.out.println("Error al obtener la cantidad: " + e.getMessage());
            scanner.nextLine();
        }
    }


}
