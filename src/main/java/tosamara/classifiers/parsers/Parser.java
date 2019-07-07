package tosamara.classifiers.parsers;

public abstract class Parser<T> {
    protected abstract String getPath();
    public abstract T parseFromFile();
}
