package dev.screwbox.core.environment;

import java.util.List;

public class BlueprintImport<T, I> {

    private final Environment environment;
    private final List<T> souces;
    private final BlueprintImportOptions<T, I> options;

    public BlueprintImport(Environment environment, List<T> sources, BlueprintImportOptions<T, I> options) {
        this.environment = environment;
        this.souces = sources;
        this.options = options;
    }

    public void run() {

    }
}
