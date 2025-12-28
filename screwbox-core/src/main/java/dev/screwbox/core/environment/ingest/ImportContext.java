package dev.screwbox.core.environment.ingest;

public interface ImportContext {

    int allocateId();

    int peekId();
}
