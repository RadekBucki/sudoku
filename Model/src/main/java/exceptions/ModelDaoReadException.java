package exceptions;

public class ModelDaoReadException extends RuntimeException {
    public ModelDaoReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
