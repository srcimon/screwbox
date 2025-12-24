package dev.screwbox.core.environment;

public interface ImportContext {

    int allocateId();

    int peekId();
}
