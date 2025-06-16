package dao;

import exceptions.DAOException;
import model.Activitats;
import utils.Estado;
import utils.EstadoActividad;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;

public class ActivitatsDao {

    public static final String SCHEMA_NAME = "hotel"; // modificar con el nombre de vuestro schema (Base de datos)
    public static final String CONNECTION =
            "jdbc:mysql://localhost:3306/" +
                    SCHEMA_NAME +
                    "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=True";
    public static final String USER_CONNECTION = "user"; // modificar por vuestro usuario de BBDD (root)
    public static final String PASS_CONNECTION = "jupiter"; // modificar por vuestro password de BBDD (root)

    private Connection conexion;

    public void conectar() throws SQLException, ClassNotFoundException, DAOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(CONNECTION, USER_CONNECTION, PASS_CONNECTION);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException(DAOException.ERROR_ConnectionFailed);
        }

    }

    public Activitats getActividadById(int id) throws SQLException, DAOException {
        try {
            String select = "SELECT * FROM actividades WHERE id = ?";
            Activitats act = null;

            try (PreparedStatement ps = conexion.prepareStatement(select)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        act = new Activitats(
                                rs.getInt("id"),
                                rs.getString("nombre_actividad"),
                                rs.getString("descripcion"),
                                rs.getFloat("precio"),
                                rs.getInt("cupo"),
                                rs.getDate("fecha_actividad").toLocalDate() // Convert java.sql.Date to LocalDate
                        );
                    }
                }
            }
            return act;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public ArrayList<Activitats> getAllActividades() throws SQLException, DAOException {
        try {
            ArrayList<Activitats> actividadesList = new ArrayList<>();

            try (Statement st = conexion.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT * FROM actividades")) {
                    while (rs.next()) {
                        Blob blob = rs.getBlob("imagen");
                        byte[] imagenBytes = blob != null ? blob.getBytes(1, (int) blob.length()) : null;

                        Activitats act = new Activitats(
                                rs.getInt("id"),
                                rs.getString("nombre_actividad"),
                                rs.getString("descripcion"),
                                imagenBytes,
                                rs.getFloat("precio"),
                                rs.getInt("cupo"),
                                rs.getDate("fecha_actividad").toLocalDate() // Convert java.sql.Date to LocalDate
                        );
                        actividadesList.add(act);
                    }
                }
            }
            return actividadesList;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public void insertNewActividad(Activitats act) throws SQLException, DAOException {
        try {
            String insert = "INSERT INTO actividades (nombre_actividad, descripcion, precio, cupo, fecha_actividad) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conexion.prepareStatement(insert)) {
                ps.setString(1, act.getNom());
                ps.setString(2, act.getDescripcion());
                ps.setFloat(3, act.getPrecio());
                ps.setInt(4, act.getCupo());
                ps.setDate(5, java.sql.Date.valueOf(act.getFecha_actividad()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public void updateActividad(Activitats act, int id) throws SQLException, DAOException {
        try {

            String update = "UPDATE actividades SET nombre_actividad = ?, descripcion = ?, precio = ?, cupo = ?, fecha_actividad = ? WHERE id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(update)) {
                ps.setString(1, act.getNom());
                ps.setString(2, act.getDescripcion());
                ps.setFloat(3, act.getPrecio());
                ps.setInt(4, act.getCupo());
                ps.setDate(5, java.sql.Date.valueOf(act.getFecha_actividad()));
                ps.setInt(6, id);

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }
    }


    public void deleteActividad(int id) throws SQLException, DAOException {
        try {
            String update = "UPDATE actividades SET estado = ? WHERE id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(update)) {
                ps.setString(1, EstadoActividad.eliminada.name());
                ps.setInt(2, id);

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }
    }

    public void desconectar() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }
}

