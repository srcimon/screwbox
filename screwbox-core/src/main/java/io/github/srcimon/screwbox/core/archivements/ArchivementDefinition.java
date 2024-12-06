package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.Engine;

/**
 * An archivable goal to keep players engaged. Can be added to game via {@link Archivements#add(ArchivementDefinition)}.
 */
@FunctionalInterface
public interface ArchivementDefinition {

    /**
     * Configures the details of the {@link ArchivementDefinition} e.g. {@link ArchivementDetails#goal()}.
     */
    ArchivementDetails details();

    /**
     * Can be overwritten to automatically progress towards the {@link ArchivementDetails#goal()}.
     */
    default int progress(final Engine engine) {
        return 0;
    }
}
