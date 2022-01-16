package exceptions;

public class ModelJdbcException extends Throwable {
    public ModelJdbcException(String message, Throwable cause) {
        super(message, cause);
    }
}
