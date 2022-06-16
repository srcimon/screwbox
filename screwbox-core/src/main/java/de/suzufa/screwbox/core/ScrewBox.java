package de.suzufa.screwbox.core;

import static java.util.Objects.nonNull;

public final class ScrewBox {

    private static Engine engine;

    private ScrewBox() {
    }

    public static Engine createEngine() {
        if (nonNull(engine)) {
            throw new IllegalStateException("only one instance of ScrewBox can be created");
        }
        engine = new DefaultEngine();
        return engine;
    }

}
