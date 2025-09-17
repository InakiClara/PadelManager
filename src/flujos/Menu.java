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
                System.out.println("8. Consultar estado de pago de una reserva");
                System.out.println("9. Pagar reserva");

                // ---------- Administrador ----------
                System.out.println("-------Administrador--------");
                System.out.println("10. Crear Administrador");
                System.out.println("11. Eliminar Administrador");
                System.out.println("12. Modificar Administrador");
                System.out.println("13. Listar Administradores");

                // ---------- Usuarios ----------
                System.out.println("-----------Usuarios---------");
                System.out.println("14. Inicio de Sesión");
                System.out.println("15. Cambiar Contraseña");
                System.out.println("16. Listar Usuarios");
                System.out.println("17. Eliminar Usuarios/Jugadores");
                System.out.println("18. Cantidad de usuarios nuevos por mes");

                // ---------- Cancha ----------
                System.out.println("-----------Cancha---------");
                System.out.println("19. Crear Cancha");
                System.out.println("20. Listar Canchas");
                System.out.println("21. Eliminar Cancha");
                System.out.println("22. Actualizar Cancha");
                System.out.println("23. Bloquear Cancha por mantenimiento");
                System.out.println("24. Desbloquear Cancha");
                System.out.println("25. Busqueda avanzada de Cancha");


                // ---------- Jugador ----------
                System.out.println("-----------Jugador---------");
                System.out.println("26. Crear Jugador");
                System.out.println("27. Modificar Jugador");
                System.out.println("28. Eliminar Jugador");
                System.out.println("29. Listar Jugadores");
                System.out.println("30. Analizar baneos de jugadores");
                System.out.println("31. Listar baenados");
                System.out.println("32. Desbanear jugadores");


                // ---------- Estadísticas ----------
                System.out.println("--------Estadísticas--------");
                System.out.println("33. Total de reservas");
                System.out.println("34. Total de ingresos");


                // ---------- Salir ----------
                System.out.println("0. Salir");
                System.out.println("============================");
                System.out.println("Ingrese el numero de la opcion que desea");
                System.out.print("Opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    // Salir
                    case 0: System.out.println("Saliendo..."); break;

                    // Reservas
                    case 1: crearReserva(); break;
                    case 2: cancelarReserva(); break;
                    case 3: modificarReserva(); break;
                    case 4: listarReservasPorUsuario(); break;
                    case 5: listarReservaPorCancha(); break;
                    case 6: listarReservasPorFecha(); break;
                    case 7: listarReservasPorFechaJugador(); break;
                    case 8: consultarEstadoPago(); break;
                    case 9: pagarReserva(); break;

                    // Administrador
                    case 10: crearAdministrador(); break;
                    case 11: eliminarAdministrador(); break;
                    case 12: modificarAdministrador(); break;
                    case 13: listarAdministradores(); break;

                    // Usuarios
                    case 14: iniciarSesion(); break;
                    case 15: cambiarContrasenia(); break;
                    case 16: listarUsuarios(); break;
                    case 17: eliminarUsuario(); break;
                    case 18: mostrarCantidadUsuariosPorMes(); break;

                    // Cancha
                    case 19: crearCancha(); break;
                    case 20: canchaDAO.listarCancha(); break;
                    case 21: desactivarCancha(); break;
                    case 22: actualizarCancha(); break;
                    case 23: bloquearCanchaMantenimiento();break;
                    case 24: desbloquearCancha();break;
                    case 25: busquedaAvanzadaMenu();break;

                    // Jugador
                    case 26: crearJugador(); break;
                    case 27: modificarJugador(); break;
                    case 28: eliminarJugador(); break;
                    case 29: listarJugadores(); break;
                    case 30: jugadorDAO.analizarBaneos(); break;
                    case 31: listarJugadoresBaneados(); break;
                    case 32: desbanearJugador(); break;

                    // Estadísticas
                    case 33: reservaDAO.totalReservas(); break;
                    case 34: mostrarTotalIngresos(); break;

                    default: System.out.println("Opción incorrecta"); break;
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        } while (opcion != 0);
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

    private void desbanearJugador() {
        System.out.print("Lista de jugadores: ");
        listarJugadoresBaneados();

        System.out.print("Ingrese la cédula del jugador a desbanear: ");
        String cedula = scanner.nextLine();

        jugadorDAO.desbanearJugador(cedula);
    }

    private void listarJugadoresBaneados() {
        Vector<Jugador> jugadores = jugadorDAO.listarJugadoresBaneados();

        if (jugadores.isEmpty()) {
            System.out.println("Listado no encontrado");
        } else {
            for (Jugador j : jugadores) {
                System.out.println(j);
            }
        }
    }

    private void listarJugadores() {
        Vector<Jugador> jugadores = jugadorDAO.listarJugadores();

        if (jugadores.isEmpty()) {
            System.out.println("Listado no encontrado");
        } else {
            for (Jugador j : jugadores) {
                System.out.println(j);
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

    private void eliminarJugador() {
        listarUsuarios();

        System.out.print("Cédula del jugador a eliminar: ");
        String cedula = scanner.nextLine();

        jugadorDAO.eliminarJugador(cedula);

    }


    private void crearReserva() {
        try {
            listarTodosLosUsuarios(); // Mostrar usuarios para no tener que memorizar cédulas

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

            listarTodosLosUsuarios(); // Mostrar todos los usuarios antes de pedir cédula

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

            System.out.print("Ingrese nuevo metodo de pago: ");
            String metodoPago = scanner.nextLine();

            Reserva reserva = new Reserva(cedulaUsuario, idCancha, fecha, horarioInicio, horarioFin, null, metodoPago, false, true);
            reserva.setId(id);

            reservaDAO.actualizarReserva(reserva);
            System.out.println("Reserva modificada correctamente: " + reserva);

        } catch (Exception e) {
            System.out.println("Error al modificar la reserva: " + e.getMessage());
        }
    }





    private void listarReservasPorUsuario() {
        // Mostrar todos los usuarios antes de pedir cédula
        listarTodosLosUsuarios();  // Llama a la función ya existente

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

    private void listarTodosLosUsuarios() {
        Vector<Usuario> usuarios = usuarioDAO.listarTodosLosUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No se encontraron usuarios que coincidan con el criterio.");
        } else {
            System.out.println("\n Lista de usuarios: ");
            for (Usuario u : usuarios) {
                System.out.println("Cédula: " + u.getCedula());
                System.out.println("Nombre: " + u.getNombre());
                System.out.println("Apellido: " + u.getApellido());
                System.out.println("Teléfono: " + u.getTelefono());
                System.out.println("Correo: " + u.getCorreo());
                System.out.println("Fecha de ingreso: " + u.getFechaIngreso());
                System.out.println("------------------------");
            }
        }
    }

    private void eliminarUsuario() {
        listarTodosLosUsuarios(); // Mostrar usuarios antes de pedir cédula

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

        listarTodosLosUsuarios();

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

            int incumplePago = 0;

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
        listarTodosLosUsuarios(); // Listar usuarios antes de pedir cédula

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
            int incumplePago = scanner.nextInt();

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

    private void mostrarTotalIngresos() {
        try {
            System.out.print("Ingrese fecha inicio (yyyy-MM-dd): ");
            java.sql.Date fechaInicio = java.sql.Date.valueOf(scanner.nextLine());

            System.out.print("Ingrese fecha fin (yyyy-MM-dd): ");
            java.sql.Date fechaFin = java.sql.Date.valueOf(scanner.nextLine());

            reservaDAO.totalIngresos(fechaInicio, fechaFin);
        } catch (Exception e) {
            System.out.println("Error al obtener total de ingresos: " + e.getMessage());
        }
    }

    private void consultarEstadoPago() {
        System.out.print("Ingrese el ID de la reserva: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        reservaDAO.obtenerEstadoPago(id);
    }

    private void pagarReserva() {
        System.out.print("Ingrese el ID de la reserva a pagar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        reservaDAO.pagarReserva(id);
    }

    private void bloquearCanchaMantenimiento(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("BLOQUEAR CANCHA POR MANTENIMIENTO");

        Vector<Cancha> listaCanchas = canchaDAO.listarCancha();
        if (listaCanchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        System.out.println("Lista de canchas:");
        for (Cancha c : listaCanchas) {
            System.out.printf("ID: %d | Precio: %.2f | Techada: %s | Disponible: %s%n",
                    c.getId(), c.getPrecio(),
                    c.isEsTechada() ? "Sí" : "No",
                    c.isEstaDispoonible() ? "Sí" : "No");
        }


        System.out.print("Ingrese el ID de la cancha que desea bloquear: ");
        int idSeleccionado = scanner.nextInt();

        Cancha canchaSeleccionada = null;
        for (Cancha c : listaCanchas) {
            if (c.getId() == idSeleccionado) {
                canchaSeleccionada = c;
                break;
            }
        }

        if (canchaSeleccionada == null) {
            System.out.println("No se encontró una cancha con ese ID.");
            return;
        }

        if (!canchaSeleccionada.isEstaDispoonible()) {
            System.out.println("La cancha ya está bloqueada o no disponible.");
            return;
        }

        System.out.print("¿Está seguro que desea bloquear esta cancha por mantenimiento? (s/n): ");
        String confirmacion = scanner.next();

        if (confirmacion.equalsIgnoreCase("s")) {
            canchaDAO.bloquearCanchaPorMantenimiento(canchaSeleccionada);
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void desbloquearCancha(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("DESBLOQUEAR CANCHA");

        Vector<Cancha> listaCanchas = canchaDAO.listarCancha();
        if (listaCanchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        System.out.print("Ingrese el ID de la cancha que desea desbloquear: ");
        int idSeleccionado = scanner.nextInt();

        Cancha canchaSeleccionada = null;
        for (Cancha c : listaCanchas) {
            if (c.getId() == idSeleccionado) {
                canchaSeleccionada = c;
                break;
            }
        }

        if (canchaSeleccionada == null) {
            System.out.println("No se encontró una cancha con ese ID.");
            return;
        }

        if (canchaSeleccionada.isEstaDispoonible()) {
            System.out.println("La cancha ya está disponible.");
            return;
        }

        System.out.print("¿Está seguro que desea desbloquear esta cancha? (s/n): ");
        String confirmacion = scanner.next();

        if (confirmacion.equalsIgnoreCase("s")) {
            canchaDAO.desbloquearCancha(canchaSeleccionada);
        } else {
            System.out.println("Operación cancelada.");
        }
    }
    private void busquedaAvanzadaMenu() {
        try {
            System.out.println("===== BÚSQUEDA AVANZADA DE CANCHAS =====");
            System.out.print("Ingrese precio mínimo (o Enter para omitir): ");
            String minPrecioInput = scanner.nextLine();
            Double minPrecio = minPrecioInput.isEmpty() ? null : Double.parseDouble(minPrecioInput);
            System.out.print("Ingrese precio máximo (o Enter para omitir): ");
            String maxPrecioInput = scanner.nextLine();
            Double maxPrecio = maxPrecioInput.isEmpty() ? null : Double.parseDouble(maxPrecioInput);
            System.out.print("¿Quiere que sea techada? (s/n o Enter para omitir): ");
            String techadaInput = scanner.nextLine();
            Boolean esTechada = techadaInput.isEmpty() ? null : techadaInput.equalsIgnoreCase("s");
            System.out.print("¿Debe estar disponible? (s/n o Enter para omitir): ");
            String disponibleInput = scanner.nextLine();
            Boolean disponible = disponibleInput.isEmpty() ? null : disponibleInput.equalsIgnoreCase("s");
            System.out.print("Ingrese hora exacta (HH:MM:SS o Enter para omitir): ");
            String horaInput = scanner.nextLine();
            Time hora = horaInput.isEmpty() ? null : Time.valueOf(horaInput);

            Date fecha = null;
            Vector<Cancha> resultados = canchaDAO.busquedaAvanzada(minPrecio, maxPrecio, esTechada, disponible, (java.sql.Date) fecha, hora);
            if (resultados.isEmpty()) {
                System.out.println("No se encontraron canchas con los criterios ingresados.");
            } else {
                System.out.println("===== RESULTADOS =====");
                for (Cancha c : resultados) {
                    System.out.println("ID: " + c.getId() +
                            " | Precio: " + c.getPrecio() +
                            " | Techada: " + c.isEsTechada() +
                            " | Disponible: " + c.isEstaDispoonible());
                    System.out.print("  Horarios: ");
                    for (Time h : c.getHorario().getHorarios()) {
                        System.out.print(h + " ");
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Error en búsqueda avanzada: " + e.getMessage());
        }
    }
}
