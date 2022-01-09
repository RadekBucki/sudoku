package exceptions;

public class ModelCloneNotSupportedException extends CloneNotSupportedException {
    public ModelCloneNotSupportedException(String s) {
        super(s);
    }
}
