package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;

public class MouseDragArchivement implements ArchivementDefinition {

    @Override
    public ArchivementOptions defineArchivement() {
        return ArchivementOptions
                .title("Mouse travels a lot")
                .goal(20000);
    }
}
