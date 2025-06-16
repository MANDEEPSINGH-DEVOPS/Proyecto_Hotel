package dao;

import exceptions.DAOException;
import model.User;
import utils.Estado;
import utils.Rol;

import java.sql.*;
import java.util.ArrayList;

public class UserDao {

    public static final String SCHEMA_NAME = "hotel"; // Cambiar por el nombre de tu schema
    public static final String CONNECTION =
            "jdbc:mysql://localhost:3306/" +
                    SCHEMA_NAME +
                    "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=True";
    public static final String USER_CONNECTION = "user"; // Cambiar por tu usuario de la BBDD
    public static final String PASS_CONNECTION = "jupiter"; // Cambiar por tu password de la BBDD

    private Connection conexion;

    public void conectar() throws SQLException, ClassNotFoundException, DAOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(CONNECTION, USER_CONNECTION, PASS_CONNECTION);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException(DAOException.ERROR_ConnectionFailed);
        }

    }

    // COMPROVACIONS DE USER Y PASSWORD
    public User getUserByUsernameAndPassword(String username, String password) throws SQLException, DAOException {
        try {
            String select = "SELECT * FROM usuarios WHERE nombre = ? AND password = ?";
            User user = null;

            try (PreparedStatement ps = conexion.prepareStatement(select)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        user = new User(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("email"),
                                rs.getString("password"),
                                Rol.valueOf(rs.getString("rol")),
                                rs.getDate("fecha_registro"),
                                Estado.valueOf(rs.getString("estado"))
                        );
                    }
                }
            }
            return user;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }


    //Usuari per id
    public User getUserById(int id) throws SQLException, DAOException {
        try {
            String select = "SELECT * FROM usuarios WHERE id = ?";
            User user = null;

            try (PreparedStatement ps = conexion.prepareStatement(select)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        user = new User(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("email"),
                                rs.getString("password"),
                                Rol.valueOf(rs.getString("rol")),
                                rs.getDate("fecha_registro"),
                                Estado.valueOf(rs.getString("estado"))

                        );
                    }
                }
            }
            return user;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    //Agafar tots usuaris
    public ArrayList<User> getAllUsers() throws SQLException, DAOException {

        try {
            ArrayList<User> userList = new ArrayList<>();

            try (Statement st = conexion.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT * FROM usuarios")) {
                    while (rs.next()) {
                        User user = new User(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("email"),
                                rs.getString("password"),
                                Rol.valueOf(rs.getString("rol")),
                                rs.getDate("fecha_registro"),
                                Estado.valueOf(rs.getString("estado"))
                        );
                        userList.add(user);
                    }
                }
            }
            return userList;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    //Insertar nou usuari
    // Insertar nuevo usuario en UserDao
    public void insertNewUser(User user) throws SQLException, DAOException {
        try {

            String insert = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = conexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                ps.setString(4, Rol.cliente.name());
                ps.executeUpdate();

                // Obtener el ID autogenerado y asignarlo al objeto User
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1)); // Asignar el ID generado
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }


    public void updateUser(User user) throws SQLException, DAOException {
        try {
            String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ?, rol = ? WHERE id = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);

            // Aquí asignamos los valores en el orden correcto
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRol().toString());
            statement.setInt(5, user.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }


    //Canvia l'estat
    public void eliminarUser(int id) throws SQLException, DAOException {
        try {
            String update = "UPDATE usuarios SET estado = ? WHERE id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(update)) {
                ps.setString(1, Estado.eliminado.name());
                ps.setInt(2, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public void updateAdmin(int id) throws SQLException, DAOException {
        try {
            String update = "UPDATE usuarios SET rol = ? WHERE id = ?";
            try (PreparedStatement ps = conexion.prepareStatement(update)) {
                ps.setString(1, Rol.admin.name());
                ps.setInt(2, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    // Desconectar
    public void desconectar() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }
}
