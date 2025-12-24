package dev.screwbox.core.environment;

import java.util.List;

public class BlueprintImport<T, I> {

    private final Environment environment;
    private final List<T> souces;
    private final ImportRules<T, I> options;

    public BlueprintImport(Environment environment, List<T> sources, ImportRules<T, I> options) {
        this.environment = environment;
        this.souces = sources;
        this.options = options;
    }

    public void run() {

    }
}
