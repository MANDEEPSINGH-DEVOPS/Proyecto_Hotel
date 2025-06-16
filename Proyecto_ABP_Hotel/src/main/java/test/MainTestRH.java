package test;

import dao.Reserva_HabitacionDao;
import exceptions.DAOException;
import model.Reserva_Habitacion;
import utils.Estado_Reserva;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class MainTestRH {
/*
    static Reserva_HabitacionDao reservaHabitacionDao = new Reserva_HabitacionDao();
    static Scanner scanner = new Scanner(System.in);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try {
            reservaHabitacionDao.conectar();
            boolean exit = false;

            while (!exit) {
                mostrarMenu();
                int opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        crearReserva();
                        break;
                    case 2:
                        listarReservas();
                        break;
                    case 3:
                        buscarReservaPorId();
                        break;
                    case 4:
                        actualizarReserva();
                        break;
                    case 5:
                        eliminarReserva();
                        break;
                    case 0:
                        exit = true;
                        reservaHabitacionDao.desconectar();
                        System.out.println("¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }
        } catch (SQLException | ClassNotFoundException | DAOException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        System.out.println("=================================");
        System.out.println("     Gestión de Reservas de Habitaciones");
        System.out.println("=================================");
        System.out.println("1. Crear nueva reserva");
        System.out.println("2. Listar todas las reservas");
        System.out.println("3. Buscar reserva por ID");
        System.out.println("4. Actualizar reserva");
        System.out.println("5. Eliminar reserva");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void crearReserva() throws SQLException {
        System.out.println("=== Crear nueva reserva ===");

        System.out.print("ID Usuario: ");
        int idUsuario = scanner.nextInt();
        System.out.print("ID Habitación: ");
        int idHabitacion = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Fecha de entrada (yyyy-mm-dd hh:mm:ss): ");
        String fechaEntradaStr = scanner.nextLine();
        System.out.print("Fecha de salida (yyyy-mm-dd hh:mm:ss): ");
        String fechaSalidaStr = scanner.nextLine();
        System.out.print("Estado (reservado, cancelado, completado): ");
        Estado_Reserva estado = Estado_Reserva.valueOf(scanner.nextLine().toLowerCase());

        try {

            Date fechaEntrada = dateFormat.parse(fechaEntradaStr);
            Date fechaSalida = dateFormat.parse(fechaSalidaStr);

            Reserva_Habitacion nuevaReserva = new Reserva_Habitacion(idUsuario, idHabitacion, fechaEntrada, fechaSalida, estado);
            reservaHabitacionDao.insertNewRh(nuevaReserva);
            System.out.println("Reserva creada exitosamente.");
        } catch (ParseException e) {
            System.out.println("Formato de fecha inválido. Inténtalo de nuevo.");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void listarReservas() throws SQLException, DAOException {
        System.out.println("=== Listar todas las reservas ===");
        ArrayList<Reserva_Habitacion> reservas = reservaHabitacionDao.getAllRh();

        for (Reserva_Habitacion reserva : reservas) {
            System.out.println(reserva);
        }
    }

    private static void buscarReservaPorId() throws SQLException, DAOException {
        System.out.println("=== Buscar reserva por ID ===");
        System.out.print("Introduce el ID de la reserva: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Reserva_Habitacion reserva = reservaHabitacionDao.rhById(id);
        if (reserva != null) {
            System.out.println(reserva);
        } else {
            System.out.println("No se encontró ninguna reserva con el ID proporcionado.");
        }
    }

    private static void actualizarReserva() throws SQLException, DAOException {
        System.out.println("=== Actualizar reserva ===");
        System.out.print("Introduce el ID de la reserva que deseas actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Reserva_Habitacion reserva = reservaHabitacionDao.rhById(id);
        if (reserva != null) {
            System.out.println("Reserva actual: " + reserva);

            System.out.print("Nuevo ID Usuario (actual: " + reserva.getId_usuario() + "): ");
            int nuevoIdUsuario = scanner.nextInt();
            System.out.print("Nuevo ID Habitación (actual: " + reserva.getId_habitacion() + "): ");
            int nuevoIdHabitacion = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nueva Fecha de entrada (actual: " + dateFormat.format(reserva.getFecha_entrada()) + "): ");
            String nuevaFechaEntradaStr = scanner.nextLine();
            System.out.print("Nueva Fecha de salida (actual: " + dateFormat.format(reserva.getFecha_salida()) + "): ");
            String nuevaFechaSalidaStr = scanner.nextLine();
            System.out.print("Nuevo Estado (reservado, cancelado, completado, actual: " + reserva.getEstado() + "): ");
            Estado_Reserva nuevoEstado = Estado_Reserva.valueOf(scanner.nextLine().toLowerCase());

            try {
                Date nuevaFechaEntrada = dateFormat.parse(nuevaFechaEntradaStr);
                Date nuevaFechaSalida = dateFormat.parse(nuevaFechaSalidaStr);

                reserva.setId_usuario(nuevoIdUsuario);
                reserva.setId_habitacion(nuevoIdHabitacion);
                reserva.setFecha_entrada(nuevaFechaEntrada);
                reserva.setFecha_salida(nuevaFechaSalida);
                reserva.setEstado(nuevoEstado);

                reservaHabitacionDao.updateReservaHabitacion(reserva);
                System.out.println("Reserva actualizada exitosamente.");
            } catch (ParseException e) {
                System.out.println("Formato de fecha inválido. Inténtalo de nuevo.");
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No se encontró ninguna reserva con el ID proporcionado.");
        }
    }

    private static void eliminarReserva() throws SQLException, DAOException {
        System.out.println("=== Eliminar reserva ===");
        System.out.print("Introduce el ID de la reserva que deseas eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        reservaHabitacionDao.deleteReservaHabitacion(id);
        System.out.println("Reserva eliminada exitosamente.");
    }

 */
}
