package dev.screwbox.core.environment;

import java.util.List;
import java.util.function.Function;

public class ImportSource<T, I> {

    public static <T> ImportSource<T, Void> noIndex(List<T> sources) {
        return new ImportSource<>(sources, x -> null);
    }

    public static <T, I> ImportSource<T, I> indexed(List<T> sources, Function<T, I> indexFunction) {
        return new ImportSource<>(sources, indexFunction);
    }

    private ImportSource(List<T> source, Function<T, I> indexFunction) {

    }
}
