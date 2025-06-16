package dao;

import exceptions.DAOException;
import model.Habitacions;
import utils.Estado_H;
import utils.Tipo_Habitacion;

import java.sql.*;
import java.util.ArrayList;

public class HabitacionsDao {

    public static final String SCHEMA_NAME = "hotel";
    public static final String CONNECTION =
            "jdbc:mysql://localhost:3306/" +
                    SCHEMA_NAME +
                    "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=True";
    public static final String USER_CONNECTION = "user";
    public static final String PASS_CONNECTION = "jupiter";

    private Connection conexion;

    public void conectar() throws SQLException, ClassNotFoundException, DAOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(CONNECTION, USER_CONNECTION, PASS_CONNECTION);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException(DAOException.ERROR_ConnectionFailed);
        }

    }

    public Habitacions getHabitacionById(int id) throws SQLException, DAOException {
        try {
            String select = "SELECT * FROM habitaciones WHERE id = ?";
            Habitacions habitacion = null;

            try (PreparedStatement ps = conexion.prepareStatement(select)) {
                ps.setInt(1, id);
                System.out.println(ps.toString());

                try (ResultSet rs = ps.executeQuery()) {
                    Blob blob = rs.getBlob("imagen");
                    byte[] imagenBytes = blob != null ? blob.getBytes(1, (int) blob.length()) : null;

                    if (rs.next()) {
                        habitacion = new Habitacions(

                                rs.getInt("id"),
                                Tipo_Habitacion.valueOf(rs.getString("tipo_habitacion").toLowerCase()),
                                imagenBytes,
                                rs.getFloat("precio"),
                                Estado_H.valueOf(rs.getString("estado").toLowerCase())
                        );
                    }
                }
            }
            return habitacion;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public ArrayList<Habitacions> getAllHabitaciones() throws SQLException, DAOException {
        try {
            ArrayList<Habitacions> habitacionesList = new ArrayList<>();

            try (Statement st = conexion.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT * FROM habitaciones")) {
                    while (rs.next()) {

                        Blob blob = rs.getBlob("imagen");
                        byte[] imagenBytes = blob != null ? blob.getBytes(1, (int) blob.length()) : null;

                        Habitacions habitacion = new Habitacions(
                                rs.getInt("id"),
                                Tipo_Habitacion.valueOf(rs.getString("tipo_habitacion").toLowerCase()),
                                imagenBytes,
                                rs.getFloat("precio"),
                                Estado_H.valueOf(rs.getString("estado").toLowerCase())
                        );
                        habitacionesList.add(habitacion);
                    }
                }
            }
            return habitacionesList;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public void insertNewHabitacion(Habitacions habitacion) throws SQLException, DAOException {
        try {
            String insert = "INSERT INTO habitaciones (tipo_habitacion, precio, estado) VALUES (?, ?, ?)";

            try (PreparedStatement ps = conexion.prepareStatement(insert)) {
                ps.setString(1, habitacion.getTipo_habitacion().name());
                ps.setFloat(2, habitacion.getPrice());
                ps.setString(3, habitacion.getEstado().name());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public void updateHabitacion(Habitacions habitacion) throws SQLException, DAOException {
        try {
            String update = "UPDATE habitaciones SET estado = ? WHERE id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(update)) {
                ps.setString(1, habitacion.getEstado().name());
                ps.setInt(2, habitacion.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public void deleteHabitacion(int id) throws SQLException, DAOException {
        try {
            String delete = "DELETE FROM habitaciones WHERE id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(delete)) {
                ps.setInt(1, id);
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
