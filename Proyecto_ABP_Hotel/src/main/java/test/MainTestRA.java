package test;

import dao.Reserva_ActividadDao;
import exceptions.DAOException;
import model.Reserva_Actividad;
import utils.Estado_Reserva;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class MainTestRA {

    static Reserva_ActividadDao reservaActividadDao = new Reserva_ActividadDao();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            reservaActividadDao.conectar();
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
                    case 0:
                        exit = true;
                        reservaActividadDao.desconectar();
                        System.out.println("¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mostrarMenu() {
        System.out.println("=================================");
        System.out.println("     Gestión de Reservas de Actividades");
        System.out.println("=================================");
        System.out.println("1. Crear nueva reserva");
        System.out.println("2. Listar todas las reservas");
        System.out.println("3. Buscar reserva por ID");
        System.out.println("4. Actualizar reserva");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void crearReserva() throws SQLException, DAOException {
        System.out.println("=== Crear nueva reserva ===");

        System.out.print("ID Usuario: ");
        int idUsuario = scanner.nextInt();
        System.out.print("ID Actividad: ");
        int idActividad = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Estado (reservado, cancelado, completado): ");
        Estado_Reserva estado = Estado_Reserva.valueOf(scanner.nextLine().toLowerCase());
        Date fecha = new Date();

        Reserva_Actividad nuevaReserva = new Reserva_Actividad(idUsuario, idActividad, estado);
        nuevaReserva.setFecha(fecha);

        reservaActividadDao.insertNewRa(nuevaReserva);
        System.out.println("Reserva creada exitosamente.");
    }

    private static void listarReservas() throws SQLException, DAOException {
        System.out.println("=== Listar todas las reservas ===");
        ArrayList<Reserva_Actividad> reservas = reservaActividadDao.getAllRa();

        for (Reserva_Actividad reserva : reservas) {
            System.out.println(reserva);
        }
    }

    private static void buscarReservaPorId() throws SQLException, DAOException {
        System.out.println("=== Buscar reserva por ID ===");
        System.out.print("Introduce el ID de la reserva: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Reserva_Actividad reserva = reservaActividadDao.raById(id);
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

        Reserva_Actividad reserva = reservaActividadDao.raById(id);
        if (reserva != null) {
            System.out.println("Reserva actual: " + reserva);

            System.out.print("Nuevo ID Usuario (actual: " + reserva.getId_user() + "): ");
            int nuevoIdUsuario = scanner.nextInt();
            System.out.print("Nuevo ID Actividad (actual: " + reserva.getId_actividad() + "): ");
            int nuevoIdActividad = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nuevo Estado (reservado, cancelado, completado, actual: " + reserva.getEstadoReserva() + "): ");
            Estado_Reserva nuevoEstado = Estado_Reserva.valueOf(scanner.nextLine().toLowerCase());

            reserva.setId_user(nuevoIdUsuario);
            reserva.setId_actividad(nuevoIdActividad);
            reserva.setEstadoReserva(nuevoEstado);

            reservaActividadDao.updateRa(reserva);
            System.out.println("Reserva actualizada exitosamente.");
        } else {
            System.out.println("No se encontró ninguna reserva con el ID proporcionado.");
        }
    }

}
