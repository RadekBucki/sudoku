package exceptions;

public class ModelDaoWriteException extends RuntimeException {
    public ModelDaoWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
