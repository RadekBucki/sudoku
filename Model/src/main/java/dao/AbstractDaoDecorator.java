package dao;

public abstract class AbstractDaoDecorator<T> implements Dao<T> {

    private final Dao<T> delegate;

    public AbstractDaoDecorator(Dao<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T read() {
        return delegate.read();
    }

    @Override
    public void write(T obj) {
        delegate.write(obj);
    }

    @Override
    public void close() throws Exception {
    }
}
