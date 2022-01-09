package dao;

import exceptions.ModelCloneNotSupportedException;
import exceptions.ModelDaoReadException;
import exceptions.ModelDaoWriteException;
import exceptions.ModelNullPointerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTest {
    @Test
    @DisplayName("ExceptionsTest")
    void ExceptionTests() {
        assertThrows(ModelDaoWriteException.class,() -> {
            Exception exception = new Exception();
            throw new ModelDaoWriteException("testModelDaoWriteException",exception);});
        assertThrows(ModelDaoReadException.class,() -> {
            Exception exception = new Exception();
            throw new ModelDaoReadException("testModelDaoReadException",exception);});
        assertThrows(ModelCloneNotSupportedException.class,() -> {
            throw new ModelCloneNotSupportedException("testModelCloneNotSupportedException");});
        assertThrows(ModelNullPointerException.class,() -> {
            throw new ModelNullPointerException("testModelNullPointerException");});
    }
}
