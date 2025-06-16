package exceptions;

import java.util.Arrays;
import java.util.List;

public class DAOException extends Exception {

    private int value;

    public static final int ERROR_ConnectionFailed = 0;
    public static final int ERROR_QueryFailed = 1;

    private static final List<String> daoMessages = Arrays.asList(
            "Fallo en la conexión con la base de datos",
            "Error al ejecutar la consulta en la base de datos"
    );

    public DAOException(int value) {
        this.value = value;
    }

    @Override
    public String getMessage() {
        if (value >= 0 && value < daoMessages.size()) {
            return daoMessages.get(value);
        }
        return "Error desconocido en el DAO";
    }
}
