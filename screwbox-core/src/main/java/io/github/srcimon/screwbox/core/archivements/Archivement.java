package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.Engine;

@FunctionalInterface
public interface Archivement {

    ArchivementConfiguration configuration();

    default int progress(final Engine engine) {
        return 0;
    }

}
