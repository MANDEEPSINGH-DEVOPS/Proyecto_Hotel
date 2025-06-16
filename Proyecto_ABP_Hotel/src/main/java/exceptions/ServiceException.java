package exceptions;

public class ServiceException extends Exception {

    // Códigos de error para servicios
    public static final int ERROR_DatosInvalidos = 1;
    public static final int ERROR_NoEncontrado = 2;

    private int code;

    public ServiceException(int code) {
        this.code = code;
    }

    public ServiceException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    @Override
    public String getMessage() {
        switch (code) {
            case ERROR_DatosInvalidos:
                return "Datos inválidos proporcionados.";
            case ERROR_NoEncontrado:
                return "Elemento no encontrado.";
            default:
                return "Error no identificado.";
        }
    }

    public int getCode() {
        return code;
    }
}
