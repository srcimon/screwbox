package io.github.srcimon.screwbox.core.archivements;

@FunctionalInterface
public interface ArchivementDefinition {

    ArchivementOptions defineArchivement();

    default boolean supports() {
        return false;
    }

}
