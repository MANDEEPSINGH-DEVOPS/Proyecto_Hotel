package imagenHabitaciones;
import exceptions.DAOException;

import java.io.IOException;
import java.sql.*;
import java.io.InputStream;

public class ImagenInsertsHabs {

    // Parámetros de conexión
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

    public void updateHabitacionImagen(int habitacionId, String rutaImagen) throws SQLException, DAOException {
        String update = "UPDATE habitaciones SET imagen = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(update);
             InputStream is = getClass().getResourceAsStream(rutaImagen)) {

            System.out.println("Buscando imagen en: " + getClass().getResource(rutaImagen));

            if (is == null) {
                System.out.println("No se pudo encontrar la imagen en el classpath.");
                return;
            }

            ps.setBinaryStream(1, is, is.available());
            ps.setInt(2, habitacionId);

            int filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                System.out.println("Imagen actualizada con ID: " + habitacionId);
            } else {
                System.out.println("No se encontró ninguna habitación con el ID especificado.");
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de imagen: " + e.getMessage());
            throw new DAOException(DAOException.ERROR_QueryFailed);
        } catch (SQLException e) {
            throw new DAOException(DAOException.ERROR_QueryFailed);
        }
    }

    public void desconectar() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

}