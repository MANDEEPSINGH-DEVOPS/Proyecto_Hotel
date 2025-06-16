package dao;

import exceptions.DAOException;
import utils.Estado_Reserva;

import java.sql.*;
import java.util.ArrayList;

public class Reserva_HabitacionDao{

    public static final String SCHEMA_NAME = "hotel"; //modificar con el nombre de vuestro schema (Base de datos)
    public static final String CONNECTION =
            "jdbc:mysql://localhost:3306/" +
                    SCHEMA_NAME +
                    "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=True";
    public static final String USER_CONNECTION = "user"; //modificar por vuestro usuario de BBDD (root)
    public static final String PASS_CONNECTION = "jupiter"; //modificar por vuestro password de BBDD (root)

    private Connection conexion;

    public void conectar() throws SQLException, ClassNotFoundException, DAOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(CONNECTION, USER_CONNECTION, PASS_CONNECTION);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException(DAOException.ERROR_ConnectionFailed);
        }

    }

    public ArrayList<model.Reserva_Habitacion> getAllRh() throws SQLException, DAOException {
        try {
            ArrayList<model.Reserva_Habitacion> rhList = new ArrayList<>();

            try (Statement st = conexion.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT * FROM reserva_habitaciones")) {
                    while (rs.next()) {
                        model.Reserva_Habitacion rh = new model.Reserva_Habitacion();
                        rh.setId(rs.getInt("id"));
                        rh.setId_usuario(rs.getInt("id_usuario"));
                        rh.setId_habitacion(rs.getInt("id_habitacion"));
                        rh.setFecha_entrada(rs.getDate("fecha_entrada").toLocalDate());
                        rh.setFecha_salida(rs.getDate("fecha_salida").toLocalDate());
                        rh.setEstado(utils.Estado_Reserva.valueOf(rs.getString("estado").toLowerCase()));
                        rh.setFecha_reserva(rs.getTimestamp("fecha_reserva"));
                        rhList.add(rh);
                    }
                }
            }
            return rhList;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }


    public void insertNewRh(model.Reserva_Habitacion rh) throws SQLException, DAOException {
        try {
            String insert = "INSERT INTO reserva_habitaciones (id_usuario, id_habitacion, fecha_entrada, fecha_salida, estado) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conexion.prepareStatement(insert)) {
                ps.setInt(1, rh.getId_usuario());
                ps.setInt(2, rh.getId_habitacion());
                ps.setDate(3, java.sql.Date.valueOf(rh.getFecha_entrada()));
                ps.setDate(4, java.sql.Date.valueOf(rh.getFecha_salida()));
                ps.setString(5, Estado_Reserva.reservado.name());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }


    public void updateReservaHabitacion(model.Reserva_Habitacion rh) throws SQLException, DAOException {
        try {
            String update = "UPDATE reserva_habitaciones SET estado = ? WHERE id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(update)) {
                ps.setString(1, rh.getEstado().name());
                ps.setInt(2, rh.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public void desconectar() throws SQLException {
        if(conexion!=null){
            conexion.close();
        }
    }



}