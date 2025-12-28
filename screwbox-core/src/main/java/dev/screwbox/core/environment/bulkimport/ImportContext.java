package dev.screwbox.core.environment.bulkimport;

public interface ImportContext {

    int allocateId();

    int peekId();
}
