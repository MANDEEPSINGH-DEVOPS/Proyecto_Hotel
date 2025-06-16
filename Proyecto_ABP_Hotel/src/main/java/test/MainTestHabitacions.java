package test;

import dao.HabitacionsDao;
import exceptions.DAOException;
import model.Habitacions;
import utils.Estado_H;
import utils.Tipo_Habitacion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainTestHabitacions {

    static HabitacionsDao habitacionsDao = new HabitacionsDao();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            habitacionsDao.conectar();
            boolean exit = false;

            while (!exit) {
                mostrarMenu();
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        crearHabitacion();
                        break;
                    case 2:
                        listarHabitaciones();
                        break;
                    case 3:
                        buscarHabitacionPorId();
                        break;
                    case 4:
                        actualizarHabitacion();
                        break;
                    case 5:
                        eliminarHabitacion();
                        break;
                    case 0:
                        exit = true;
                        habitacionsDao.desconectar();
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
        System.out.println("      Gestión de Habitaciones");
        System.out.println("=================================");
        System.out.println("1. Crear nueva habitación");
        System.out.println("2. Listar todas las habitaciones");
        System.out.println("3. Buscar habitación por ID");
        System.out.println("4. Actualizar habitación");
        System.out.println("5. Eliminar habitación");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void crearHabitacion() throws SQLException, DAOException {
        System.out.println("=== Crear nueva habitación ===");

        System.out.print("Tipo de habitación (SIMPLE, DOBLE, SUITE): ");
        Tipo_Habitacion tipoHabitacion = Tipo_Habitacion.valueOf(scanner.nextLine().toLowerCase());
        System.out.print("Precio: ");
        float precio = scanner.nextFloat();
        scanner.nextLine(); // Consumir el salto de línea
        System.out.print("Estado (DISPONIBLE, OCUPADA, MANTENIMIENTO): ");
        Estado_H estado = Estado_H.valueOf(scanner.nextLine().toLowerCase());

        Habitacions nuevaHabitacion = new Habitacions(0, tipoHabitacion, null, precio, estado);
        habitacionsDao.insertNewHabitacion(nuevaHabitacion);
        System.out.println("Habitación creada exitosamente.");
    }

    private static void listarHabitaciones() throws SQLException, DAOException {
        System.out.println("=== Listar todas las habitaciones ===");
        ArrayList<Habitacions> habitaciones = habitacionsDao.getAllHabitaciones();

        for (Habitacions habitacion : habitaciones) {
            System.out.println(habitacion);
        }
    }

    private static void buscarHabitacionPorId() throws SQLException, DAOException {
        System.out.println("=== Buscar habitación por ID ===");
        System.out.print("Introduce el ID de la habitación: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Habitacions habitacion = habitacionsDao.getHabitacionById(id);
        if (habitacion != null) {
            System.out.println(habitacion);
        } else {
            System.out.println("No se encontró ninguna habitación con el ID proporcionado.");
        }
    }

    private static void actualizarHabitacion() throws SQLException, DAOException {
        System.out.println("=== Actualizar habitación ===");
        System.out.print("Introduce el ID de la habitación que deseas actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Habitacions habitacion = habitacionsDao.getHabitacionById(id);
        if (habitacion != null) {
            System.out.println("Habitación actual: " + habitacion);

            System.out.print("Nuevo tipo de habitación (actual: " + habitacion.getTipo_habitacion() + "): ");
            Tipo_Habitacion nuevoTipoHabitacion = Tipo_Habitacion.valueOf(scanner.nextLine().toLowerCase());
            System.out.print("Nuevo precio (actual: " + habitacion.getPrice() + "): ");
            float nuevoPrecio = scanner.nextFloat();
            scanner.nextLine();
            System.out.print("Nuevo estado (actual: " + habitacion.getEstado() + "): ");
            Estado_H nuevoEstado = Estado_H.valueOf(scanner.nextLine().toLowerCase());

            habitacion.setTipo_habitacion(nuevoTipoHabitacion);
            habitacion.setPrice(nuevoPrecio);
            habitacion.setEstado(nuevoEstado);

            habitacionsDao.updateHabitacion(habitacion);
            System.out.println("Habitación actualizada exitosamente.");
        } else {
            System.out.println("No se encontró ninguna habitación con el ID proporcionado.");
        }
    }

    private static void eliminarHabitacion() throws SQLException, DAOException {
        System.out.println("=== Eliminar habitación ===");
        System.out.print("Introduce el ID de la habitación que deseas eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        habitacionsDao.deleteHabitacion(id);
        System.out.println("Habitación eliminada exitosamente.");
    }
}
