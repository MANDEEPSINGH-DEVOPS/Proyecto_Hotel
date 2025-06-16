package dao;

import exceptions.DAOException;
import model.Reserva_Actividad;
import utils.Estado_Reserva;

import java.sql.*;
import java.util.ArrayList;

public class Reserva_ActividadDao {

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

    public Reserva_Actividad raById(int id) throws SQLException, DAOException {
        try {
            String select = "SELECT * FROM reserva_actividades WHERE id = ?";
            Reserva_Actividad ra = null;

            try (PreparedStatement ps = conexion.prepareStatement(select)) {
                ps.setInt(1, id);
                System.out.println(ps.toString());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ra = new Reserva_Actividad();
                        ra.setId(rs.getInt("id"));
                        ra.setId_user(rs.getInt("id_usuario"));
                        ra.setId_actividad(rs.getInt("id_actividad"));
                        ra.setEstadoReserva(Estado_Reserva.valueOf(rs.getString("estado"))); // Parse de String a Enum
                        ra.setFecha(rs.getDate("fecha_reserva"));
                    }
                }
            }
            return ra;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public ArrayList<Reserva_Actividad> getAllRa() throws SQLException, DAOException {
        try {
            ArrayList<Reserva_Actividad> raList = new ArrayList<>();

            try (Statement st = conexion.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT * FROM reserva_actividades")) {
                    while (rs.next()) {
                        Reserva_Actividad ra = new Reserva_Actividad();
                        ra.setId(rs.getInt("id"));
                        ra.setId_user(rs.getInt("id_usuario"));
                        ra.setId_actividad(rs.getInt("id_actividad"));
                        ra.setEstadoReserva(Estado_Reserva.valueOf(rs.getString("estado"))); // Parse de String a Enum
                        ra.setFecha(rs.getDate("fecha_reserva")); // La fecha ya es Date
                        raList.add(ra);
                    }
                }
            }
            return raList;
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }

    public void insertNewRa(Reserva_Actividad ra) throws SQLException, DAOException {
        try {
            String insert = "INSERT INTO reserva_actividades (id_usuario, id_actividad, estado) VALUES (?, ?, ?)";

            try (PreparedStatement ps = conexion.prepareStatement(insert)) {
                ps.setInt(1, ra.getId_user());
                ps.setInt(2, ra.getId_actividad());
                ps.setString(3, Estado_Reserva.reservado.name());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed);
        }

    }

    public void updateRa(Reserva_Actividad ra) throws SQLException, DAOException {
        try {
            String update = "UPDATE reserva_actividades SET estado = ? WHERE id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(update)) {
                ps.setString(1, ra.getEstadoReserva().name()); // El estado que has pasado en la solicitud
                ps.setInt(2, ra.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed); // Error en la consulta
        }

    }



//    public void deleteReservaActividad(int id) throws SQLException {
//        String delete = "DELETE FROM reserva_actividad WHERE id = ?";
//
//        try (PreparedStatement ps = conexion.prepareStatement(delete)) {
//            ps.setInt(1, id);
//            ps.executeUpdate();
//        }
//
//    }


    public void desconectar() throws SQLException {
        if(conexion!=null){
            conexion.close();
        }
    }
}
