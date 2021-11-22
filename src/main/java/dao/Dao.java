package dao;

import java.io.IOException;

public interface Dao<T> {
    T read() throws IOException, ClassNotFoundException;

    void write(T obj) throws IOException;
}
