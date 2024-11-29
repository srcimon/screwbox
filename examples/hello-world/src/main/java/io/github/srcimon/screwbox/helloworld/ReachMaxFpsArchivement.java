package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;

public class ReachMaxFpsArchivement implements ArchivementDefinition {

    @Override
    public ArchivementOptions defineArchivement() {
        return ArchivementOptions.title("reach max fps")
                .goal(150)
                .progressionMode(ArchivementOptions.ProgressionMode.SUM);
    }

    @Override
    public int progress(Engine engine) {
        return engine.loop().fps();
    }
}
