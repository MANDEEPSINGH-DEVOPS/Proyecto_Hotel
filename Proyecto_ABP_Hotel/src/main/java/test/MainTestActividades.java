package test;

import dao.ActivitatsDao;
import exceptions.DAOException;
import model.Activitats;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class MainTestActividades {
    private static ActivitatsDao activitatsDao = new ActivitatsDao();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            activitatsDao.conectar();
            menu();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                activitatsDao.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void menu() {
        int opcion = 0;

        do {
            System.out.println("1. Crear nueva actividad");
            System.out.println("2. Ver todas las actividades");
            System.out.println("3. Buscar actividad por ID");
            System.out.println("4. Actualizar actividad");
            System.out.println("5. Eliminar actividad");
            System.out.println("6. Salir");

            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    crearActividad();
                    break;
                case 2:
                    verTodasActividades();
                    break;
                case 3:
                    buscarActividadPorId();
                    break;
                case 4:
                    actualizarActividad();
                    break;
                case 5:
                    eliminarActividad();
                    break;
                case 6:
                    System.out.println("exit");
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        } while (opcion != 6);
    }

    private static void crearActividad() {
        System.out.println("\n--- Crear Nueva Actividad ---");
        System.out.print("Nombre de la actividad: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Precio: ");
        float precio = scanner.nextFloat();
        System.out.print("Cupo: ");
        int cupo = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Fecha de la actividad (yyyy-mm-dd): ");
        LocalDate fechaActividad = LocalDate.parse(scanner.nextLine());

        Activitats nuevaActividad = new Activitats(nombre, descripcion, precio, cupo, fechaActividad);

        try {
            activitatsDao.insertNewActividad(nuevaActividad);
            System.out.println("Actividad creada con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear la actividad.");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void verTodasActividades() {
        System.out.println("\n--- Listar Todas las Actividades ---");

        try {
            ArrayList<Activitats> actividades = activitatsDao.getAllActividades();
            for (Activitats act : actividades) {
                System.out.println(act);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al listar actividades.");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void buscarActividadPorId() {
        System.out.println("\n--- Buscar Actividad por ID ---");
        System.out.print("Introduce el ID de la actividad: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            Activitats act = activitatsDao.getActividadById(id);
            if (act != null) {
                System.out.println(act);
            } else {
                System.out.println("No se encontró una actividad con el ID proporcionado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al buscar la actividad.");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void actualizarActividad() {
        System.out.println("\n--- Actualizar Actividad ---");
        System.out.print("Introduce el ID de la actividad a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            Activitats act = activitatsDao.getActividadById(id);
            if (act != null) {
                System.out.print("Nuevo nombre (actual: " + act.getNom() + "): ");
                String nuevoNombre = scanner.nextLine();
                act.setNom(nuevoNombre);

                System.out.print("Nueva descripción (actual: " + act.getDescripcion() + "): ");
                String nuevaDescripcion = scanner.nextLine();
                act.setDescripcion(nuevaDescripcion);

                System.out.print("Nuevo precio (actual: " + act.getPrecio() + "): ");
                float nuevoPrecio = scanner.nextFloat();
                act.setPrecio(nuevoPrecio);

                System.out.print("Nuevo cupo (actual: " + act.getCupo() + "): ");
                int nuevoCupo = scanner.nextInt();
                act.setCupo(nuevoCupo);

                scanner.nextLine();
                System.out.print("Nueva fecha (actual: " + act.getFecha_actividad() + ", formato yyyy-mm-dd): ");
                LocalDate nuevaFecha = LocalDate.parse(scanner.nextLine());
                act.setFecha_actividad(nuevaFecha);


                activitatsDao.updateActividad(act, id);
                System.out.println("Actividad actualizada con éxito.");
            } else {
                System.out.println("No se encontró una actividad con el ID proporcionado.");
            }
        } catch (SQLException | DAOException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar la actividad.");
        }
    }

    private static void eliminarActividad() {
        System.out.println("\n--- Eliminar Actividad ---");
        System.out.print("Introduce el ID de la actividad a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            activitatsDao.deleteActividad(id);
            System.out.println("Actividad eliminada con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar la actividad.");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
}
