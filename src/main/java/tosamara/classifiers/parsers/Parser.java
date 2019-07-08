package tosamara.classifiers.parsers;

/**
 * Parse object from file.
 * @param <T> type of parsed object
 */
public abstract class Parser<T> {
    /**
     * Path to parsed object.
     * @return path as string
     */
    protected abstract String getPath();

    /**
     * Parse object from file.
     * @return Java-Object
     */
    public abstract T parseFromFile();
}
