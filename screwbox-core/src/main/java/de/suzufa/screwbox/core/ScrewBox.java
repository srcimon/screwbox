package de.suzufa.screwbox.core;

public final class ScrewBox {

    private static boolean initialized;

    private ScrewBox() {
    }

    public static Engine createEngine() {
        if (initialized) {
            throw new IllegalStateException("only one instance of ScrewBox can be created");
        }
        initialized = true;
        return new DefaultEngine();
    }

}
