package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.Engine;

@FunctionalInterface
public interface Archivement {

    ArchivementDetails details();

    default int progress(final Engine engine) {
        return 0;
    }

}
