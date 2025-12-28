package dev.screwbox.core.environment.sourceimport;

public interface ImportContext {

    int allocateId();

    int peekId();
}
