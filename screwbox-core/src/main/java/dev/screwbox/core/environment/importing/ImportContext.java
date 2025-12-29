package dev.screwbox.core.environment.importing;

public interface ImportContext {

    int allocateId();

    int peekId();

}
