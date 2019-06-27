package tosamara.classifiers.parsers;

import java.util.List;

public abstract class Parser<T> {
    protected abstract String getPath();
    public abstract List<T> parse();
}
