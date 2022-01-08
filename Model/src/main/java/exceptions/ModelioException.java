package exceptions;

public class ModelioException extends RuntimeException {
    public ModelioException(String message, Throwable cause) {
        super(message, cause);
    }
}
