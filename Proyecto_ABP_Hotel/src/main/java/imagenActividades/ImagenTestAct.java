package imagenActividades;
import exceptions.DAOException;

import java.io.IOException;
import java.sql.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImagenTestAct {
    public static void main(String[] args) {
        ImagenInsertsAct imagenInserts = new ImagenInsertsAct();

        try {
            // Conecta a la base de datos
            imagenInserts.conectar();

            Map<Integer, String> imagenesMap= new HashMap<>();
            imagenesMap.put(1,"/imagenByte/act1.jpg");
            imagenesMap.put(2,"/imagenByte/act2.jpg");
            imagenesMap.put(3,"/imagenByte/act3.jpg");
            imagenesMap.put(4,"/imagenByte/act4.jpg");
            imagenesMap.put(5,"/imagenByte/act5.jpg");
            imagenesMap.put(9,"/imagenByte/act6.jpg");

            // ID de la habitación a la que deseas actualizar la imagen
            int activitatId = 9;
            String rutaImagen = imagenesMap.get(activitatId);

            if (rutaImagen != null) {
                imagenInserts.updateActivitatImagen(activitatId, rutaImagen);
            } else {
                System.out.println("No se encontró la ruta de imagen para el ID de habitación: " + activitatId);
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
