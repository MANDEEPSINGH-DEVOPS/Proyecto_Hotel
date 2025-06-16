package test;

import dao.UserDao;
import exceptions.DAOException;
import model.User;
import utils.Estado;
import utils.Rol;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainTestUsers {

    static UserDao userDao = new UserDao();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            userDao.conectar();
            boolean exit = false;

            while (!exit) {
                mostrarMenu();
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        listarUsuarios();
                        break;
                    case 2:
                        cambiarEstadoAEliminado();
                        break;
                    case 3:
                        crearUsuario();
                        break;
                    case 4:
                        buscarUsuarioPorId();
                        break;
                    case 5:
                        updateUser();
                        break;
                    case 6:
                        cambiarRolAAdmin();
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                userDao.desconectar(); // Asegúrate de desconectar
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("=================================");
        System.out.println("          Menú de Usuarios       ");
        System.out.println("=================================");
        System.out.println("1. Listar todos los usuarios");
        System.out.println("2. Cambiar estado de usuario a eliminado");
        System.out.println("3. Crear un usuario");
        System.out.println("4. Buscar por id");
        System.out.println("5. Actualizar un usuario");
        System.out.println("6. Convertir a adminstrador");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void crearUsuario() throws SQLException, DAOException {
        System.out.println("=== Crear nuevo usuario ===");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        User nuevoUsuario = new User();
        nuevoUsuario.setUsername(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);

        userDao.insertNewUser(nuevoUsuario);
        System.out.println("Usuario creado exitosamente con ID: " + nuevoUsuario.getId());
    }

    private static void listarUsuarios() throws SQLException, DAOException {
        System.out.println("=== Listar todos los usuarios ===");
        ArrayList<User> users = userDao.getAllUsers();

        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void cambiarRolAAdmin() throws SQLException, DAOException {
        System.out.print("Introduce el ID del usuario a rolear como admin: ");
        int idToDelete = scanner.nextInt();
        userDao.updateAdmin(idToDelete);
        System.out.println("Usuario con ID " + idToDelete + " marcado como administrador.");
    }

    private static void cambiarEstadoAEliminado() throws SQLException, DAOException {
        System.out.print("Introduce el ID del usuario a marcar como eliminado: ");
        int idToDelete = scanner.nextInt();
        userDao.eliminarUser(idToDelete);
        System.out.println("Usuario con ID " + idToDelete + " marcado como eliminado.");
    }

    private static void buscarUsuarioPorId() throws SQLException, DAOException {
        System.out.println("=== Buscar usuario por ID ===");
        System.out.print("Introduce el ID del usuario: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        User usuario = userDao.getUserById(id);
        if (usuario != null) {
            System.out.println(usuario);
        } else {
            System.out.println("No se encontró ningún usuario con el ID proporcionado.");
        }
    }


    private static void updateUser() throws SQLException, DAOException {
        System.out.println("=== Actualizar un usuario ===");

        // Solicitar el ID del usuario que queremos actualizar
        System.out.print("Introduce el ID del usuario a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Limpiar el buffer de entrada

        // Buscar el usuario en la base de datos
        User usuario = userDao.getUserById(id);

        if (usuario == null) {
            System.out.println("No se encontró ningún usuario con el ID proporcionado.");
            return;
        }

        // Mostrar los detalles actuales del usuario
        System.out.println("Usuario encontrado: " + usuario);

        // Solicitar nuevos valores para el usuario, dejando la opción de mantener los valores actuales
        System.out.print("Nuevo nombre (dejar en blanco para mantener el actual '" + usuario.getUsername() + "'): ");
        String nuevoNombre = scanner.nextLine();
        if (!nuevoNombre.isEmpty()) {
            usuario.setUsername(nuevoNombre);
        }

        System.out.print("Nuevo email (dejar en blanco para mantener el actual '" + usuario.getEmail() + "'): ");
        String nuevoEmail = scanner.nextLine();
        if (!nuevoEmail.isEmpty()) {
            usuario.setEmail(nuevoEmail);
        }

        System.out.print("Nueva contraseña (dejar en blanco para mantener la actual): ");
        String nuevaPassword = scanner.nextLine();
        if (!nuevaPassword.isEmpty()) {
            usuario.setPassword(nuevaPassword);
        }

        System.out.print("Nuevo rol (actual: '" + usuario.getRol() + "', opciones: ADMIN, CLIENTE): ");
        String nuevoRol = scanner.nextLine();
        if (!nuevoRol.isEmpty()) {
            usuario.setRol(Rol.valueOf(nuevoRol.toLowerCase()));  // Convertir a mayúsculas y asignar
        }

        // Actualizar el usuario en la base de datos
        userDao.updateUser(usuario);
        System.out.println("Usuario actualizado exitosamente.");
    }

}
