package imagenHabitaciones;

import exceptions.DAOException;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class imageTestHabs {
    public static void main(String[] args) {
        ImagenInsertsHabs imagenInserts = new ImagenInsertsHabs();

        try {
            // Conecta a la base de datos
            imagenInserts.conectar();

            Map<Integer, String> imagenesMap= new HashMap<>();
            imagenesMap.put(1,"/imagenByte/hab1.jpg");
            imagenesMap.put(2,"/imagenByte/hab2.jpg");
            imagenesMap.put(3,"/imagenByte/hab3.jpg");
            imagenesMap.put(4,"/imagenByte/hab4.jpg");
            imagenesMap.put(5,"/imagenByte/hab5.jpg");
            imagenesMap.put(6,"/imagenByte/hab6.jpg");
            imagenesMap.put(7,"/imagenByte/hab7.jpg");
            imagenesMap.put(8,"/imagenByte/hab8.jpg");
            imagenesMap.put(9,"/imagenByte/hab9.jpg");
            imagenesMap.put(10,"/imagenByte/hab10.jpg");
            imagenesMap.put(11,"/imagenByte/hab11.jpg");

            // ID de la habitación a la que deseas actualizar la imagen
            int habitacionId = 11;
            String rutaImagen = imagenesMap.get(habitacionId);

            if (rutaImagen != null) {
                imagenInserts.updateHabitacionImagen(habitacionId, rutaImagen);
            } else {
                System.out.println("No se encontró la ruta de imagen para el ID de habitación: " + habitacionId);
            }

        } catch (SQLException | ClassNotFoundException | DAOException e) {
            e.printStackTrace();
        } finally {
            try {
                imagenInserts.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
