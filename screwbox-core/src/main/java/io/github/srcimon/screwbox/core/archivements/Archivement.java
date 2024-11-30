package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.Engine;

@FunctionalInterface
public interface Archivement {

    ArchivementOptions options();

    default int progress(Engine engine) {
        return 0;
    }


}
